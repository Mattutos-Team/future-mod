package com.mattutos.arkfuture.config;

import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public abstract class EnumContainerData<T extends Enum<T> & BaseData<T>> implements ContainerData {

    private final int[] ints;
    private final Class<T> baseData;

    public EnumContainerData(Class<T> pBaseData) {
        this.ints = new int[pBaseData.getEnumConstants().length];
        this.baseData = pBaseData;
    }

    public static <T extends Enum<T> & BaseData<T>> SimpleContainerData createSimple(Class<T> pBaseData) {
        return new SimpleContainerData(pBaseData.getEnumConstants().length);
    }

    @Override
    public int get(int pIndex) {
        return this.ints[pIndex];
    }

    @Override
    public void set(int pIndex, int pValue) {
        this.set(getByIndex(pIndex), pValue);
    }

    @Override
    public int getCount() {
        return ints.length;
    }

    public long get(T enumData) {
        return enumData.getValueContainerData(this);
    }

    public abstract void set(T enumData, long pValue);

    public T getByIndex(int pIndex) {
        return baseData.getEnumConstants()[pIndex];
    }
}
