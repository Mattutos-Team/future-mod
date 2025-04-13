package com.mattutos.arkfuture.core.energy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AFEnergyStorageTest {

    public static Stream<Arguments> provideExtractEnergy() {
        return Stream.of(
                Arguments.of(5_000, 500, true, 500, 0),
                Arguments.of(5_000, 1_000, true, 1_000, 0),
                Arguments.of(5_000, 2_000, true, 1_000, 0),
                Arguments.of(5_000, 500, false, 500, 500),
                Arguments.of(5_000, 1_000, false, 1_000, 1_000),
                Arguments.of(5_000, 2_000, false, 1_000, 1_000)
        );
    }

    @ParameterizedTest
    @MethodSource("provideExtractEnergy")
    void extractEnergy(int initialEnergy, int expectedExtractedEnergy, boolean simulate, int simulateExtractedEnergy, int realExtractedEnergy) {
        // Arrange
        AFEnergyStorage energyStorage = new AFEnergyStorage(10_000, 1_000, 1_000, initialEnergy);

        // Act
        int extractedEnergy = energyStorage.extractEnergy(expectedExtractedEnergy, simulate);

        // Assert
        assertEquals(simulateExtractedEnergy, extractedEnergy);
        assertEquals(realExtractedEnergy, (initialEnergy - energyStorage.getEnergyStored()));
    }

    @Test
    void receiveEnergy() {
        // Arrange
        AFEnergyStorage energyStorage = new AFEnergyStorage(10_000, 1_000, 1_000, 5_000);
        int energyToReceive = 4_000;

        // Act
        int receivedEnergy = energyStorage.receiveEnergy(energyToReceive, false);

        // Assert
        assertEquals(1_000, receivedEnergy);
        assertEquals(6_000, energyStorage.getEnergyStored());
    }

    @Test
    void setEnergy() {
        // Arrange
        AFEnergyStorage energyStorage = new AFEnergyStorage(1000);
        int expectedEnergy = 500;

        // Act
        energyStorage.setEnergy(expectedEnergy);

        // Assert
        assertEquals(expectedEnergy, energyStorage.getEnergyStored());
    }

    @Test
    void setCapacity() {
        // Arrange
        AFEnergyStorage energyStorage = new AFEnergyStorage(1000);
        int expectedCapacity = 2000;

        // Act
        energyStorage.setCapacity(expectedCapacity);

        // Assert
        assertEquals(expectedCapacity, energyStorage.getMaxEnergyStored());
    }

    @Test
    void extractMaxEnergy() {
        // Arrange
        AFEnergyStorage energyStorage = new AFEnergyStorage(10_000, 1_000, 1_000, 5_000);

        // Act
        int extractedEnergy = energyStorage.extractMaxEnergy(false);

        // Assert
        assertEquals(1_000, extractedEnergy);
        assertEquals(4_000, energyStorage.getEnergyStored());
    }

    @Test
    void receiveMaxEnergy() {
        // Arrange
        AFEnergyStorage energyStorage = new AFEnergyStorage(10_000, 1_000, 1_000, 5_000);

        // Act
        int receivedEnergy = energyStorage.receiveMaxEnergy(false);

        // Assert
        assertEquals(1_000, receivedEnergy);
        assertEquals(6_000, energyStorage.getEnergyStored());
    }

    public static Stream<Arguments> provideForceExtractEnergy() {
        return Stream.of(
                Arguments.of(5_000, 500, true, 500, 0),
                Arguments.of(5_000, 1_000, true, 1_000, 0),
                Arguments.of(5_000, 2_000, true, 2_000, 0),
                Arguments.of(5_000, 5_000, true, 5_000, 0),
                Arguments.of(5_000, 8_000, true, 5_000, 0),
                Arguments.of(5_000, 500, false, 500, 500),
                Arguments.of(5_000, 1_000, false, 1_000, 1_000),
                Arguments.of(5_000, 2_000, false, 2_000, 2_000),
                Arguments.of(5_000, 5_000, false, 5_000, 5_000),
                Arguments.of(5_000, 8_000, false, 5_000, 5_000)
        );
    }

    @ParameterizedTest
    @MethodSource("provideForceExtractEnergy")
    void forceExtractEnergy(int initialEnergy, int expectedExtractedEnergy, boolean simulate, int simulateExtractedEnergy, int realExtractedEnergy) {
        // Arrange
        AFEnergyStorage energyStorage = new AFEnergyStorage(10_000, 1_000, 1_000, initialEnergy);

        // Act
        int extractedEnergy = energyStorage.forceExtractEnergy(expectedExtractedEnergy, simulate);

        // Assert
        assertEquals(simulateExtractedEnergy, extractedEnergy);
        assertEquals(realExtractedEnergy, (initialEnergy - energyStorage.getEnergyStored()));
    }

    @Test
    void forceReceiveEnergy() {
        // Arrange
        AFEnergyStorage energyStorage = new AFEnergyStorage(10_000, 1_000, 1_000, 5_000);
        int expectedEnergy = 3_000;

        // Act
        int receivedEnergy = energyStorage.forceReceiveEnergy(expectedEnergy, false);

        // Assert
        assertEquals(3_000, receivedEnergy);
        assertEquals(8_000, energyStorage.getEnergyStored());
    }

    @Test
    void sendMaxEnergyTo() {
        // Arrange
        AFEnergyStorage energyStorage = new AFEnergyStorage(10_000, 1_000, 1_000, 5_000);
        AFEnergyStorage targetEnergyStorage = new AFEnergyStorage(10_000, 1_000, 1_000, 0);

        // Act
        energyStorage.sendMaxEnergyTo(targetEnergyStorage);

        // Assert
        assertEquals(4_000, energyStorage.getEnergyStored());
        assertEquals(1_000, targetEnergyStorage.getEnergyStored());
    }

    @Test
    void sendEnergyTo() {
        // Arrange
        AFEnergyStorage energyStorage = new AFEnergyStorage(10_000, 1_000, 1_000, 5_000);
        AFEnergyStorage targetEnergyStorage = new AFEnergyStorage(10_000, 1_000, 1_000, 0);
        int expectedEnergy = 500;

        // Act
        energyStorage.sendEnergyTo(targetEnergyStorage, expectedEnergy);

        // Assert
        assertEquals(4_500, energyStorage.getEnergyStored());
        assertEquals(500, targetEnergyStorage.getEnergyStored());
    }

    @Test
    void receiveMaxEnergyFrom() {
        // Arrange
        AFEnergyStorage energyStorage = new AFEnergyStorage(10_000, 1_000, 1_000, 5_000);
        AFEnergyStorage sourceEnergyStorage = new AFEnergyStorage(10_000, 1_000, 1_000, 5_000);

        // Act
        energyStorage.receiveMaxEnergyFrom(sourceEnergyStorage);

        // Assert
        assertEquals(6_000, energyStorage.getEnergyStored());
        assertEquals(4_000, sourceEnergyStorage.getEnergyStored());
    }

    @Test
    void receiveEnergyFrom() {
        // Arrange
        AFEnergyStorage energyStorage = new AFEnergyStorage(10_000, 1_000, 1_000, 5_000);
        AFEnergyStorage sourceEnergyStorage = new AFEnergyStorage(10_000, 1_000, 1_000, 5_000);
        int expectedEnergy = 500;

        // Act
        energyStorage.receiveEnergyFrom(sourceEnergyStorage, expectedEnergy);

        // Assert
        assertEquals(5_500, energyStorage.getEnergyStored());
        assertEquals(4_500, sourceEnergyStorage.getEnergyStored());
    }
}