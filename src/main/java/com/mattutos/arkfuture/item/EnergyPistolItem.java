package com.mattutos.arkfuture.item;

import com.mattutos.arkfuture.entity.EnergyProjectileEntity;
import com.mattutos.arkfuture.init.DataComponentTypesInit;
import com.mattutos.arkfuture.item.client.energypistol.EnergyPistolRender;
import com.mattutos.arkfuture.networking.ItemStackSyncUtil;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
public class EnergyPistolItem extends Item implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public EnergyPistolItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("animation.energy_pistol.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private EnergyPistolRender renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new EnergyPistolRender();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack pStack) {
        ItemStack battery = getBattery(pStack);
        return battery != null && !battery.isEmpty();
    }

    @Override
    public int getBarWidth(@NotNull ItemStack pStack) {
        ItemStack battery = getBattery(pStack);
        if (battery.getItem() instanceof BatteryItem batteryItem) {
            int energy = ItemEnergyCapability.getEnergy(battery);

            return Math.round((energy / (float) batteryItem.getCapacity()) * 13); // A barra no MC tem 13 pixels de largura
        }

        return 0;
    }

    @Override
    public int getBarColor(@NotNull ItemStack pStack) {
        ItemStack battery = getBattery(pStack);
        if (battery.getItem() instanceof BatteryItem batteryItem) {
            int energy = ItemEnergyCapability.getEnergy(battery);
            float percentage = energy / (float) batteryItem.getCapacity();

            return Mth.hsvToRgb(percentage * 0.33F, 1.0F, 1.0F); // A barra no MC tem 13 pixels de largura
        }
        return 0;
    }

    public ItemStack getBattery(ItemStack stack) {
        return stack.get(DataComponentTypesInit.LOADED_BATTERY.get());
    }

    public boolean isCharged(ItemStack stack) {
        ItemStack loadedBattery = this.getBattery(stack);
        return loadedBattery != null && !loadedBattery.isEmpty();
    }

    private void setCharged(ItemStack stack, ItemStack value) {
        stack.set(DataComponentTypesInit.LOADED_BATTERY.get(), value);
    }

    private ItemStack getBetterBattery(Player pPlayer) {
        Optional<ItemStack> firstBattery = pPlayer.getInventory().items.stream()
                .filter(itemStack -> itemStack.getItem() instanceof BatteryItem)
                .max(Comparator.comparingInt(ItemEnergyCapability::getEnergy));

        return firstBattery.orElse(ItemStack.EMPTY);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack pistol = pPlayer.getItemInHand(pUsedHand);

        if (pLevel.isClientSide) return InteractionResultHolder.pass(pistol);

        if (isCharged(pistol)) {
            if (pPlayer.isCrouching()) { // remover bateria da arma e adicionar no inventario do player
                ItemStack battery = getBattery(pistol);
                ItemStack copyBattery = battery.copy();
                if (!pPlayer.addItem(copyBattery)) {
                    pPlayer.drop(copyBattery, false);
                }
                battery.shrink(1);
                pLevel.playSound(null, pPlayer, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                setCharged(pistol, null);
                return InteractionResultHolder.success(pistol);
            }
        } else {
            ItemStack battery = getBetterBattery(pPlayer);

            if (battery != ItemStack.EMPTY) {
                ItemStack copyStack = battery.copy();

                battery.shrink(1);
                setCharged(pistol, copyStack);
                pLevel.playSound(null, pPlayer, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.PLAYERS, 1.0F, 1.0F);

                return InteractionResultHolder.success(pistol);
            }
        }

        return InteractionResultHolder.fail(pistol);
    }

    private boolean consumeBattery(ItemStack stack, int energy) {
        ItemStack battery = this.getBattery(stack);
        if (battery == null) return false;

        Optional<Integer> optionalExtractEnergy = battery.getCapability(ForgeCapabilities.ENERGY)
                .map(iEnergyStorage -> iEnergyStorage.extractEnergy(energy, false));

        boolean success = optionalExtractEnergy.isPresent() && optionalExtractEnergy.get() == energy;

        if (success) setCharged(stack, battery);

        return success;
    }

    public void shoot(ServerPlayer pPlayer, ItemStack pItemStack) {
        ItemStack mainHandItem = pPlayer.getMainHandItem();
        if (pPlayer.getCooldowns().isOnCooldown(this) || !(mainHandItem.getItem() instanceof EnergyPistolItem)) return;

        if (consumeBattery(pItemStack, 100)) {
            log.info("atirou");
            pPlayer.getCooldowns().addCooldown(this, 20); // 1 segundo de cooldown
            ItemStackSyncUtil.syncItemInHand(pPlayer, InteractionHand.MAIN_HAND);

            EnergyProjectileEntity projectile = new EnergyProjectileEntity(pPlayer.level(), pPlayer, new Vec3(pPlayer.getLookAngle().x * 0.5, pPlayer.getLookAngle().y * 0.5, pPlayer.getLookAngle().z * 0.5));
            projectile.setPos(pPlayer.getX(), pPlayer.getEyeY() - 0.1, pPlayer.getZ());
            pPlayer.level().addFreshEntity(projectile);
        } else {
            pPlayer.displayClientMessage(Component.translatable("message.energy.not.enough"), true);
        }
    }
}
