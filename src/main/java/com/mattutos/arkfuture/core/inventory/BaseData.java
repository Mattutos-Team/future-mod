package com.mattutos.arkfuture.core.inventory;

import net.minecraft.world.inventory.ContainerData;

import java.util.List;

public interface BaseData<T extends Enum<T>> {

    int getDataPack();

    List<? extends BaseData<?>> getAllValues();

    default void setValueContainerData(ContainerData containerData, long value) {
        var startedPosition = this.getPositionData();

        for (int i = (getDataPack() - 1); i >= 0; i--) {
            short valueToSave = (short) (value & 0xFFFF);
            value >>= 16;
            containerData.set(startedPosition + i, valueToSave);
        }
    }

    default long getValueContainerData(ContainerData containerData) {
        var startedPosition = this.getPositionData();
        long value = 0;

        for (int i = (getDataPack() - 1); i >= 0; i--) {
            value <<= 16;
            value += containerData.get(startedPosition + i);
        }

        return value;
    }

    default int getPositionData() {
        int position = 0;
        for (BaseData<?> data : getAllValues()) {
            if (this == data) {
                return position;
            }
            position += data.getDataPack();
        }
        return -1;
    }

}
