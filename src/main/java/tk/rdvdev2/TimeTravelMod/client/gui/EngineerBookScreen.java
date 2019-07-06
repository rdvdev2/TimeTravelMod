package tk.rdvdev2.TimeTravelMod.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import tk.rdvdev2.TimeTravelMod.ModItems;
import tk.rdvdev2.TimeTravelMod.ModPacketHandler;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;
import tk.rdvdev2.TimeTravelMod.common.networking.SyncBookData;
import tk.rdvdev2.TimeTravelMod.common.timemachine.CreativeTimeMachine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class EngineerBookScreen extends Screen {

    private TimeMachineData[] timeMachineData;
    private CompoundNBT bookData;
    private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation("textures/gui/book.png");
    private HashMap<Integer, String> pages = new HashMap<Integer, String>();

    public EngineerBookScreen(Collection<TimeMachine> timeMachines) {
        super(new StringTextComponent("TITLE PLACEHOLDER"));

        timeMachineData = new TimeMachineData[timeMachines.size()];

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
                timeMachineData[i++] = d;
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
            timeMachineData[i++] = d;
        }

        // Generation of the page list                                           | Specification                |
        pages.put(0, "welcome"); //                                              | welcome -> Welcome page      |
        int indexpages = i / 5; // TODO: 5 is a placeholder                      |                              |
        int tmpages = ++indexpages + i; //                                       |                              |
        while (indexpages > 0) pages.put(indexpages, "index" + --indexpages); // | index_  -> Index pages       |
        while (i > 0) pages.put(tmpages--, "tm" + --i); //                       | tm_     -> Time machine page |
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void init() {
        Iterator<ItemStack> i = minecraft.player.getHeldEquipment().iterator();
        while(i.hasNext()) {
            ItemStack item = i.next();
            if (item.isItemEqual(new ItemStack(ModItems.engineerBook))) {
                bookData = item.getChildTag("data");
                break;
            }
        }

        if (!bookData.contains("page")) bookData.putInt("page", 0);
        String page = pages.get(bookData.getInt("page"));
        if (page.equals("welcome")) {

            // Draw welcome screen
            addButton(new TextWidget(width / 2, height / 2 - 192 + 2, "Time Machine Engineer's Book", true));

        } else if (page.startsWith("index")) {

            int n = Integer.parseInt(page.substring(5));
            // Draw index page

        } else if (page.startsWith("tm")) {

            int n = Integer.parseInt(page.substring(2));
            TimeMachineData data = timeMachineData[n];
            // Draw time machine page

        }
        super.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
        int i = (this.width - 192) / 2;
        int j = (this.height - 192) / 2;
        this.blit(i, j, 0, 0, 192, 192);
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        // Set data on client
        Minecraft.getInstance().deferTask(()->{
            ItemStack item = Minecraft.getInstance().player.inventory.getCurrentItem();
            int slot = Minecraft.getInstance().player.inventory.currentItem;
            if (item.isItemEqual(new ItemStack(ModItems.engineerBook))) {
                CompoundNBT tag = item.getTag();
                tag.put("data", this.bookData);
                item.setTag(tag);
                Minecraft.getInstance().player.inventory.setInventorySlotContents(slot, item);
            } else {
                TimeTravelMod.logger.warn("Client was unable to set Engineer Book data!");
            }
        });
        // Sync to server
        ModPacketHandler.CHANNEL.sendToServer(new SyncBookData(this.bookData));
    }

    private class TimeMachineData {

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
    }
}