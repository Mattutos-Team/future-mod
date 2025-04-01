package com.mattutos.arkfuture.screen;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.menu.mechanicaltable.MechanicalTableMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MechanicalTableScreen extends AbstractContainerScreen<MechanicalTableMenu> {
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ArkFuture.MOD_ID, "textures/gui/mechanical_table/mechanical_table_menu_gui.png");

    private static final ResourceLocation CRAFTING_PROGRESS_BAR =
            ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, "textures/gui/mechanical_table/mechanical_table_crafting_bar.png");


    public MechanicalTableScreen(MechanicalTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.titleLabelX = 72;
        this.titleLabelY = 7;
    }

//    private boolean hasRecipeError() {
//        return this.menu.getSlot(0).hasItem() && this.menu.getSlot(1).hasItem()
//                && this.menu.getSlot(2).hasItem() && this.menu.getSlot(3).hasItem()
//                && this.menu.getSlot(4).hasItem() && !this.menu.getSlot(this.menu.getResultSlot()).hasItem();
//    }
//
//    protected void renderErrorIcon(@NotNull GuiGraphics pGuiGraphics, int pX, int pY) {
//        if (this.hasRecipeError()) {
//            pGuiGraphics.blit(GUI_TEXTURE, pX + 65, pY + 46, this.imageWidth, 0, 28, 21);
//        }
//    }


    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(pGuiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            guiGraphics.blit(CRAFTING_PROGRESS_BAR, x + 113, y + 40, 0, 0, menu.getScaledArrowProgress(), 6, 22, 6);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
