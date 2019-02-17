package tk.rdvdev2.TimeTravelMod.client.creativetab;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tk.rdvdev2.TimeTravelMod.ModItems;

public class tabTTM extends ItemGroup {
    public tabTTM(int id){
        super(id, "Time Travel Mod");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack createIcon(){
        return new ItemStack(ModItems.timeCrystal);
    }

    public String getTranslatedTabLabel() {
        return "Time Travel Mod";
    }
}
