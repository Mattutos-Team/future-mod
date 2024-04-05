package com.mattutos.future;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = FutureMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static final RandomSource RANDOM_SOURCE = new LegacyRandomSource(new Random().nextLong());

}
