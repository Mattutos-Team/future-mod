package com.mattutos.arkfuture.core.inventory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleEnumContainerDataTest {

    @Test
    @DisplayName("Teste do SimpleEnumContainerData configurando e recuperando dados short, int e long")
    void testSetAndGet() {
        SimpleEnumContainerData<EnumTest> containerData = new SimpleEnumContainerData<>(EnumTest.class);

        containerData.set(EnumTest.SHORT, 0xffff);
        containerData.set(EnumTest.INT, 0xffff_ffffL);
        containerData.set(EnumTest.LONG, 0xffff_ffff_ffff_ffffL);

        assertEquals((short)0xffff, (short)containerData.get(EnumTest.SHORT));
        assertEquals(0xffff_ffff, (int)containerData.get(EnumTest.INT));
        assertEquals(0xffff_ffff_ffff_ffffL, containerData.get(EnumTest.LONG));
    }

    @Test
    @DisplayName("Validando count automatico da classe abstrata")
    void testCount() {
        SimpleEnumContainerData<EnumTest> containerData = new SimpleEnumContainerData<>(EnumTest.class);

        assertEquals(7, containerData.getCount());
    }

    @Test
    @DisplayName("Validando valor de cada parte do enum")
    void testGetValuePart() {
        SimpleEnumContainerData<EnumTest> containerData = new SimpleEnumContainerData<>(EnumTest.class);

        containerData.set(EnumTest.SHORT, 0x0001);
        containerData.set(EnumTest.INT, 0x0020_0010L);
        containerData.set(EnumTest.LONG, 0x0400_0300_0200_0100L);

        assertEquals(0x0001, containerData.get(0)); // valor de short

        assertEquals(0x0020, containerData.get(1)); // segundo word de int
        assertEquals(0x0010, containerData.get(2)); // primeiro word de int

        assertEquals(0x0400, containerData.get(3)); // quarto word de long
        assertEquals(0x0300, containerData.get(4)); // terceiro word de long
        assertEquals(0x0200, containerData.get(5)); // segundo word de long
        assertEquals(0x0100, containerData.get(6)); // primeiro word de long
    }

}