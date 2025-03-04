package com.stasidzie.inkandquill.item;

import com.stasidzie.inkandquill.network.ModPackets;
import com.stasidzie.inkandquill.network.packet.OpenInkAndQuillScreenS2CPacket;
import com.stasidzie.inkandquill.tag.ModTags;
import com.stasidzie.inkandquill.util.HandUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class InkAndQuillItem extends Item {
    public InkAndQuillItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return damage(itemStack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack inkAndQuill = player.getItemInHand(hand);
        ItemStack otherItem = player.getItemInHand(HandUtil.otherHand(hand));

        if (otherItem.isEmpty()) {
            if (player.level().isClientSide) {
                player.displayClientMessage(Component.translatable("message.inkandquill.item.inkandquill.empty_hand_using"), true);
            }
            return InteractionResultHolder.fail(inkAndQuill);
        }
        if (!otherItem.is(ModTags.Items.INK_AND_QUILL_RENAMEABLE)) {
            return InteractionResultHolder.pass(inkAndQuill);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            ModPackets.sendToPlayer(new OpenInkAndQuillScreenS2CPacket(hand), serverPlayer);
        }
        return InteractionResultHolder.consume(inkAndQuill);
    }

    /**
     * @param player player that suppose to hold Ink and Quill item
     * @param handWithQuill hand that suppose to hold Ink and Quill item
     * @return true if Ink and Quill item is in given hand and if the other hand holds renameable item
     */
    public static boolean handsCheck(Player player, InteractionHand handWithQuill) {
        return player.getItemInHand(handWithQuill).is(ModItems.INK_AND_QUILL.get())
                && player.getItemInHand(HandUtil.otherHand(handWithQuill)).is(ModTags.Items.INK_AND_QUILL_RENAMEABLE);
    }

    /**
     * @param inkAndQuillItem Ink and Quill item stack
     * @return item damaged by 1 or empty bottle if destroyed
     */
    public static ItemStack damage(ItemStack inkAndQuillItem) {
        if (inkAndQuillItem.getDamageValue() < inkAndQuillItem.getMaxDamage() - 1) {
            ItemStack damagedStack = inkAndQuillItem.copy();
            damagedStack.setDamageValue(damagedStack.getDamageValue() + 1);
            return damagedStack;
        }
        return new ItemStack(Items.GLASS_BOTTLE);
    }
}
