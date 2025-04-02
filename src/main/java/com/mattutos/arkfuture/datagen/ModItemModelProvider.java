package com.mattutos.arkfuture.datagen;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.init.ItemInit;

import net.minecraft.data.PackOutput;

import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArkFuture.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.basicItem(ItemInit.ANCIENT_ORE_ITEM.get());
        this.basicItem(ItemInit.ANCIENT_ORE_INGOT_ITEM.get());
        this.basicItem(ItemInit.BATTERY_10K.get());
    }
}
