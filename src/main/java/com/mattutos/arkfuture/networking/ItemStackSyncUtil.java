package com.mattutos.arkfuture.networking;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class ItemStackSyncUtil {

    public static void syncItemInHand(ServerPlayer player, InteractionHand hand) {
        // Slot da mão principal (slot 36-44 no inventário)
        int slot = player.getInventory().selected + 36; // mão principal

        ItemStack stack = player.getMainHandItem();
        AbstractContainerMenu container = player.containerMenu;

        // Envia o packet direto para o cliente
        player.connection.send(
                new ClientboundContainerSetSlotPacket(
                        container.containerId,  // containerId atual (normalmente 0 para inventário)
                        container.getStateId(), // estado da UI
                        slot,                   // slot no inventário
                        stack.copy()            // stack com os dados atualizados
                )
        );
    }
}
