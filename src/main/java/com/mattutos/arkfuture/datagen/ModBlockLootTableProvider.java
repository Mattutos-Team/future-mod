package com.mattutos.arkfuture.datagen;

import com.mattutos.arkfuture.init.BlockInit;
import com.mattutos.arkfuture.init.ItemInit;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider pRegistries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    @Override
    protected void generate() {
        this.dropSelf(BlockInit.ANCIENT_ORE_BLOCK_ITEM.get());
        this.dropSelf(BlockInit.ANCIENT_ORE_INGOT_BLOCK_ITEM.get());
        this.dropSelf(BlockInit.COAL_POWER_GENERATOR.get());
        this.dropSelf(BlockInit.MECHANICAL_TABLE.get());

        this.add(BlockInit.ANCIENT_ORE_VEIN_BLOCK_ITEM.get(),
                block ->
                        this.createMultipleOreDrops(
                                BlockInit.ANCIENT_ORE_VEIN_BLOCK_ITEM.get(),
                                ItemInit.ANCIENT_ORE_ITEM.get(),
                                2f,
                                5f)
        );

        this.add(BlockInit.DEEPSLATE_ANCIENT_ORE_VEIN_BLOCK_ITEM.get(),
                block ->
                        this.createMultipleOreDrops(
                                BlockInit.ANCIENT_ORE_VEIN_BLOCK_ITEM.get(),
                                ItemInit.ANCIENT_ORE_ITEM.get(),
                                2f,
                                5f)
        );
    }

    protected LootTable.Builder createMultipleOreDrops(Block pBlock, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(
                pBlock,
                this.applyExplosionDecay(
                        pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                                .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
                )
        );
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
