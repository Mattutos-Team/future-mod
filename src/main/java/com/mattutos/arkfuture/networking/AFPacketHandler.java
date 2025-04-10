package com.mattutos.arkfuture.networking;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.networking.packet.MouseScrollingVerticalC2SPacket;
import com.mattutos.arkfuture.networking.packet.ShootPistolC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class AFPacketHandler {
    public static final SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    static {
        INSTANCE = ChannelBuilder.named(ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, "main"))
                .networkProtocolVersion(1)
                .clientAcceptedVersions((s, v) -> true)
                .serverAcceptedVersions((s, v) -> true)
                .simpleChannel();
    }

    public static void register() {
        INSTANCE.messageBuilder(ShootPistolC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ShootPistolC2SPacket::new)
                .encoder(ShootPistolC2SPacket::toBytes)
                .consumerMainThread(ShootPistolC2SPacket::handle)
                .add();

        INSTANCE.messageBuilder(MouseScrollingVerticalC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MouseScrollingVerticalC2SPacket::new)
                .encoder(MouseScrollingVerticalC2SPacket::toBytes)
                .consumerMainThread(MouseScrollingVerticalC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG pMessage) {
        INSTANCE.send(pMessage, PacketDistributor.SERVER.noArg());
    }

    public static <MSG> void sendToPlayer(MSG pMessage, ServerPlayer pServerPlayer) {
        INSTANCE.send(pMessage, PacketDistributor.PLAYER.with(pServerPlayer));
    }

}
