package com.mattutos.future.client;

import com.mattutos.future.FutureMod;
import com.mattutos.future.blockentity.CoalEnergyGeneratorEntity;
import com.mattutos.future.menu.CoalGeneratorContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GeneratorScreen extends AbstractContainerScreen<CoalGeneratorContainerMenu> {

    private static final int ENERGY_LEFT = 96;
    private static final int ENERGY_WIDTH = 72;
    private static final int ENERGY_TOP = 20;
    private static final int ENERGY_HEIGHT = 10;

    private static final int ENERGY_COLOR_FROM  = 0xffff0000;
    private static final int ENERGY_COLOR_TO    = 0xff000000;
    private static final int ENERGY_COLOR_BG    = 0xff330000;

    private final ResourceLocation GUI = new ResourceLocation(FutureMod.MOD_ID, "textures/gui/coal_energy_generator.png");

    public GeneratorScreen(CoalGeneratorContainerMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.inventoryLabelY = this.imageHeight - 110;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(GUI, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        int power = menu.getPower();
        int p = (int) ((power / (float) CoalEnergyGeneratorEntity.CAPACITY) * ENERGY_WIDTH);
        graphics.fillGradient(leftPos + ENERGY_LEFT, topPos + ENERGY_TOP, leftPos + ENERGY_LEFT + p, topPos + ENERGY_TOP + ENERGY_HEIGHT, ENERGY_COLOR_FROM, ENERGY_COLOR_TO);
        graphics.fill(leftPos + ENERGY_LEFT + p, topPos + ENERGY_TOP, leftPos + ENERGY_LEFT + ENERGY_WIDTH, topPos + ENERGY_TOP + ENERGY_HEIGHT, ENERGY_COLOR_BG);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mousex, int mousey, float partialTick) {
        super.render(graphics, mousex, mousey, partialTick);
        // Render tooltip with power if in the energy box
        if (mousex >= leftPos + ENERGY_LEFT && mousex < leftPos + ENERGY_LEFT + ENERGY_WIDTH && mousey >= topPos + ENERGY_TOP && mousey < topPos + ENERGY_TOP + ENERGY_HEIGHT) {
            int power = menu.getPower();
            graphics.renderTooltip(this.font, Component.literal(power + " RF"), mousex, mousey);
        }
    }
}
