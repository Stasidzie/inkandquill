package com.stasidzie.inkandquill.tag;

import com.stasidzie.inkandquill.InkAndQuillMod;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

@MethodsReturnNonnullByDefault

public class ModTags {
    public static class Items {

        public static final TagKey<Item> INK_AND_QUILL_RENAMEABLE = tag("ink_and_quill_renameable");

        @SuppressWarnings("SameParameterValue")
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(InkAndQuillMod.MODID, name));
        }
    }
}
