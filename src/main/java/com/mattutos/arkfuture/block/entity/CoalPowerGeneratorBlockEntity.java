package com.mattutos.arkfuture.block.entity;

import com.mattutos.arkfuture.block.entity.util.CustomBaseContainerBlockEntity;
import com.mattutos.arkfuture.init.BlockEntityInit;
import com.mattutos.arkfuture.screen.CoalPowerGeneratorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoalPowerGeneratorBlockEntity extends CustomBaseContainerBlockEntity {

    public enum SLOT {
        FUEL, ENERGY_IN, ENERGY_OUT;

        static public int count() {
            return SLOT.values().length;
        }
    }

    public enum NBT {
        REMAINING_BURN_TIME,
        ENERGY,
        INVENTORY;

        public final String key = "coal_power_generator." + this.name().toLowerCase();
    }

    public enum DATA {
        REMAINING_BURN_TIME,
        GENERATING,
        CAN_GENERATE,
        MAX_TRANSFER,
        CAPACITY,
        ENERGY_STORED;

        static public DATA getByIndex(int index) {
            return index < 0 || index >= DATA.values().length ? DATA.values()[0] : DATA.values()[index];
        }

        static public int count() {
            return SLOT.values().length;
        }
    }

    public static final int GENERATE = 50;
    public static final int MAX_TRANSFER = 1_000;
    public static final int CAPACITY = 100_000;

    private ItemStackHandler itemStackHandler = createItemStackHandler();
    private LazyOptional<IItemHandler> lazyInventoryHandler = LazyOptional.of(() -> itemStackHandler);

    private final EnergyStorage energyStorage = createEnergyStorage();
    private final LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> energyStorage);

    private int remainingBurnTime = 0;

    protected final ContainerData containerData;

    private @NotNull ItemStackHandler createItemStackHandler() {
        return new ItemStackHandler(SLOT.values().length) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if (!level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }
        };
    }

    private @NotNull EnergyStorage createEnergyStorage() {
        return new EnergyStorage(CAPACITY, MAX_TRANSFER, MAX_TRANSFER, 0);
    }

    public CoalPowerGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.COAL_POWER_GENERATOR.get(), pPos, pBlockState);

        containerData = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (DATA.getByIndex(pIndex)) {
                    case REMAINING_BURN_TIME -> remainingBurnTime;
                    case GENERATING -> remainingBurnTime > 0 ? GENERATE : 0;
                    case CAN_GENERATE -> GENERATE;
                    case MAX_TRANSFER -> MAX_TRANSFER;
                    case CAPACITY -> CAPACITY;
                    case ENERGY_STORED -> energyStorage.getMaxEnergyStored();
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
            }

            @Override
            public int getCount() {
                return DATA.count();
            }
        };
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new CoalPowerGeneratorMenu(pContainerId, pInventory, this, this.containerData);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inv.setItem(i, itemStackHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    // metodo usado para salvar informações NBT do bloco

    /**
     * Salva os dados da BlockEntity em um CompoundTag quando o jogo salva o mundo.
     * 🔹 Quando é chamado?
     * Quando o mundo é salvo (autosave ou ao sair do jogo).
     * Quando o bloco é exportado para um Structure Block.
     * <p>
     * 🔹 Para que serve?
     * Salva os dados personalizados da BlockEntity para que eles sejam restaurados ao carregar o mundo novamente.
     */
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put(NBT.INVENTORY.key, itemStackHandler.serializeNBT(pRegistries));
        pTag.put(NBT.ENERGY.key, energyStorage.serializeNBT(pRegistries));
        pTag.putInt(NBT.REMAINING_BURN_TIME.key, remainingBurnTime);
    }

    // metodo usado para carregar(quando o bloco for instanciado/renderizado) as informações do NBT do bloco

    /**
     * Carrega os dados da BlockEntity a partir do CompoundTag quando o jogo carrega o mundo.
     * 🔹 Quando é chamado?
     * Quando o mundo é carregado.
     * Quando o bloco é colocado a partir de um Structure Block.
     * <p>
     * 🔹 Para que serve?
     * Recupera os dados salvos e restaura o estado da BlockEntity.
     */
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        itemStackHandler.deserializeNBT(pRegistries, pTag.getCompound(NBT.INVENTORY.key));

        // TODO: precisa criar a implementação
//        energyStorage.deserializeNBT(pRegistries, pTag.getCompound(NBT.ENERGY.key));
        remainingBurnTime = pTag.getInt(NBT.REMAINING_BURN_TIME.key);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyInventoryHandler = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyInventoryHandler.invalidate();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.ark_future.coal_power_generator");
    }

    @Override
    protected ItemStackHandler getItems() {
        return itemStackHandler;
    }

    @Override
    protected void setItems(ItemStackHandler pItems) {
        this.itemStackHandler = pItems;
    }

    @Override
    public int getContainerSize() {
        return itemStackHandler.getSlots();
    }

    public void tickServer() {
        generateEnergy();
        distributeEnergy();
    }

    private boolean canGenerate() {
        ItemStack fuel = itemStackHandler.getStackInSlot(SLOT.FUEL.ordinal());
        return !fuel.isEmpty() && remainingBurnTime > 0;
    }

    private void generateEnergy() {
        if (remainingBurnTime > 0) {
            remainingBurnTime--;
            energyStorage.receiveEnergy(GENERATE, false);
        } else {
            ItemStack fuel = itemStackHandler.getStackInSlot(SLOT.FUEL.ordinal());
            if (!fuel.isEmpty()) {
                remainingBurnTime = ForgeHooks.getBurnTime(fuel, null);
                fuel.shrink(1);
            }
        }
    }

    private void distributeEnergy() {
    }

    // metodo usado para sincronização

    /**
     * Define os dados que serão enviados para o cliente quando um chunk contendo o BlockEntity for carregado.
     * 🔹 Quando é chamado?
     * Quando o chunk que contém o BlockEntity é enviado para o cliente (exemplo: jogador se aproxima do bloco).
     * Ele envia um NBT inicial para sincronizar o estado do bloco com o cliente.
     * <p>
     * 🔹 Para que serve?
     * Mantém a sincronização inicial do estado do bloco ao carregar chunks.
     * Se o bloco tiver informações personalizadas (energia, inventário, etc.), elas são enviadas para o cliente.
     */
    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // metodo usado para sincronização

    /**
     * Controla a atualização do BlockEntity quando o seu estado muda dinamicamente sem precisar recarregar o chunk.
     * 🔹 Quando é chamado?
     * Quando algo muda no BlockEntity (por exemplo, um contador interno, carga de energia, temperatura de uma máquina, etc.).
     * Se o código chamar level.sendBlockUpdated(), ele força a atualização para os clientes.
     * <p>
     * 🔹 Para que serve?
     * Sincroniza mudanças no bloco sem precisar descarregar e recarregar o chunk.
     * Mantém o cliente atualizado sem causar lag, pois só envia dados necessários.
     * 🔹 Exemplo de uso: Se o BlockEntity gerencia um valor de energia, você pode chamar esse método para atualizar o cliente sempre que a energia mudar.
     */
    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyInventoryHandler.cast();
        } else if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }
}
