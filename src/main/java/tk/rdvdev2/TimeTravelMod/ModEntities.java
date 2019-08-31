package tk.rdvdev2.TimeTravelMod;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.client.renderer.entity.IllagerBanditRenderer;
import tk.rdvdev2.TimeTravelMod.common.entity.IllagerBanditEntity;

import static tk.rdvdev2.TimeTravelMod.TimeTravelMod.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final EntityType<IllagerBanditEntity> ILLAGER_BANDIT = ((EntityType<IllagerBanditEntity>)(EntityType.Builder.create(IllagerBanditEntity::new, EntityClassification.MONSTER).size(0.6F, 1.95F).build("illager_bandit")).setRegistryName(MODID, "illager_bandit"));

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll(
                ILLAGER_BANDIT
        );
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(IllagerBanditEntity.class, IllagerBanditRenderer::new);
    }
}
