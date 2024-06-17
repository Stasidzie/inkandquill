package com.stasidzie.inkandquill.util;

import net.minecraft.world.InteractionHand;

public class HandUtil {
    /**
     * Returns the other hand from received
     */
    public static InteractionHand otherHand(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }
}
