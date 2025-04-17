package com.mattutos.arkfuture.block.entity;

import com.mattutos.arkfuture.block.entity.util.AFBaseContainerBlockEntity;
import com.mattutos.arkfuture.block.entity.util.AFEnergyContainerBlockEntity;
import com.mattutos.arkfuture.core.energy.AFEnergyStorage;
import com.mattutos.arkfuture.crafting.recipe.mechanicalentityassembler.MechanicalEntityAssemblerRecipe;
import com.mattutos.arkfuture.init.BlockEntityInit;
import com.mattutos.arkfuture.init.BlockInit;
import com.mattutos.arkfuture.init.MobAssemblyRecipesInit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MechanicalAssemblerBlockEntity extends AFEnergyContainerBlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final ItemStackHandler itemStackHandler;
    private final AFEnergyStorage energyStorage;

    @Getter
    private boolean isAssembling = false;
    private int progress = 0;
    private int maxProgress = 0;

    public MechanicalAssemblerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.MECHANICAL_ASSEMBLER.get(), pPos, pBlockState);

        itemStackHandler = createItemStackHandlerSingle();
        energyStorage = createEnergyStorage(10_000);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> animationState) {
        if (this.isAssembling) {
            animationState.getController().setAnimation(
                    RawAnimation.begin().then("animation.mechanical_assembler.powered", Animation.LoopType.LOOP)
            );
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected AFEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.ark_future.mechanical_assembler");
    }

    @Override
    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.isAssembling = pTag.getBoolean("isAssembling");
        this.progress = pTag.getInt("progress");
        this.maxProgress = pTag.getInt("maxProgress");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putBoolean("isAssembling", this.isAssembling);
        pTag.putInt("progress", this.progress);
        pTag.putInt("maxProgress", this.maxProgress);
    }

    @Override
    public void tickServer() {
        if (isAssembling) {
            BlockPattern pattern = getMechanicalAssemblyPattern();
            BlockPattern.BlockPatternMatch blockPatternMatch = pattern.find(this.level, this.worldPosition);

            if (blockPatternMatch == null) {
                resetAssembly();
                log.warn("Mechanical Assembler Block Entity did not find pattern");
                return;
            }

            List<AssemblerPartBlockEntity> partEntities = getPartEntities(blockPatternMatch);
            List<ItemStack> itemStackList = partEntities.stream()
                    .map(p -> p.getItem(0))
                    .toList();

            MechanicalEntityAssemblerRecipe entityAssemblerRecipe = getMatching(itemStackList);

            if (entityAssemblerRecipe == null) {
                resetAssembly();
                log.warn("Mechanical Assembler Block Entity did not find recipe");
                return;
            }

            if (energyStorage.extractEnergy(entityAssemblerRecipe.energyExpenditurePerTick(), true) != entityAssemblerRecipe.energyExpenditurePerTick()) {
                log.warn("Mechanical Assembler Block Entity did not have enough energy");
                return;
            }

            energyStorage.extractEnergy(entityAssemblerRecipe.energyExpenditurePerTick(), false);
            this.progress++;
            log.info("Energy: {}", energyStorage.getEnergyStored());

            if (isFinished()) {
                resetAssembly();
                craftEntity(entityAssemblerRecipe, partEntities);
                setChanged();
            }
        }
    }

    private void resetAssembly() {
        this.isAssembling = false;
        this.progress = 0;
        this.maxProgress = 0;
    }

    private MechanicalEntityAssemblerRecipe getMatching(List<ItemStack> itemStackList) {
        return MobAssemblyRecipesInit.findMatching(this.getItem(0), itemStackList);
    }

    private void craftEntity(MechanicalEntityAssemblerRecipe entityAssemblerRecipe, List<AssemblerPartBlockEntity> partEntities) {
        Entity entity = entityAssemblerRecipe.entityType().create(this.level);

        if (entity != null) {
            // Define a posição para spawnar (por exemplo, acima da block entity)
            BlockPos spawnPos = this.worldPosition.above();
            entity.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 0.0F, 0.0F);

            partEntities.forEach(p -> {
                if (this.level.getBlockEntity(p.getBlockPos()) instanceof AFBaseContainerBlockEntity assemblerPartBlockEntity) {
                    assemblerPartBlockEntity.clearContent();
                }
            });
            this.clearContent();

            // Adiciona a entidade no mundo
            this.level.addFreshEntity(entity);
        }
    }

    private boolean isFinished() {
        if (this.progress >= this.maxProgress) {
            this.progress = 0;
            return true;
        }
        return false;
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return null;
    }

    private BlockPattern getMechanicalAssemblyPattern() {
        return BlockPatternBuilder.start()
                .aisle(" M ")
                .aisle("PPP")
                .aisle(" P ")
                .aisle(" P ")
                .where('P', BlockInWorld.hasState(blockInWorld -> blockInWorld.getBlock().equals(BlockInit.ASSEMBLER_PART.get())))
                .where('M', BlockInWorld.hasState(blockInWorld -> blockInWorld.getBlock().equals(BlockInit.MECHANICAL_ASSEMBLER.get())))
                .build();
    }

    public void tryExecuteProcess() {
        BlockPattern pattern = getMechanicalAssemblyPattern();
        BlockPattern.BlockPatternMatch blockPatternMatch = pattern.find(this.level, this.worldPosition);

        if (blockPatternMatch != null) {
            log.info("Mechanical Assembler Block Entity found pattern");

            List<AssemblerPartBlockEntity> partEntities = getPartEntities(blockPatternMatch);
            List<ItemStack> itemStackList = partEntities.stream()
                    .map(p -> p.getItem(0))
                    .toList();

            MechanicalEntityAssemblerRecipe entityAssemblerRecipe = getMatching(itemStackList);
            if (entityAssemblerRecipe != null) {
                this.isAssembling = true;
                this.maxProgress = entityAssemblerRecipe.totalTicksPerCraft();
                this.progress = 0;
            } else {
                log.warn("Mechanical Assembler Block Entity did not find recipe");
            }
        } else {
            log.warn("Mechanical Assembler Block Entity did not find pattern");
        }
    }

    private List<AssemblerPartBlockEntity> getPartEntities(BlockPattern.BlockPatternMatch match) {
        List<AssemblerPartBlockEntity> parts = new ArrayList<>();

        for (int x = 0; x < match.getWidth(); x++) {
            for (int y = 0; y < match.getHeight(); y++) {
                for (int z = 0; z < match.getDepth(); z++) {
                    BlockInWorld blockInWorld = match.getBlock(x, y, z);
                    if (blockInWorld.getState().getBlock() == BlockInit.ASSEMBLER_PART.get()) {
                        BlockPos pos = blockInWorld.getPos();
                        if (blockInWorld.getLevel().getBlockEntity(pos) instanceof AssemblerPartBlockEntity part) {
                            parts.add(part);
                            part.setEntityCore(this);
                        }
                    }
                }
            }
        }

        return parts;
    }
}
