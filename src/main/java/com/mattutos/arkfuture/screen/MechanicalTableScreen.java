package com.mattutos.arkfuture.screen;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.menu.MechanicalTableMenu;
import com.mattutos.arkfuture.screen.util.MouseUtils;
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

    private static final ResourceLocation ENERGY_INCREASING_BAR =
            ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, "textures/gui/mechanical_table/mechanical_table_charged.png");


    public MechanicalTableScreen(MechanicalTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.titleLabelX = 72;
        this.titleLabelY = 7;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        pGuiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(pGuiGraphics);
        renderEnergyIncreasingBar(pGuiGraphics);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics) {
        if (menu.isCrafting()) {
            int scaledArrowProgress = (int) (menu.getScaledArrowProgress() * 18);
            guiGraphics.blit(CRAFTING_PROGRESS_BAR, leftPos + 113, topPos + 40, 0, 0, scaledArrowProgress, 6, 18, 6);
        }
    }

    private void renderEnergyIncreasingBar(GuiGraphics guiGraphics) {
        if (menu.isEnergyIncreasing()) {
            int energyHeight = (int) (menu.getScaledEnergyStoredProgress() * 14);
            guiGraphics.blit(ENERGY_INCREASING_BAR, leftPos + 72, topPos + 36 + (14 - energyHeight),
                    0, 0, 5, energyHeight, 5, 14);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        renderEnergyTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderEnergyTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {

        // Define energy bar position and size (based on renderEnergyIncreasingBar)
        int barX = leftPos + 72;
        int barY = topPos + 36;
        int barWidth = 5;
        int barHeight = 14;

        if (MouseUtils.isMouseOver(mouseX, mouseY, barX, barY, barWidth, barHeight)) {
            long stored = menu.getStoredEnergy();
            long max = menu.getMaxEnergy();
            guiGraphics.renderTooltip(this.font, Component.literal(stored + "/" + max + " FE"), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, "Tier 1", 137, 5, 0xffffff); // x=8, y=6 is typical top-left padding
    }
}
