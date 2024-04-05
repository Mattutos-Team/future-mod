package com.mattutos.future.init;

import com.mattutos.future.FutureMod;
import com.mattutos.future.block.EnergyCoalGeneratorBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FutureMod.MOD_ID);

    public static final RegistryObject<EnergyCoalGeneratorBlock> COAL_ENERGY_GENERATOR_BLOCK =
            BLOCKS.register("coal_energy_generator", () -> new EnergyCoalGeneratorBlock(BlockBehaviour.Properties.copy(Blocks.ANVIL)));

}
