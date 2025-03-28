package com.mattutos.arkfuture.init;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockInitTest {

    @Test
    void register() {
        var block = BlockInit.COAL_POWER_GENERATOR;
        assertNotNull(block);
        assertNotNull(block.get());
    }
}