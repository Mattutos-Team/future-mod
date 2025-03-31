package com.mattutos.arkfuture.datagen;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.init.BlockInit;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ArkFuture.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BlockInit.ANCIENT_ORE_BLOCK_ITEM.get())
                .add(BlockInit.ANCIENT_ORE_INGOT_BLOCK_ITEM.get())
                .add(BlockInit.ANCIENT_ORE_VEIN_BLOCK_ITEM.get())
                .add(BlockInit.DEEPSLATE_ANCIENT_ORE_VEIN_BLOCK_ITEM.get())
                .add(BlockInit.COAL_POWER_GENERATOR.get())
                .add(BlockInit.MECHANICAL_TABLE.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(BlockInit.ANCIENT_ORE_BLOCK_ITEM.get())
                .add(BlockInit.ANCIENT_ORE_INGOT_BLOCK_ITEM.get())
                .add(BlockInit.ANCIENT_ORE_VEIN_BLOCK_ITEM.get())
                .add(BlockInit.DEEPSLATE_ANCIENT_ORE_VEIN_BLOCK_ITEM.get())
                .add(BlockInit.COAL_POWER_GENERATOR.get())
                .add(BlockInit.MECHANICAL_TABLE.get());
    }
}
