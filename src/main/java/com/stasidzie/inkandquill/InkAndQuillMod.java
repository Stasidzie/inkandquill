package com.stasidzie.inkandquill;

import com.mojang.logging.LogUtils;
import com.stasidzie.inkandquill.network.ModPackets;
import com.stasidzie.inkandquill.sounds.ModSounds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import com.stasidzie.inkandquill.item.ModItems;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault

@Mod(InkAndQuillMod.MODID)
public class InkAndQuillMod {
    public static final String MODID = "inkandquill";
    public static final Logger LOGGER = LogUtils.getLogger();

    public InkAndQuillMod() {
        LOGGER.info("Start registering");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModPackets.register();
        ModSounds.register(modEventBus);

        modEventBus.addListener(ModItems::addCreative);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("Finish registering");
    }


}
