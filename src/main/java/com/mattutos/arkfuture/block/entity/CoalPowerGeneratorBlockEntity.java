package com.mattutos.arkfuture.block.entity;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.block.CoalPowerGeneratorBlock;
import com.mattutos.arkfuture.block.entity.util.AFBaseContainerBlockEntity;
import com.mattutos.arkfuture.core.inventory.BaseData;
import com.mattutos.arkfuture.core.inventory.EnumContainerData;
import com.mattutos.arkfuture.init.BlockEntityInit;
import com.mattutos.arkfuture.menu.CoalPowerGeneratorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
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

public class CoalPowerGeneratorBlockEntity extends AFBaseContainerBlockEntity {

    public enum SLOT {
        FUEL, ENERGY_CHARGER, ENERGY_DISCHARGER;

        static public int count() {
            return SLOT.values().length;
        }
    }

    public enum NBT {
        REMAINING_BURN_TIME,
        TOTAL_BURN_TIME,
        ENERGY,
        INVENTORY;

        public final String key = "coal_power_generator." + this.name().toLowerCase();
    }

    public enum DATA implements BaseData {
        REMAINING_BURN_TIME,
        TOTAL_BURN_TIME,
        GENERATING,
        HOW_MUCH_CAN_GENERATE,
        MAX_TRANSFER,
        CAPACITY(4),
        ENERGY_STORED(4);

        private final int dataPackShort;

        DATA() {
            this.dataPackShort = 1;
        }

        DATA(int dataPackShort) {
            this.dataPackShort = dataPackShort;
        }

        @Override
        public int getDataPack() {
            return this.dataPackShort;
        }
    }

    public static final int GENERATE = 10;
    public static final int MAX_TRANSFER = 1_000;
    public static final int CAPACITY = 100_000;

    private ItemStackHandler itemStackHandler = createItemStackHandler();
    private LazyOptional<IItemHandler> lazyInventoryHandler = LazyOptional.of(() -> itemStackHandler);

