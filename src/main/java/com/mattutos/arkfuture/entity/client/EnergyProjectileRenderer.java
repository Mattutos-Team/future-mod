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
//        pPoseStack.pushPose();
//        pPoseStack.scale(0.5f, 0.5f, 0.5f); // Tamanho do quad
//
//        VertexConsumer vertex = pBufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
//        Matrix4f matrix = pPoseStack.last().pose();
//
//        // Um quad plano com textura (exemplo simplificado)
//        vertex.addVertex(matrix, -0.5f, -0.25f, 0).setColor(255, 255, 255, 255).setUv(0, 0).setUv2(pPackedLight, pPackedLight);
//        vertex.addVertex(matrix, 0.5f, -0.25f, 0).setColor(255, 255, 255, 255).setUv(1, 0).setUv2(pPackedLight, pPackedLight);
//        vertex.addVertex(matrix, 0.5f, 0.25f, 0).setColor(255, 255, 255, 255).setUv(1, 1).setUv2(pPackedLight, pPackedLight);
//        vertex.addVertex(matrix, -0.5f, 0.25f, 0).setColor(255, 255, 255, 255).setUv(0, 1).setUv2(pPackedLight, pPackedLight);
//
//        pPoseStack.popPose();

        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBufferSource, pPackedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EnergyProjectileEntity pEntity) {
        return TEXTURE;
    }
}
