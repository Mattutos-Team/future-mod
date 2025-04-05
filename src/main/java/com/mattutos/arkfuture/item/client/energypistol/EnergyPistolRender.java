package com.mattutos.arkfuture.item.client.energypistol;

import com.mattutos.arkfuture.item.EnergyPistolItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class EnergyPistolRender extends GeoItemRenderer<EnergyPistolItem> {
    public EnergyPistolRender() {
        super(new EnergyPistolModel());
    }
}
