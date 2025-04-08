package com.mattutos.arkfuture.event;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.item.EnergyPistolItem;
import com.mattutos.arkfuture.networking.AFPacketHandler;
import com.mattutos.arkfuture.networking.packet.ShootPistolC2SPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArkFuture.MOD_ID)
public class EnergyPistolShootEvent {

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        ItemStack heldItem = event.getItemStack();
        if (heldItem.getItem() instanceof EnergyPistolItem) {
            AFPacketHandler.sendToServer(new ShootPistolC2SPacket());
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        ItemStack heldItem = event.getItemStack();
        if (heldItem.getItem() instanceof EnergyPistolItem) {
            AFPacketHandler.sendToServer(new ShootPistolC2SPacket());
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        ItemStack mainHand = player.getMainHandItem();

        if (mainHand.getItem() instanceof EnergyPistolItem) {
            AFPacketHandler.sendToServer(new ShootPistolC2SPacket());
            event.setCanceled(true); // Impede bater em entidades
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        if (!event.getEntity().level().isClientSide && event.getEntity() instanceof LivingEntity living) {
            FreezeHandler.tickEntity(living);
        }
    }

}
