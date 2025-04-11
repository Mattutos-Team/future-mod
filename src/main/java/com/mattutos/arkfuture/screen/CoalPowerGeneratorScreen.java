package com.mattutos.arkfuture.screen;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.menu.CoalPowerGeneratorMenu;
import com.mattutos.arkfuture.screen.util.MouseUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class CoalPowerGeneratorScreen extends AbstractContainerScreen<CoalPowerGeneratorMenu> {

    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ArkFuture.MOD_ID, "textures/gui/coal_power_generator/coal_power_generator_gui.png");

    private static final ResourceLocation FLAME_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ArkFuture.MOD_ID, "textures/gui/coal_power_generator/flame_gui.png");

    private static final int FLAME_WIDTH = 16;
    private static final int FLAME_HEIGHT = 22;

    private static final int ENERGY_X = 154;
    private static final int ENERGY_Y = 11;
    private static final int ENERGY_WIDTH = 12;
    private static final int ENERGY_HEIGHT = 62;

    public CoalPowerGeneratorScreen(CoalPowerGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        // Ajustando posição do título do inventário
        this.inventoryLabelY = this.imageHeight - 92;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        pGuiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        renderProgressFlame(pGuiGraphics);
        renderProgressEnergyStored(pGuiGraphics);
    }

    private void renderProgressEnergyStored(GuiGraphics pGuiGraphics) {
        pGuiGraphics.fill(leftPos + ENERGY_X, topPos + ENERGY_Y, leftPos + (ENERGY_X + ENERGY_WIDTH), topPos + (ENERGY_Y + ENERGY_HEIGHT), 0xff666666);
        if (menu.getEnergyStored() > 0 && menu.getMaxEnergyStored() != 0) {
            int percentEnergyHeight = (int) (menu.getEnergyStored() * ENERGY_HEIGHT / menu.getMaxEnergyStored());
            pGuiGraphics.fillGradient(leftPos + ENERGY_X, topPos + (ENERGY_Y + (ENERGY_HEIGHT - percentEnergyHeight)), leftPos + (ENERGY_X + ENERGY_WIDTH), topPos + (ENERGY_Y + ENERGY_HEIGHT), 0xffcc0000, 0xffaa0000);
        }
    }

    private void renderProgressFlame(GuiGraphics pGuiGraphics) {
        if (menu.isBurning()) {
            int scaledFlameProgressHeight = (int) (menu.getScaledFlameProgress() * FLAME_HEIGHT);
            int yFlameHeightProgress = FLAME_HEIGHT - scaledFlameProgressHeight;
            pGuiGraphics.blit(FLAME_TEXTURE, leftPos + 26, topPos + (22 + yFlameHeightProgress), 0, yFlameHeightProgress, FLAME_WIDTH, scaledFlameProgressHeight, FLAME_WIDTH, FLAME_HEIGHT);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        renderLabelsEnergy(pGuiGraphics);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        renderTooltipEnergy(pGuiGraphics, pMouseX, pMouseY);
    }

    private void renderLabelsEnergy(GuiGraphics pGuiGraphics) {
        int generating = menu.getGenerating();
        long energyStored = menu.getEnergyStored();

        int xTempGenerating = this.font.width(generating + "");
        int xTempEnergyStored = this.font.width(energyStored + "");

        pGuiGraphics.drawString(this.font, energyStored + " FE", (leftPos - xTempEnergyStored) + 98, topPos + 48, 0x404040, false);
        pGuiGraphics.drawString(this.font, generating + " FE/T", (leftPos - xTempGenerating) + 98, topPos + 58, 0x404040, false);
    }

    private void renderTooltipEnergy(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        // Renderiza o tooltip de energia se o mouse estiver em cima da barra de energia
        if (MouseUtils.isMouseOver(pMouseX, pMouseY, leftPos + ENERGY_X, topPos + ENERGY_Y, ENERGY_WIDTH, ENERGY_HEIGHT)) {
            long power = menu.getEnergyStored();
            pGuiGraphics.renderTooltip(this.font, Component.literal(power + " FE"), pMouseX, pMouseY);
        }
    }

}
