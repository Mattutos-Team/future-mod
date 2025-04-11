package com.mattutos.arkfuture.menu;

import com.mattutos.arkfuture.block.entity.MechanicalTableBlockEntity;
import com.mattutos.arkfuture.core.inventory.EnumContainerData;
import com.mattutos.arkfuture.core.inventory.SimpleEnumContainerData;
import com.mattutos.arkfuture.crafting.recipe.mechanicaltable.MechanicalTableRecipe;
import com.mattutos.arkfuture.init.BlockInit;
import com.mattutos.arkfuture.init.MenuInit;
import com.mattutos.arkfuture.init.recipe.ModRecipe;
import com.mattutos.arkfuture.menu.common.IngredientSlot;
import com.mattutos.arkfuture.menu.common.ResultSlot;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class MechanicalTableMenu extends AbstractContainerMenu {

    public final MechanicalTableBlockEntity blockEntity;
    private final Level level;
    private final EnumContainerData<MechanicalTableBlockEntity.DATA> containerData;

    public MechanicalTableMenu(int pContainerId, Inventory pInventory, FriendlyByteBuf pExtraData) {
        this(pContainerId, pInventory, pInventory.player.level().getBlockEntity(pExtraData.readBlockPos()), new SimpleEnumContainerData<>(MechanicalTableBlockEntity.DATA.class));
    }

    public MechanicalTableMenu(int pContainerId, Inventory pInventory, BlockEntity pBlockEntity, EnumContainerData<MechanicalTableBlockEntity.DATA> pContainerData) {
        super(MenuInit.MECHANICAL_TABLE_MENU.get(), pContainerId);
        this.blockEntity = ((MechanicalTableBlockEntity) pBlockEntity);
        this.level = pInventory.player.level();
        this.containerData = pContainerData;

        List<RecipeHolder<MechanicalTableRecipe>> allRecipesForMechanicalTable = this.level.getRecipeManager().getAllRecipesFor(ModRecipe.MECHANICAL_TABLE_TYPE.get());
        Set<Ingredient> validBaseIngridientsList = getValidBaseIngredients(allRecipesForMechanicalTable);
        Set<Ingredient> validPliersIngridientsList = getValidPliersIngredients(allRecipesForMechanicalTable);
        Set<Ingredient> validAdditionsIngredients = getValidAdditionsIngredients(allRecipesForMechanicalTable);

        addPlayerInventory(pInventory);
        addPlayerHotbar(pInventory);

        //BASE ITEM
        this.addSlot(new IngredientSlot(this.blockEntity.getItemStackHandler(), 0, 29, 35, validBaseIngridientsList));

        //MECHANICAL PLIERS
        this.addSlot(new IngredientSlot(this.blockEntity.getItemStackHandler(), 1, 84, 35, validPliersIngridientsList));

        //INGREDIENTS ITEMS
        this.addSlot(new IngredientSlot(this.blockEntity.getItemStackHandler(), 2, 11, 35, validAdditionsIngredients)); // LEFT
        this.addSlot(new IngredientSlot(this.blockEntity.getItemStackHandler(), 3, 47, 35, validAdditionsIngredients)); // RIGHT
        this.addSlot(new IngredientSlot(this.blockEntity.getItemStackHandler(), 4, 29, 17, validAdditionsIngredients)); // UP
        this.addSlot(new IngredientSlot(this.blockEntity.getItemStackHandler(), 5, 29, 53, validAdditionsIngredients)); // BOTTOM

        //RESULT SLOT
        this.addSlot(new ResultSlot(this.blockEntity.getItemStackHandler(), 6, 152, 35));

        addDataSlots(containerData);
    }

    //CHECK WHETHER IS CRAFTING OR NOT
    public boolean isCrafting() {
        return containerData.get(0) > 0;
    }

    public boolean isEnergyIncreasing() {
        long energy = containerData.get(MechanicalTableBlockEntity.DATA.ENERGY_STORED);
        return energy <= MechanicalTableBlockEntity.CAPACITY;
    }

    public int getScaledEnergyStoredProgress() {
        long progress = containerData.get(MechanicalTableBlockEntity.DATA.ENERGY_STORED);
        long maxProgress = containerData.get(MechanicalTableBlockEntity.DATA.MAX_ENERGY_CAPACITY);
        int arrowPixelSize = 14;

        return Math.toIntExact(maxProgress != 0 && progress != 0 ? progress * arrowPixelSize / maxProgress : 0);
    }

    public int getScaledArrowProgress() {
        int progress = this.containerData.get(0);
        int maxProgress = this.containerData.get(1);
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
    private static final int TE_INVENTORY_SLOT_COUNT = 7;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, (TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT - 1), false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX, false)) {
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

    private Set<Ingredient> getValidBaseIngredients(List<RecipeHolder<MechanicalTableRecipe>> recipes) {
        return recipes.stream()
                .map(RecipeHolder::value)
                .map(MechanicalTableRecipe::base)
                .filter(base -> base != null && !base.isEmpty())
                .collect(Collectors.toSet());
    }

    private Set<Ingredient> getValidAdditionsIngredients(List<RecipeHolder<MechanicalTableRecipe>> recipes) {
        return recipes.stream()
                .map(RecipeHolder::value)
                .map(MechanicalTableRecipe::additions)
                .filter(addition -> addition != null && !addition.isEmpty())
                .collect(Collectors.toSet());
    }

    private Set<Ingredient> getValidPliersIngredients(List<RecipeHolder<MechanicalTableRecipe>> recipes) {
        return recipes.stream()
                .map(RecipeHolder::value)
                .map(MechanicalTableRecipe::pliers)
                .filter(pliers -> pliers != null && !pliers.isEmpty())
                .collect(Collectors.toSet());
    }

    public long getStoredEnergy() {
        return containerData.get(MechanicalTableBlockEntity.DATA.ENERGY_STORED);
    }

    public long getMaxEnergy() {
        return containerData.get(MechanicalTableBlockEntity.DATA.MAX_ENERGY_CAPACITY);
    }
}
