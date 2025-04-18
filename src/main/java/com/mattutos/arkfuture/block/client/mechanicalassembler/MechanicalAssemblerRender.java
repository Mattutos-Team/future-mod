package com.mattutos.arkfuture.block.client.mechanicalassembler;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.block.entity.MechanicalAssemblerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.model.GeoModel;

public class MechanicalAssemblerRender extends AFAssemblerRender<MechanicalAssemblerBlockEntity> {
    public MechanicalAssemblerRender(GeoModel<MechanicalAssemblerBlockEntity> ignoredModel) {
        super(new DefaultedBlockGeoModel<>(ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, "mechanical_assembler")));
    }
}
