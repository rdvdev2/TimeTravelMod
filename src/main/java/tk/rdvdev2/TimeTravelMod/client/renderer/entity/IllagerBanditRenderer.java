package tk.rdvdev2.TimeTravelMod.client.renderer.entity;

import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import tk.rdvdev2.TimeTravelMod.client.model.entity.IllagerBanditModel;
import tk.rdvdev2.TimeTravelMod.common.entity.IllagerBanditEntity;

import javax.annotation.Nullable;

import static tk.rdvdev2.TimeTravelMod.TimeTravelMod.MODID;

public class IllagerBanditRenderer extends BipedRenderer<IllagerBanditEntity, IllagerBanditModel> {
    public static final ResourceLocation texture = new ResourceLocation(MODID,"textures/entity/illager_bandit.png");

    public IllagerBanditRenderer(EntityRendererManager renderManager) {
        super(renderManager, new IllagerBanditModel(), 0.5f);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(IllagerBanditEntity entity) {
        return texture;
    }
}
