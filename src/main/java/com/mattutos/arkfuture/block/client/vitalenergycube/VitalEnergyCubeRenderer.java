package com.mattutos.arkfuture.block.client.vitalenergycube;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.block.entity.VitalEnergyCubeBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class VitalEnergyCubeRenderer extends GeoBlockRenderer<VitalEnergyCubeBlockEntity> {

    public VitalEnergyCubeRenderer(GeoModel<VitalEnergyCubeBlockEntity> model) {
        super(new DefaultedBlockGeoModel<>(ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, "vital_energy_cube")));
    }


}
