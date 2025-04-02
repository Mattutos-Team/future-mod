package com.mattutos.arkfuture.datagen;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.init.BlockInit;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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

        this.customStatesFacingAndPowered(BlockInit.COAL_POWER_GENERATOR);
        this.blockOrientableWithItem(BlockInit.MECHANICAL_TABLE);
    }

    private String getBlockPath(Block block) {
        ResourceLocation blockResource = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block));
        return blockResource.getPath();
    }

    private void blockCubeAllWithItem(RegistryObject<Block> blockRegistryObject) {
        this.simpleBlockWithItem(blockRegistryObject.get(), this.cubeAll(blockRegistryObject.get()));
    }

    private void blockOrientableWithItem(RegistryObject<? extends Block> blockRegistryObject, String namespace, String side, String front, String top) {
        Block block = blockRegistryObject.get();

        ResourceLocation sideResource = ResourceLocation.fromNamespaceAndPath(namespace, ModelProvider.BLOCK_FOLDER + "/" + side);
        ResourceLocation frontResource = ResourceLocation.fromNamespaceAndPath(namespace, ModelProvider.BLOCK_FOLDER + "/" + front);
        ResourceLocation topResource = ResourceLocation.fromNamespaceAndPath(namespace, ModelProvider.BLOCK_FOLDER + "/" + top);

        ModelFile model = this.models().orientable(getBlockPath(block), sideResource, frontResource, topResource);

        this.simpleBlockWithItem(block, model);
    }

    private void customStatesFacingAndPowered(RegistryObject<? extends Block> blockRegistryObject) {
        String blockPath = getBlockPath(blockRegistryObject.get());
        String side = blockPath + "_side";
        String front = blockPath + "_front";
        String frontOn = blockPath + "_front_on";
        String top = blockPath + "_top";

        ResourceLocation sideResource = ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, ModelProvider.BLOCK_FOLDER + "/" + blockPath + "/" + side);
        ResourceLocation frontResource = ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, ModelProvider.BLOCK_FOLDER + "/" + blockPath + "/" + front);
        ResourceLocation frontOnResource = ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, ModelProvider.BLOCK_FOLDER + "/" + blockPath + "/" + frontOn);
        ResourceLocation topResource = ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, ModelProvider.BLOCK_FOLDER + "/" + blockPath + "/" + top);


        BlockModelBuilder modelCPGOff = this.models().orientable(blockPath, sideResource, frontResource, topResource);
        BlockModelBuilder modelCPGOn = this.models().orientable(blockPath.concat("_on"), sideResource, frontOnResource, topResource);

        getVariantBuilder(blockRegistryObject.get()).forAllStates(state -> {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            boolean powered = state.getValue(BlockStateProperties.POWERED);

            return ConfiguredModel.builder()
                    .modelFile(powered ? modelCPGOn : modelCPGOff)
                    .rotationY((int) facing.getOpposite().toYRot())
                    .build();
        });
        this.simpleBlockItem(blockRegistryObject.get(), modelCPGOff);
    }

    /**
     * For "/orientable" blocks with no texture
     */
    private void blockOrientableWithItem(RegistryObject<? extends Block> blockRegistryObject) {
        Block block = blockRegistryObject.get();
        BlockModelBuilder model = this.models().withExistingParent(this.getBlockPath(block), ModelProvider.BLOCK_FOLDER + "/orientable");

        this.simpleBlockWithItem(block, model);
    }

    private void blockOrientableWithItem(RegistryObject<? extends Block> blockRegistryObject, String side, String front, String top) {
        this.blockOrientableWithItem(blockRegistryObject, ArkFuture.MOD_ID, side, front, top);
    }
}
