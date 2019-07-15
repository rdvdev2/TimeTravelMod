package tk.rdvdev2.TimeTravelMod.client.gui;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.gui.ScrollPanel;
import tk.rdvdev2.TimeTravelMod.ModItems;
import tk.rdvdev2.TimeTravelMod.ModTimeMachines;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;
import tk.rdvdev2.TimeTravelMod.common.timemachine.CreativeTimeMachine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class EngineerBookScreen extends Screen {

    private ArrayList<TimeMachineData> timeMachineData;
    private DocsPanel panel;

    public EngineerBookScreen(Collection<TimeMachine> timeMachines) {
        super(new StringTextComponent("TITLE PLACEHOLDER"));

        timeMachineData = new ArrayList<>(timeMachines.size());

        Iterator<TimeMachine> iterator = timeMachines.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            TimeMachine tm = iterator.next();
            TimeMachineData d = new TimeMachineData();
            d.name = tm.getName();
            d.description = tm.getDescription();
            d.tier = tm.getTier();
            d.cooldown = tm.getCooldownTime() / 20;
            if (tm instanceof CreativeTimeMachine) {
                d.controllerBlockPos = null; // Flag to indicate the Time Machine has no building
                d.tier++; // Ensure it's the last one in the list
                timeMachineData.add(d);
                continue;
            }
            d.basicBlocksPos = tm.getBasicBlocksPos(Direction.NORTH);
            d.basicBlocks = tm.getBasicBlocks();
            d.coreBlocksPos = tm.getCoreBlocksPos(Direction.NORTH);
            d.coreBlocks = tm.getCoreBlocks();
            d.controllerBlockPos = new BlockPos(0, 0, 0);
            d.controllerBlocks = tm.getControllerBlocks();
            d.upgrades = tm.getCompatibleUpgrades();
            d.generateBoundingBox(); // For the relocation method
            d.relocateBlocks(); // Relocate blocks
            d.generateBoundingBox(); // Regenerate for the blockTypeMap generator
            d.generateBlockTypeMap(); // Generate the blockTypeMap
            timeMachineData.add(d);
        }
        Collections.sort(timeMachineData);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        this.panel = new DocsPanel(Minecraft.getInstance(), this.width, this.height, 0, 0);
        this.children.add(0, this.panel);
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        if (this.panel != null) {
            this.panel.render(p_render_1_, p_render_2_, p_render_3_);
        }
        super.render(p_render_1_, p_render_2_, p_render_3_);
    }

    private class TimeMachineData implements Comparable<TimeMachineData> {

        public TranslationTextComponent name;
        public TranslationTextComponent description;
        public int tier;
        public int cooldown; // in seconds
        public BlockPos[] basicBlocksPos;
        public BlockState[] basicBlocks;
        public BlockPos[] coreBlocksPos;
        public BlockState[] coreBlocks;
        public BlockPos controllerBlockPos;
        public BlockState[] controllerBlocks;
        public TimeMachineUpgrade[] upgrades;

        public TimeMachine.TMComponentType[][][] blockTypeMap;
        public AxisAlignedBB boundingBox;

        public void relocateBlocks() {
            BlockPos translocation = new BlockPos(0 - boundingBox.minX, 0 - boundingBox.minY, 0 - boundingBox.minZ);
            for (int i = 0; i < basicBlocksPos.length; i++) basicBlocksPos[i] = basicBlocksPos[i].add(translocation);
            for (int i = 0; i < coreBlocksPos.length; i++) coreBlocksPos[i] = coreBlocksPos[i].add(translocation);
            controllerBlockPos = controllerBlockPos.add(translocation);
        }

        public void generateBlockTypeMap() {
            blockTypeMap = new TimeMachine.TMComponentType[(int)boundingBox.maxY+1][(int)boundingBox.maxX+1][(int)boundingBox.maxZ+1];
            for(int y = 0; y <= boundingBox.maxY; y++)
                for(int x = 0; x <= boundingBox.maxX; x++)
                    nextPos: for(int z = 0; z <= boundingBox.maxZ; z++) {
                        for(BlockPos pos: basicBlocksPos) if (pos.equals(new BlockPos(x, y, z))) {
                            blockTypeMap[y][x][z] = TimeMachine.TMComponentType.BASIC; continue nextPos;
                        }
                        for(BlockPos pos: coreBlocksPos) if (pos.equals(new BlockPos(x, y, z))) {
                            blockTypeMap[y][x][z] = TimeMachine.TMComponentType.CORE; continue nextPos;
                        }
                        if (controllerBlockPos.equals(new BlockPos(x, y, z))) {
                            blockTypeMap[y][x][z] = TimeMachine.TMComponentType.CONTROLPANEL; continue nextPos;
                        } else blockTypeMap[y][x][z] = null;
                    }
        }

        public void generateBoundingBox() {
            int minX = 100, minY = 100, minZ = 100;
            int maxX = -100, maxY = -100, maxZ = -100;

            for(BlockPos pos: basicBlocksPos) {
                if (pos.getX() < minX) minX = pos.getX(); else if (pos.getX() > maxX) maxX = pos.getX();
                if (pos.getY() < minY) minY = pos.getY(); else if (pos.getY() > maxY) maxY = pos.getY();
                if (pos.getZ() < minZ) minZ = pos.getZ(); else if (pos.getZ() > maxZ) maxZ = pos.getZ();
            }
            for(BlockPos pos: coreBlocksPos) {
                if (pos.getX() < minX) minX = pos.getX(); else if (pos.getX() > maxX) maxX = pos.getX();
                if (pos.getY() < minY) minY = pos.getY(); else if (pos.getY() > maxY) maxY = pos.getY();
                if (pos.getZ() < minZ) minZ = pos.getZ(); else if (pos.getZ() > maxZ) maxZ = pos.getZ();
            }
            if (controllerBlockPos.getX() < minX) minX = controllerBlockPos.getX(); else if (controllerBlockPos.getX() > maxX) maxX = controllerBlockPos.getX();
            if (controllerBlockPos.getY() < minY) minY = controllerBlockPos.getY(); else if (controllerBlockPos.getY() > maxY) maxY = controllerBlockPos.getY();
            if (controllerBlockPos.getZ() < minZ) minZ = controllerBlockPos.getZ(); else if (controllerBlockPos.getZ() > maxZ) maxZ = controllerBlockPos.getZ();

            boundingBox = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
        }

        @Override
        public int compareTo(TimeMachineData o) {
            return Integer.compare(this.tier, o.tier);
        }
    }

    class DocsPanel extends ScrollPanel {

        private int contentHeight = 0;

        public DocsPanel(Minecraft client, int width, int height, int top, int left) {
            super(client, width, height, top, left);
        }

        @Override
        protected int getContentHeight() {
            return Math.max(contentHeight-8, height); // TODO: When panel is finished, this should calculate the height by itself
        }

        @Override
        protected void drawPanel(int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY) {
            int padding = 4;
            int right = this.left + this.width - 6;
            right -= 2;
            relativeY += padding;
            relativeY += drawCenteredString(ModItems.engineerBook.getName().getUnformattedComponentText(), width / 2, relativeY, 0xFFD900);
            relativeY += 2;
            relativeY += drawSplitString(
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed consectetur purus ac tellus ultrices tempor. Interdum et malesuada fames ac ante ipsum primis in faucibus. Duis quis interdum erat. Curabitur vel ultrices velit. In vitae dolor lorem. Phasellus efficitur diam eros, vel efficitur leo pulvinar eget. Integer posuere eros ante, ut vestibulum metus pharetra vitae. Aenean rhoncus sem ut sapien mattis, non volutpat nisl sodales. Ut quis ipsum eu massa placerat maximus. Ut ornare eros nec velit mattis congue. Mauris volutpat maximus purus, vel malesuada nulla pharetra eget.",
                    left + padding, relativeY, (right - padding) - left, 0xFFFFFF);
            relativeY += 8;
            relativeY += drawCenteredString("Time Machines", width / 2, relativeY, 0xFFD900);
            relativeY += 2;
            for(TimeMachineData data: timeMachineData) {
                int tier;
                if (data.name.getKey().equals(ModTimeMachines.timeMachineCreative.getName().getKey())) {
                    tier = data.tier - 1;
                } else {
                    tier = data.tier;
                }
                relativeY += drawString(data.name.setStyle(new Style().setBold(true)).getFormattedText(), left + padding, relativeY, 0xFFFFFF);
                relativeY += drawString("Max tier: "+tier+" | Cooldown time: "+data.cooldown+" seconds", left + padding, relativeY, 0xC98300);
                relativeY += 2;
                relativeY += drawSplitString(data.description.getFormattedText(), left + padding, relativeY, (right - padding) - left, 0xFFFFFF);
                if (data.upgrades != null && data.upgrades.length != 0) {
                    relativeY += 2;
                    relativeY += drawString(new StringTextComponent("Compatible upgrades").setStyle(new Style().setUnderlined(true)).getFormattedText(), left + padding, relativeY, 0xFFFFFF);
                    for (TimeMachineUpgrade upgrade : data.upgrades) {
                        relativeY += 2;
                        relativeY += drawString(upgrade.getName().getFormattedText(), left + padding, relativeY,0xFFFFFF);
                    }
                }
                relativeY += 8;
            }

            contentHeight = relativeY;
        }

        private int drawCenteredString(String text, int x, int y, int color) {
            super.drawCenteredString(font, text, x, y, color);
            return font.FONT_HEIGHT;
        }

        private int drawSplitString(String text, int x, int y, int width, int color) {
            font.drawSplitString(text, x, y, width, color);
            return font.listFormattedStringToWidth(text, width).size() * 9;
        }

        public int drawString(String text, int x, int y, int color) {
            drawString(font, text, x, y, color);
            return font.FONT_HEIGHT;
        }
    }
}