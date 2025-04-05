package com.mattutos.arkfuture.worldgen;

import com.mattutos.arkfuture.ArkFuture;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> ANCIENT_ORE_PLACED_UPPER_KEY = registerKey("ancient_ore_upper_placed");
    public static final ResourceKey<PlacedFeature> ANCIENT_ORE_PLACED_MIDDLE_KEY = registerKey("ancient_ore_middle_placed");
    public static final ResourceKey<PlacedFeature> ANCIENT_ORE_PLACED_SMALL_KEY = registerKey("ancient_ore_small_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, ANCIENT_ORE_PLACED_MIDDLE_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_ANCIENT_ORE_KEY),
                ModOrePlacement.commonOrePlacement(
                        10,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56))));

        register(context, ANCIENT_ORE_PLACED_SMALL_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_ANCIENT_ORE_SMALL_KEY),
                ModOrePlacement.commonOrePlacement(
                        10,
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, name));
    }

    private static void register(
            BootstrapContext<PlacedFeature> context,
            ResourceKey<PlacedFeature> key,
            Holder<ConfiguredFeature<?, ?>> configuration,
            List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}