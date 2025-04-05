package com.mattutos.arkfuture.item.client.energypistol;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.item.EnergyPistolItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class EnergyPistolModel extends DefaultedItemGeoModel<EnergyPistolItem> {

    public EnergyPistolModel() {
        super(ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, "energy_pistol"));
    }
}
