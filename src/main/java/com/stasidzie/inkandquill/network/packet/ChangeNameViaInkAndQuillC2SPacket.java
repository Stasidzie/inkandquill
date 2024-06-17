package com.stasidzie.inkandquill.network.packet;

import com.stasidzie.inkandquill.item.InkAndQuillItem;
import com.stasidzie.inkandquill.item.ModItems;
import com.stasidzie.inkandquill.sounds.ModSounds;
import com.stasidzie.inkandquill.util.HandUtil;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ChangeNameViaInkAndQuillC2SPacket {

    private final String newName;
    private final InteractionHand handWithQuill;

    public ChangeNameViaInkAndQuillC2SPacket(String newName, InteractionHand handWithQuill) {
        this.newName = newName;
        this.handWithQuill = handWithQuill;
    }

    public ChangeNameViaInkAndQuillC2SPacket(FriendlyByteBuf buf) {
        this.newName = buf.readUtf();
        this.handWithQuill = buf.readEnum(InteractionHand.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.newName);
        buf.writeEnum(this.handWithQuill);
    }

    public static void onReceive(ChangeNameViaInkAndQuillC2SPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer player = context.getSender();
        if (player != null) {
            context.enqueueWork(() -> handle(packet, player));
            context.setPacketHandled(true);
        }
    }

    public static void handle(ChangeNameViaInkAndQuillC2SPacket packet, ServerPlayer player) {
        if (!InkAndQuillItem.handsCheck(player, packet.handWithQuill))
            return;

        ItemStack itemToRename = player.getItemInHand(HandUtil.otherHand(packet.handWithQuill));
        if (packet.newName.equals(itemToRename.getHoverName().getString()))
            return;

        String s = validateName(packet.newName);
        if (s == null)
            return;

        boolean wasUsed = false;

        if (Util.isBlank(s)) {
            if (itemToRename.hasCustomHoverName()) {
                itemToRename.resetHoverName();
                wasUsed = true;
             }
        } else {
            itemToRename.setHoverName(Component.literal(s));
            wasUsed = true;
        }

        if (wasUsed) {
            ItemStack inkAndQuill = player.getItemInHand(packet.handWithQuill);
            player.setItemInHand(packet.handWithQuill, InkAndQuillItem.damage(inkAndQuill));
            //noinspection resource
            Level level = player.level();
            RandomSource randomsource = player.getRandom();
            player.awardStat(Stats.ITEM_USED.get(ModItems.INK_AND_QUILL.get()));
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.INK_AND_QUILL_WROTE.get(), SoundSource.PLAYERS, 1.0F, (randomsource.nextFloat() - randomsource.nextFloat()) * 0.2F + 1.0F);
        }
    }

    private static @Nullable String validateName(String itemName) {
        String s = SharedConstants.filterText(itemName);
        return s.length() <= 50 ? s : null;
    }

}
