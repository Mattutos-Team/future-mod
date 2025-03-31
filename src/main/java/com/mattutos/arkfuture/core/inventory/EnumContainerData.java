package com.mattutos.arkfuture.core.inventory;

import net.minecraft.world.inventory.ContainerData;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public abstract class EnumContainerData<T extends Enum<T> & BaseData<T>> implements ContainerData {

    protected final Map<Integer, AbstractMap.SimpleEntry<T, Integer>> mapEnumIndexes;

    public EnumContainerData(Class<T> pBaseData) {
        mapEnumIndexes = new HashMap<>();
        int index = 0;
        for (T baseData : pBaseData.getEnumConstants()) {
            int dataPack = baseData.getDataPack();

            for (int i = 0; i < dataPack; i++) {
                AbstractMap.SimpleEntry<T, Integer> simpleEntry = new AbstractMap.SimpleEntry<>(baseData, i);
                mapEnumIndexes.put(index, simpleEntry);
                index++;
            }
        }
    }

    @Override
    public void set(int pIndex, int pValue) {
        AbstractMap.SimpleEntry<T, Integer> simpleEntry = mapEnumIndexes.get(pIndex);

        T enumData = simpleEntry.getKey();
        long index = simpleEntry.getValue();

        this.set(enumData, (this.get(enumData) & ~(0xffffL << (16 * index))) | ((long) pValue << (16 * index)));
    }

    @Override
    public int get(int pIndex) {
        AbstractMap.SimpleEntry<T, Integer> simpleEntry = mapEnumIndexes.get(pIndex);

        T enumData = simpleEntry.getKey();
        long value = this.get(enumData);
        int index = simpleEntry.getValue();

        return (short) ((value >> (16 * index)) & 0xffff);
    }

    @Override
    public int getCount() {
        return mapEnumIndexes.size();
    }

    abstract public void set(T enumData, long value);

    abstract public long get(T enumData);
}
