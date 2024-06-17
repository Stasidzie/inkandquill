package com.stasidzie.inkandquill.network;

import com.stasidzie.inkandquill.InkAndQuillMod;
import com.stasidzie.inkandquill.network.packet.ChangeNameViaInkAndQuillC2SPacket;
import com.stasidzie.inkandquill.network.packet.OpenInkAndQuillScreenS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPackets {
    private static SimpleChannel channel;

    public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(InkAndQuillMod.MODID, "messages");
    public static final String NETWORK_VERSION = "1";

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        channel = NetworkRegistry.ChannelBuilder
                .named(CHANNEL_NAME)
                .networkProtocolVersion(() -> NETWORK_VERSION)
                .clientAcceptedVersions(NETWORK_VERSION::equals)
                .serverAcceptedVersions(NETWORK_VERSION::equals)
                .simpleChannel();

        //Packets:

        channel.messageBuilder(OpenInkAndQuillScreenS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(OpenInkAndQuillScreenS2CPacket::new)
                .encoder(OpenInkAndQuillScreenS2CPacket::toBytes)
                .consumerMainThread(OpenInkAndQuillScreenS2CPacket::onReceive)
                .add();

        channel.messageBuilder(ChangeNameViaInkAndQuillC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ChangeNameViaInkAndQuillC2SPacket::new)
                .encoder(ChangeNameViaInkAndQuillC2SPacket::toBytes)
                .consumerMainThread(ChangeNameViaInkAndQuillC2SPacket::onReceive)
                .add();
    }

    public static void sendToServer(Object message) {
        channel.sendToServer(message);
    }

    public static void sendToPlayer(Object message, ServerPlayer player) {
        channel.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
