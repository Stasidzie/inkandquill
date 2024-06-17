package com.stasidzie.inkandquill.item;

import com.stasidzie.inkandquill.InkAndQuillMod;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, InkAndQuillMod.MODID);

    public static final RegistryObject<Item> INK_BOTTLE = ITEMS.register("ink_bottle",
            () -> new Item(new Item.Properties())
    );
    public static final RegistryObject<Item> INK_AND_QUILL = ITEMS.register("ink_and_quill",
            () -> new InkAndQuillItem((new Item.Properties()).durability(16)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(INK_BOTTLE);
            event.accept(INK_AND_QUILL);
        } else if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(INK_AND_QUILL);
        }
    }
}
