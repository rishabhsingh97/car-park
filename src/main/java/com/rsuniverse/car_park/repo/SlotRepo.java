package com.rsuniverse.car_park.repo;

import java.util.HashMap;
import java.util.Map;

import com.rsuniverse.car_park.models.entities.Slot;

public class SlotRepo {
    private static final SlotRepo instance = new SlotRepo();
    private final Map<Integer, Slot> slotMap = new HashMap<>(); 

    private SlotRepo() {}

    public static SlotRepo getInstance() {
        return instance;
    }

    public Map<Integer, Slot> getSlotMap() {
        return slotMap;
    }

    public void addSlot(Slot slot) {
        slotMap.put(slot.getId(), slot); 
    }

    public Slot getSlotById(int id) {
        return slotMap.get(id); 
    }

    public boolean containsSlot(int id) {
        return slotMap.containsKey(id); 
    }
}
