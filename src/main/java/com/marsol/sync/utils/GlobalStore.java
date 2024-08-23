package com.marsol.sync.utils;

import com.marsol.sync.model.Scale;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

public class GlobalStore {
    private static final GlobalStore INSTANCE = new GlobalStore();

    private final PriorityQueue<Scale> scalesQueue = new PriorityQueue<>((s1, s2)->s1.getLastUpdateDateTime().compareTo(s2.getLastUpdateDateTime()));
    private final HashMap<Integer, LocalDateTime> scaleMap = new HashMap<>(); //Mapa para verificar duplicados

    private GlobalStore() {

    }

    public static GlobalStore getInstance() {
        return INSTANCE;
    }

    public PriorityQueue<Scale> getScalesQueue() {
        return scalesQueue;
    }

    public HashMap<Integer, LocalDateTime> getScaleMap() {
        return scaleMap;
    }
}
