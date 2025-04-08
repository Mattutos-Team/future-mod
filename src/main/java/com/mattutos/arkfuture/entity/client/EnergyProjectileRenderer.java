package com.mattutos.arkfuture.entity.client;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.entity.EnergyProjectileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class EnergyProjectileRenderer extends EntityRenderer<EnergyProjectileEntity> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, "energy_projectile");

    public EnergyProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(EnergyProjectileEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBufferSource, pPackedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EnergyProjectileEntity pEntity) {
        return TEXTURE;
    }
}
