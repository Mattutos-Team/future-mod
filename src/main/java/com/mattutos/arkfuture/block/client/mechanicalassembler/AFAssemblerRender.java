package com.mattutos.arkfuture.block.client.mechanicalassembler;

import com.mattutos.arkfuture.block.entity.util.AFBaseContainerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public abstract class AFAssemblerRender<T extends AFBaseContainerBlockEntity & GeoAnimatable> extends GeoBlockRenderer<T> implements BlockEntityRenderer<T> {

    public AFAssemblerRender(GeoModel<T> model) {
        super(model);
    }

    @Override
    public void render(T pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        super.render(pBlockEntity, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);
        ItemStack itemStack = pBlockEntity.getItem(0);

        if (!itemStack.isEmpty()) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

            pPoseStack.pushPose();
            pPoseStack.translate(0.5, 0.96, 0.5);
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
            pPoseStack.mulPose(Axis.XP.rotationDegrees(90));
            Level level = pBlockEntity.getLevel();
            itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, getLightLevel(level, pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, level, 1);
            pPoseStack.popPose();
        }
    }

    private int getLightLevel(Level pLvel, BlockPos pBlockPos) {
        int blockLight = pLvel.getBrightness(LightLayer.BLOCK, pBlockPos);
        int skyLight = pLvel.getBrightness(LightLayer.SKY, pBlockPos);
        return LightTexture.pack(blockLight, skyLight);
    }
}
