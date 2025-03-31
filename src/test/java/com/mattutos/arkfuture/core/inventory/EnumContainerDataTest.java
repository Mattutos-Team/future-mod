package com.mattutos.arkfuture.core.inventory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EnumContainerDataTest {

    long energiaTest = 100_000;

    @Test
    @DisplayName("Teste do EnumContainerData configurando e recuperando dados short, int e long")
    void testSetAndGet() {
        Map<String, Long> mapTest = new HashMap<>();
        EnumContainerData<EnumTest> enumContainerData = new EnumContainerData<>(EnumTest.class) {
            @Override
            public void set(EnumTest enumData, long value) {
                switch (enumData) {
                    case SHORT -> mapTest.put("SHORT", value);
                    case INT -> mapTest.put("INT", value);
                    case LONG -> energiaTest = value;
                }
            }

            @Override
            public long get(EnumTest enumData) {
                return switch (enumData) {
                    case SHORT -> mapTest.get(enumData.name());
                    case INT -> 0L;
                    case LONG -> energiaTest;
                };
            }
        };

        assertNull(mapTest.get(EnumTest.SHORT.name()));
        assertEquals(0, enumContainerData.get(EnumTest.INT));
        assertEquals(100_000, energiaTest);

        enumContainerData.set(EnumTest.SHORT, 0xffff);
        enumContainerData.set(EnumTest.INT, 0xffff_ffffL);
        enumContainerData.set(EnumTest.LONG, 123);

        assertEquals(0xffff, mapTest.get(EnumTest.SHORT.name()));
        assertEquals(0, enumContainerData.get(EnumTest.INT));
        assertEquals(123, energiaTest);
    }

}