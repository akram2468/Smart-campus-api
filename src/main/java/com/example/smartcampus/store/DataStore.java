package com.example.smartcampus.store;

import com.example.smartcampus.model.Room;
import com.example.smartcampus.model.Sensor;
import com.example.smartcampus.model.SensorReading;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    public static final Map<String, Room> rooms = new ConcurrentHashMap<>();
    public static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    public static final Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();

    private DataStore() {
    }
}