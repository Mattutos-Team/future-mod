package com.mattutos.arkfuture.block.client.mechanicalassembler;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.block.entity.AssemblerPartBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.model.GeoModel;

public class AssemblerPartRender extends AFAssemblerRender<AssemblerPartBlockEntity> {
    public AssemblerPartRender(GeoModel<AssemblerPartBlockEntity> ignoredModel) {
        super(new DefaultedBlockGeoModel<>(ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, "assembler_part")));
    }
}
