package com.mattutos.arkfuture.screen;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.menu.MechanicalTableMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MechanicalTableScreen extends AbstractContainerScreen<MechanicalTableMenu> {
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ArkFuture.MOD_ID, "textures/gui/mechanical_table_gui.png");

    private final CyclingSlotBackground baseIcon = new CyclingSlotBackground(1); //MID SLOT BASE ITEM
    private final CyclingSlotBackground additionalIcon1 = new CyclingSlotBackground(0);
    private final CyclingSlotBackground additionalIcon2 = new CyclingSlotBackground(2);
    private final CyclingSlotBackground additionalIcon3 = new CyclingSlotBackground(3);
    private final CyclingSlotBackground additionalIcon4 = new CyclingSlotBackground(4);

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

    private Optional<Item> getBaseItem() {
        ItemStack itemstack = this.menu.getSlot(1).getItem();
        if (!itemstack.isEmpty()) {
            Item item = itemstack.getItem();

            return Optional.of(item);
        }
        return Optional.empty();
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private static final Component MISSING_TEMPLATE_TOOLTIP = Component.translatable("container.upgrade.missing_template_tooltip");
    private static final Component ERROR_TOOLTIP = Component.translatable("container.upgrade.error_tooltip");

//    private void renderOnboardingTooltips(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
//        Optional<Component> optional = Optional.empty();
//        if (this.hasRecipeError() && this.isHovering(65, 46, 28, 21, pMouseX, pMouseY)) {
//            optional = Optional.of(ERROR_TOOLTIP);
//        }
//
//        if (this.hoveredSlot != null) {
//            ItemStack itemstack = this.menu.getSlot(0).getItem();
//            if (itemstack.isEmpty()) {
//                if (this.hoveredSlot.index == 0) {
//                    optional = Optional.of(MISSING_TEMPLATE_TOOLTIP);
//                }
//            }
//        }
//
//        optional.ifPresent((p_280863_) -> {
//            pGuiGraphics.renderTooltip(this.font, this.font.split(p_280863_, 115), pMouseX, pMouseY);
//        });
//    }

}
