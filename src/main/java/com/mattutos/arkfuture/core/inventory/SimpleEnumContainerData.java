package com.mattutos.arkfuture.core.inventory;

import java.util.Arrays;

public class SimpleEnumContainerData<T extends Enum<T> & BaseData<T>> implements EContainerData<T> {

    private final int[] ints;
    protected final Class<T> baseData;

    public SimpleEnumContainerData(Class<T> pBaseData) {
        ints = new int[Arrays.stream(pBaseData.getEnumConstants()).mapToInt(BaseData::getDataPack).sum()];
        this.baseData = pBaseData;
    }

    @Override
    public void set(int pIndex, int pValue) {
        ints[pIndex] = pValue;
    }

    @Override
    public int get(int pIndex) {
        return ints[pIndex];
    }

    @Override
    public int getCount() {
        return ints.length;
    }

    public long get(T enumData) {
        return enumData.getValueContainerData(this);
    }

    public void set(T enumData, long pValue) {
        enumData.setValueContainerData(this, pValue);
    }

}
