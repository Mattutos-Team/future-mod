package com.mattutos.arkfuture.item.client.energypistol;

import com.mattutos.arkfuture.item.EnergyPistolItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class EnergyPistolRender extends GeoItemRenderer<EnergyPistolItem> {
    public EnergyPistolRender() {
        super(new EnergyPistolModel());
    }

    @Override
    public void renderRecursively(PoseStack poseStack, EnergyPistolItem animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {

        if (bone.getName().equals("battery")) {
            boolean showBattery = animatable.shouldShowBattery();
            bone.setHidden(!showBattery);
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }
}
