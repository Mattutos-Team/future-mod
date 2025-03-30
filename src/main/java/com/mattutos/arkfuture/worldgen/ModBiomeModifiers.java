package com.mattutos.arkfuture.worldgen;

import com.mattutos.arkfuture.ArkFuture;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_ANCIENT_ORE_UPPER = registerKey("add_ancient_ore_upper");
    public static final ResourceKey<BiomeModifier> ADD_ANCIENT_ORE_MIDDLE = registerKey("add_ancient_ore_middle");
    public static final ResourceKey<BiomeModifier> ADD_ANCIENT_ORE_SMALL = registerKey("add_ancient_ore_small");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeature = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        context.register(ADD_ANCIENT_ORE_UPPER,
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                        HolderSet.direct(placedFeature.getOrThrow(ModPlacedFeatures.ANCIENT_ORE_PLACED_UPPER_KEY)),
                        GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_ANCIENT_ORE_MIDDLE,
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                        HolderSet.direct(placedFeature.getOrThrow(ModPlacedFeatures.ANCIENT_ORE_PLACED_MIDDLE_KEY)),
                        GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_ANCIENT_ORE_SMALL,
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                        HolderSet.direct(placedFeature.getOrThrow(ModPlacedFeatures.ANCIENT_ORE_PLACED_SMALL_KEY)),
                        GenerationStep.Decoration.UNDERGROUND_ORES));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, name));
    }
}