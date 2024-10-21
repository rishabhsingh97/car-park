package com.rsuniverse.car_park.repo;

import com.rsuniverse.car_park.models.entities.Slot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SlotRepoTestTest {

    private SlotRepo slotRepo;

    @BeforeEach
    void setUp() {
        // Use getInstance() to ensure we're testing the singleton pattern.
        slotRepo = SlotRepo.getInstance();

        // Clear the slotMap before each test to ensure no state persists between tests
        slotRepo.getSlotMap().clear();
    }

    @Test
    void testAddSlot_Success() {
        // Setup
        Slot slot = Slot.builder().id(1).build();

        // Execute
        slotRepo.addSlot(slot);

        // Verify
        Map<Integer, Slot> slotMap = slotRepo.getSlotMap();
        assertNotNull(slotMap);
        assertEquals(1, slotMap.size());
        assertTrue(slotRepo.containsSlot(1));
    }

    @Test
    void testGetSlotById_ExistingSlot() {
        // Setup
        Slot slot = Slot.builder().id(1).build();
        slotRepo.addSlot(slot);

        // Execute
        Slot retrievedSlot = slotRepo.getSlotById(1);

        // Verify
        assertNotNull(retrievedSlot);
        assertEquals(1, retrievedSlot.getId());
    }

    @Test
    void testGetSlotById_NonExistingSlot() {
        // Execute
        Slot retrievedSlot = slotRepo.getSlotById(99);

        // Verify
        assertNull(retrievedSlot);
    }

    @Test
    void testContainsSlot_ExistingSlot() {
        // Setup
        Slot slot = Slot.builder().id(1).build();
        slotRepo.addSlot(slot);

        // Execute & Verify
        assertTrue(slotRepo.containsSlot(1));
    }

    @Test
    void testContainsSlot_NonExistingSlot() {
        // Execute & Verify
        assertFalse(slotRepo.containsSlot(99));
    }
}
