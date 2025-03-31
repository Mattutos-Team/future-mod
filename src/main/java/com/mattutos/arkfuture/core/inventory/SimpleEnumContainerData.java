package com.mattutos.arkfuture.core.inventory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SimpleEnumContainerData<T extends Enum<T> & BaseData<T>> extends EnumContainerData<T> {

    private final int[] ints;
    protected final Class<T> baseData;
    protected final Map<T, Integer> mapEnumStartIndex;

    public SimpleEnumContainerData(Class<T> pBaseData) {
        super(pBaseData);
        ints = new int[Arrays.stream(pBaseData.getEnumConstants()).mapToInt(BaseData::getDataPack).sum()];
        this.baseData = pBaseData;

        mapEnumStartIndex = new HashMap<>();
        int index = 0;
        for (T data : pBaseData.getEnumConstants()) {
            mapEnumStartIndex.put(data, index);
            index += data.getDataPack();
        }
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

    @Override
    public long get(T enumData) {
        var startedPosition = mapEnumStartIndex.get(enumData);
        long value = 0;

        for (int i = (enumData.getDataPack() - 1); i >= 0; i--) {
            value <<= 16;
            value += this.get(startedPosition + i);
        }

        return value;
    }

    @Override
    public void set(T enumData, long pValue) {
        var startedPosition = mapEnumStartIndex.get(enumData);

        for (int i = (enumData.getDataPack() - 1); i >= 0; i--) {
            short valueToSave = (short) (pValue & 0xFFFF);
            pValue >>= 16;
            this.set(startedPosition + i, valueToSave);
        }
    }

}
