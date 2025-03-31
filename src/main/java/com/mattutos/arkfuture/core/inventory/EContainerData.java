package com.mattutos.arkfuture.core.inventory;

import net.minecraft.world.inventory.ContainerData;

public interface EContainerData<T extends Enum<T> & BaseData<T>> extends ContainerData {

    long get(T enumData);

    void set(T enumData, long pValue);

}
