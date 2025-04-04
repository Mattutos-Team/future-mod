package com.mattutos.arkfuture.menu;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.block.entity.MechanicalTableBlockEntity;
import com.mattutos.arkfuture.crafting.recipe.mechanicaltable.MechanicalTableRecipe;
import com.mattutos.arkfuture.crafting.recipe.common.IngredientStack;
import com.mattutos.arkfuture.init.BlockInit;
import com.mattutos.arkfuture.init.MenuInit;
import com.mattutos.arkfuture.menu.common.BaseSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class MechanicalTableMenu extends AbstractContainerMenu {

    private static final Logger log = LoggerFactory.getLogger(MechanicalTableMenu.class);
    public final MechanicalTableBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public MechanicalTableMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public MechanicalTableMenu(int pContainerId, Inventory inv, BlockEntity blockEntity, ContainerData data) {
        super(MenuInit.MECHANICAL_TABLE_MENU.get(), pContainerId);
        this.blockEntity = ((MechanicalTableBlockEntity) blockEntity);
        this.level = inv.player.level();
        this.data = data;

        //TODO - CREATE A LOGIC TO GET RECIPES BY A SPECIFIC FOLDER
        List<String> recipePaths = List.of("ancient_obsidian", "ancient_iron");
        List<MechanicalTableRecipe> recipes = getMultipleRecipes(recipePaths);
        List<IngredientStack.Item> validBaseIngridientsList = getValidBaseIngredients(recipes);

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        //BASE ITEM
        this.addSlot(new BaseSlot(this.blockEntity.itemHandler, 1, 29, 35, validBaseIngridientsList));

        //INGREDIENTS ITEMS
        this.addSlot(new SlotItemHandler(this.blockEntity.itemHandler, 0, 11, 35)); // LEFT
        this.addSlot(new SlotItemHandler(this.blockEntity.itemHandler, 2, 47, 35)); // RIGHT
        this.addSlot(new SlotItemHandler(this.blockEntity.itemHandler, 3, 29, 17)); // UP
        this.addSlot(new SlotItemHandler(this.blockEntity.itemHandler, 4, 29, 53)); // BOTTOM

        //RESULT SLOT
        this.addSlot(new SlotItemHandler(this.blockEntity.itemHandler, 5, 152, 35));

        addDataSlots(data);
    }


    //CHECK WHETHER IS CRAFTING OR NOT
    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public boolean isEnergyIncreasing() {
        log.info("isEnergyIncreasing: {}", this.data.get(1));
        return false;
    }

    public int getScaledArrowProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowPixelSize = 18;

        return maxProgress != 0 && progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
    }


    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 6;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }


    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, BlockInit.MECHANICAL_TABLE.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }


    private List<MechanicalTableRecipe> getMultipleRecipes(List<String> paths) {
        RecipeManager recipeManager = level.getRecipeManager();
        List<MechanicalTableRecipe> validRecipes = new ArrayList<>();

        for (String path : paths) {
            ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, path);

            log.info("Processing recipe ID: {}", recipeId);

            RecipeHolder<?> recipeHolder = recipeManager.byKey(recipeId).orElse(null);

            if (recipeHolder == null) {
                log.warn("No recipe found for ID: {}", recipeId);
            }

            if (recipeHolder != null && recipeHolder.value() instanceof MechanicalTableRecipe) {
                validRecipes.add((MechanicalTableRecipe) recipeHolder.value());
                log.info("Valid recipe found: {}", recipeId);
            }
        }
        log.info("Total valid recipes found: {}", validRecipes.size());

        return validRecipes;
    }


    private List<IngredientStack.Item> getValidBaseIngredients(List<MechanicalTableRecipe> recipes) {
        List<IngredientStack.Item> validBases = new ArrayList<>();

        for (MechanicalTableRecipe recipe : recipes) {
            IngredientStack.Item base = recipe.getBase();

            if (base != null && !base.isEmpty()) {
                validBases.add(base);
            }
        }

        return validBases;
    }
}
