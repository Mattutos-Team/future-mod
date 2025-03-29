package com.mattutos.arkfuture.datagen;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.init.BlockInit;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ArkFuture.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.blockCubeAllWithItem(BlockInit.ANCIENT_ORE_VEIN_BLOCK_ITEM);
        this.blockCubeAllWithItem(BlockInit.DEEPSLATE_ANCIENT_ORE_VEIN_BLOCK_ITEM);
        this.blockCubeAllWithItem(BlockInit.ANCIENT_ORE_BLOCK_ITEM);
        this.blockCubeAllWithItem(BlockInit.ANCIENT_ORE_INGOT_BLOCK_ITEM);

        this.blockOrientableWithItem(
                BlockInit.COAL_POWER_GENERATOR,
                "coal_power_generator_side",
                "coal_power_generator_front",
                "coal_power_generator_top");
        this.blockOrientableWithItem(BlockInit.MECHANICAL_TABLE);
    }

    private String getBlockPath(Block block) {
        ResourceLocation blockResource = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block));
        return blockResource.getPath();
    }

    private void blockCubeAllWithItem(RegistryObject<Block> blockRegistryObject) {
        this.simpleBlockWithItem(blockRegistryObject.get(), this.cubeAll(blockRegistryObject.get()));
    }

    private void blockOrientableWithItem(
            RegistryObject<? extends Block> blockRegistryObject,
            String namespace,
            String side,
            String front,
            String top) {
        Block block = blockRegistryObject.get();

        ResourceLocation sideResource = ResourceLocation.fromNamespaceAndPath(namespace, ModelProvider.BLOCK_FOLDER + "/" + side);
        ResourceLocation frontResource = ResourceLocation.fromNamespaceAndPath(namespace, ModelProvider.BLOCK_FOLDER + "/" + front);
        ResourceLocation topResource = ResourceLocation.fromNamespaceAndPath(namespace, ModelProvider.BLOCK_FOLDER + "/" + top);

        this.simpleBlockWithItem(
                block,
                this.models().orientable(getBlockPath(block), sideResource, frontResource, topResource)
        );
    }

    /**
     * For "/orientable" blocks with no texture
     */
    private void blockOrientableWithItem(RegistryObject<? extends Block> blockRegistryObject) {
        Block block = blockRegistryObject.get();

        this.simpleBlockWithItem(
                block,
                this.models().withExistingParent(
                        this.getBlockPath(block), ModelProvider.BLOCK_FOLDER + "/orientable"));
    }

    private void blockOrientableWithItem(
            RegistryObject<? extends Block> blockRegistryObject,
            String side,
            String front,
            String top) {
        this.blockOrientableWithItem(blockRegistryObject, ArkFuture.MOD_ID, side, front, top);
    }
}
