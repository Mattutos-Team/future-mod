package com.mattutos.arkfuture.item.common;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

public class SingleItemTooltipComponent implements ClientTooltipComponent {

    private final ItemStack itemStack;
    private final String text;

    public SingleItemTooltipComponent(ItemStack itemStack, String text) {
        this.itemStack = itemStack;
        this.text = text;
    }

    @Override
    public int getHeight() {
        return 20; // Altura total da tooltip
    }

    @Override
    public int getWidth(Font font) {
        return 20; // Largura para renderizar o ícone
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        guiGraphics.renderItem(itemStack, x, y); // Renderiza o ícone do item
        guiGraphics.renderItemDecorations(font, itemStack, x, y); // Mostra quantidade ou efeitos
    }

    @Override
    public void renderText(Font pFont, int pMouseX, int pMouseY, Matrix4f pMatrix, MultiBufferSource.BufferSource pBufferSource) {
        if (this.text == null) return;

        pFont.drawInBatch(text, pMouseX + 20, pMouseY + 6, 0xFFFFFF, false, pMatrix, pBufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
    }
}
