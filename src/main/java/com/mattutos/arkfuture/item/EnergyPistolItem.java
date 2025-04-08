package com.mattutos.arkfuture.item;

import com.mattutos.arkfuture.core.energy.AFEnergyStorage;
import com.mattutos.arkfuture.entity.EnergyProjectileEntity;
import com.mattutos.arkfuture.init.DataComponentTypesInit;
import com.mattutos.arkfuture.item.client.energypistol.EnergyPistolRender;
import com.mattutos.arkfuture.item.common.SingleItemTooltip;
import com.mattutos.arkfuture.item.util.ItemEnergyCapability;
import com.mattutos.arkfuture.networking.ItemStackSyncUtil;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import java.util.List;
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

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if (!Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.ark_future.press.shift"));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.ark_future.item.energy_pistol.power", getPower(pStack)));
            pTooltipComponents.add(Component.translatable("tooltip.ark_future.item.energy_pistol.consume", energyUsePerShoot(pStack)));
        }

        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack pStack) {
        if (Screen.hasShiftDown()) {
            ItemStack battery = getBattery(pStack);
            if (battery != null && !battery.isEmpty()) {
                String textBattery = battery.getCapability(ForgeCapabilities.ENERGY)
                        .map(iEnergyStorage -> (iEnergyStorage.getEnergyStored()) + " / " + (iEnergyStorage.getMaxEnergyStored()))
                        .orElse(null);
                return Optional.of(new SingleItemTooltip(battery, textBattery));
            }
        }
        return super.getTooltipImage(pStack);
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

    public int getPower(ItemStack stack) {
        Integer power = stack.get(DataComponentTypesInit.POWER_INT.get());
        return power == null ? 1 : power;
    }

    public void setPower(ItemStack stack, int power) {
        if (power <= 0) power = 1;
        else if (power > 3) power = 3;
        stack.set(DataComponentTypesInit.POWER_INT.get(), power);
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

        Optional<Integer> optionalExtractEnergy = battery.getCapability(ForgeCapabilities.ENERGY) // testa se a bateria tem energia
                .map(iEnergyStorage -> ((AFEnergyStorage) iEnergyStorage).forceExtractEnergy(energy, true));

        boolean success = optionalExtractEnergy.isPresent() && optionalExtractEnergy.get() == energy;

        if (success) {
            battery.getCapability(ForgeCapabilities.ENERGY) // remove a energia da bateria
                    .map(iEnergyStorage -> ((AFEnergyStorage) iEnergyStorage).forceExtractEnergy(energy, false));

            setCharged(stack, battery);
        }

        return success;
    }

    private int energyUsePerShoot(ItemStack pItemStack) {
        return  (int) (100 * Math.pow(10, (getPower(pItemStack) - 1)));
    }

    public void shoot(ServerPlayer pPlayer, ItemStack pItemStack) {
        ItemStack mainHandItem = pPlayer.getMainHandItem();
        if (pPlayer.getCooldowns().isOnCooldown(this) || !(mainHandItem.getItem() instanceof EnergyPistolItem)) return;

        int energyToUse = energyUsePerShoot(pItemStack);
        if (consumeBattery(pItemStack, energyToUse)) {
            // força 1: 10 * 2 = 20 ticks
            // força 2: 10 * 4 = 40 ticks
            // força 3: 10 * 8 = 80 ticks
            int tickFreezyEntity = (int) (10 * (Math.pow(2, getPower(pItemStack)))); // Paralisia depende da força

            pPlayer.getCooldowns().addCooldown(this, (int)(tickFreezyEntity * 1.25)); // cooldown é o tempo de freeze + 25%
            ItemStackSyncUtil.syncItemInHand(pPlayer, InteractionHand.MAIN_HAND);

            Vec3 vec3 = new Vec3(pPlayer.getLookAngle().x * 0.5, pPlayer.getLookAngle().y * 0.5, pPlayer.getLookAngle().z * 0.5);
            EnergyProjectileEntity projectile = new EnergyProjectileEntity(pPlayer.level(), pPlayer, vec3, tickFreezyEntity);
            projectile.setPos(pPlayer.getX(), pPlayer.getEyeY() - 0.1, pPlayer.getZ());
            pPlayer.level().addFreshEntity(projectile);
        } else {
            pPlayer.displayClientMessage(Component.translatable("message.ark_future.energy_pistol.energy.not.enough"), true);
        }
    }

    public void changePower(ServerPlayer pPlayer, ItemStack pItemStack, double power) {
        setPower(pItemStack, (int) (getPower(pItemStack) + power));
        pPlayer.displayClientMessage(Component.translatable("message.ark_future.energy_pistol.power.defined", getPower(pItemStack)), true);
    }
}
