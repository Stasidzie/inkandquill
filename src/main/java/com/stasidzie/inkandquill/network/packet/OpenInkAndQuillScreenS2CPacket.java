package com.stasidzie.inkandquill.network.packet;

import com.stasidzie.inkandquill.client.screen.InkAndQuillScreen;
import com.stasidzie.inkandquill.item.InkAndQuillItem;
import com.stasidzie.inkandquill.util.HandUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenInkAndQuillScreenS2CPacket {

    private final InteractionHand handWithQuill;

    public OpenInkAndQuillScreenS2CPacket(InteractionHand handWithQuill) {
        this.handWithQuill = handWithQuill;
    }

    public OpenInkAndQuillScreenS2CPacket(FriendlyByteBuf buf) {
        this.handWithQuill = buf.readEnum(InteractionHand.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(handWithQuill);
    }

    public static void onReceive(OpenInkAndQuillScreenS2CPacket packet, Supplier<NetworkEvent.Context> ignoredContextSupplier) {
        handle(packet);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handle(OpenInkAndQuillScreenS2CPacket packet) {
        Player player = Minecraft.getInstance().player;
        if (player != null && InkAndQuillItem.handsCheck(player, packet.handWithQuill)) {
            ItemStack itemToRename = player.getItemInHand(HandUtil.otherHand(packet.handWithQuill));
            String itemName = itemToRename.hasCustomHoverName() ? itemToRename.getHoverName().getString() : "";
            Minecraft.getInstance().setScreen(new InkAndQuillScreen(itemToRename, itemName, packet.handWithQuill));
        }
    }
}
