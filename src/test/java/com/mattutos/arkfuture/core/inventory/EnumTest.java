package com.mattutos.arkfuture.core.inventory;

public enum EnumTest implements BaseData {
    SHORT(1),
    INT(2),
    LONG(4);

    private final int dataPack;

    EnumTest(int dataPack) {
        this.dataPack = dataPack;
    }

    @Override
    public int getDataPack() {
        return dataPack;
    }
}
