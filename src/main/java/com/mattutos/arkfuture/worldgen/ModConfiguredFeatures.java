package com.mattutos.arkfuture.worldgen;

import com.mattutos.arkfuture.ArkFuture;

import com.mattutos.arkfuture.init.BlockInit;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ANCIENT_ORE_KEY = registerKey("ancient_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ANCIENT_ORE_SMALL_KEY = registerKey("ancient_ore_small");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> overworldAncientOres = List.of(
                OreConfiguration.target(stoneReplaceables, BlockInit.ANCIENT_ORE_VEIN_BLOCK_ITEM.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, BlockInit.DEEPSLATE_ANCIENT_ORE_VEIN_BLOCK_ITEM.get().defaultBlockState()));

        register(context, OVERWORLD_ANCIENT_ORE_KEY, Feature.ORE, new OreConfiguration(overworldAncientOres, 9));
        register(context, OVERWORLD_ANCIENT_ORE_SMALL_KEY, Feature.ORE, new OreConfiguration(overworldAncientOres, 4));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}