    private final EnergyStorage energyStorage = createEnergyStorage();
    private final LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> energyStorage);

    private int remainingBurnTime = 0;
    private int totalBurnTime = 0;
    private int generating = 0;

    protected final EnumContainerData<DATA> containerData;

    private @NotNull ItemStackHandler createItemStackHandler() {
        return new ItemStackHandler(SLOT.values().length) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if (!level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
                }
            }
        };
    }

    private @NotNull EnergyStorage createEnergyStorage() {
        return new EnergyStorage(CAPACITY, MAX_TRANSFER, MAX_TRANSFER, 0);
    }

    public CoalPowerGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.COAL_POWER_GENERATOR.get(), pPos, pBlockState);

        containerData = new EnumContainerData<>(DATA.class) {
            @Override
            public void set(DATA enumData, long value) {
            }

            @Override
            public long get(DATA enumData) {
                return switch (enumData) {
                    case REMAINING_BURN_TIME -> CoalPowerGeneratorBlockEntity.this.remainingBurnTime;
                    case TOTAL_BURN_TIME -> CoalPowerGeneratorBlockEntity.this.totalBurnTime;
                    case GENERATING -> CoalPowerGeneratorBlockEntity.this.generating;
                    case HOW_MUCH_CAN_GENERATE -> GENERATE;
                    case MAX_TRANSFER -> MAX_TRANSFER;
                    case ENERGY_STORED -> CoalPowerGeneratorBlockEntity.this.energyStorage.getEnergyStored();
                    case CAPACITY -> CoalPowerGeneratorBlockEntity.this.energyStorage.getMaxEnergyStored();
                    default -> throw new IllegalStateException("Unexpected value: " + enumData);
                };
            }
        };

    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new CoalPowerGeneratorMenu(pContainerId, pInventory, this, this.containerData);
    }

    /**
     * Metodo usado para salvar informações NBT do bloco
     * Salva os dados da BlockEntity em um CompoundTag quando o jogo salva o mundo.
     * 🔹 Quando é chamado?
     * Quando o mundo é salvo (autosave ou ao sair do jogo).
     * Quando o bloco é exportado para um Structure Block.
     * <p>
     * 🔹 Para que serve?
     * Salva os dados personalizados da BlockEntity para que eles sejam restaurados ao carregar o mundo novamente.
     */
    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put(NBT.INVENTORY.key, itemStackHandler.serializeNBT(pRegistries));
        compoundTag.put(NBT.ENERGY.key, energyStorage.serializeNBT(pRegistries));
        compoundTag.putInt(NBT.REMAINING_BURN_TIME.key, remainingBurnTime);
        compoundTag.putInt(NBT.TOTAL_BURN_TIME.key, totalBurnTime);

        pTag.put(ArkFuture.MOD_ID, compoundTag);
    }

    /**
     * Metodo usado para carregar(quando o bloco for instanciado/renderizado) as informações do NBT do bloco
     * Carrega os dados da BlockEntity a partir do CompoundTag quando o jogo carrega o mundo.
     * 🔹 Quando é chamado?
     * Quando o mundo é carregado.
     * Quando o bloco é colocado a partir de um Structure Block.
     * <p>
     * 🔹 Para que serve?
     * Recupera os dados salvos e restaura o estado da BlockEntity.
     */
    @Override
    protected void loadAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        CompoundTag compoundTag = pTag.getCompound(ArkFuture.MOD_ID);
        itemStackHandler.deserializeNBT(pRegistries, compoundTag.getCompound(NBT.INVENTORY.key));
        energyStorage.deserializeNBT(pRegistries, compoundTag.get(NBT.ENERGY.key));
        remainingBurnTime = compoundTag.getInt(NBT.REMAINING_BURN_TIME.key);
        totalBurnTime = compoundTag.getInt(NBT.TOTAL_BURN_TIME.key);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyInventoryHandler = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.ark_future.coal_power_generator");
    }

    @Override
    public ItemStackHandler getItems() {
        return itemStackHandler;
    }

    @Override
    protected void setItems(ItemStackHandler pItems) {
        this.itemStackHandler = pItems;
    }

    @Override
    public void tickServer() {
        generateEnergy();
        distributeEnergy();
    }

    private void generateEnergy() {
        if (energyStorage.receiveEnergy(GENERATE, true) > 0) {

            // verifica slot de entrada de energia
            ItemStack stackInSlot = itemStackHandler.getStackInSlot(SLOT.ENERGY_DISCHARGER.ordinal());
            if (!stackInSlot.isEmpty()) {
                stackInSlot.getCapability(ForgeCapabilities.ENERGY)
                        .ifPresent(itemEnergyStorage -> {
                            if (itemEnergyStorage.canExtract()) {
                                int energyToReceive = itemEnergyStorage.extractEnergy(MAX_TRANSFER, true);
                                int received = energyStorage.receiveEnergy(energyToReceive, false);
                                itemEnergyStorage.extractEnergy(received, false);
                                CoalPowerGeneratorBlockEntity.this.setChanged();
                            }
                        });
            }

            // Queima de combustivel para gerar energia
            if (remainingBurnTime > 0) {
                if (energyStorage.receiveEnergy(GENERATE, true) != GENERATE) {
                    generating = 0;
                    return;
                }
                remainingBurnTime--;
                generating = energyStorage.receiveEnergy(GENERATE, false);
            } else {
                generating = 0;
                ItemStack fuel = itemStackHandler.getStackInSlot(SLOT.FUEL.ordinal());
                if (!fuel.isEmpty()) {
                    totalBurnTime = remainingBurnTime = ForgeHooks.getBurnTime(fuel, null);
                    fuel.shrink(1);
                }
            }
        }

        // este metodo cria somente um estado novo, nao atualiza o bloco atual.
        BlockState blockState = this.getBlockState().setValue(CoalPowerGeneratorBlock.POWERED, generating > 0);
        if (this.level != null) this.level.setBlock(this.worldPosition, blockState, Block.UPDATE_ALL); // seta o novo estado
        setChanged();
    }

    private void distributeEnergy() {
        if (energyStorage.getEnergyStored() <= 0) return;

        // checa se existe um item para adicionar energia no slot de entrada
        ItemStack stackOutSlot = itemStackHandler.getStackInSlot(SLOT.ENERGY_CHARGER.ordinal());
        if (!stackOutSlot.isEmpty()) {
            stackOutSlot.getCapability(ForgeCapabilities.ENERGY)
                    .ifPresent(itemEnergyStorage -> {
                        if (itemEnergyStorage.canReceive()) {
                            int energyToSend = energyStorage.extractEnergy(MAX_TRANSFER, true);
                            int received = itemEnergyStorage.receiveEnergy(energyToSend, false);
                            energyStorage.extractEnergy(received, false);
                            CoalPowerGeneratorBlockEntity.this.setChanged();
                        }
                    });
        }

        // checa todos os lados verifica se os blocos vizinhos podem receber energia
        for (Direction direction : Direction.values()) {
            if (this.level == null) break;
            BlockEntity blockEntity = level.getBlockEntity(this.worldPosition.relative(direction));
            if (blockEntity != null) {
                blockEntity.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite())
                        .ifPresent(iEnergyStorage -> {
                            if (iEnergyStorage.canReceive()) {
                                int energyToSend = Math.min(energyStorage.getEnergyStored(), MAX_TRANSFER);
                                int received = iEnergyStorage.receiveEnergy(energyToSend, false);
                                energyStorage.extractEnergy(received, false);
                                blockEntity.setChanged();
                                CoalPowerGeneratorBlockEntity.this.setChanged();
                            }
                        });
            }
        }
    }

    /**
     * Metodo usado para sincronização
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

    /**
     * Metodo usado para sincronização
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
