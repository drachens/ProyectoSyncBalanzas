package com.marsol.sync.utils;

import com.marsol.sync.domain.model.Scale;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class GlobalStore {
    private static final GlobalStore INSTANCE = new GlobalStore();

    private final PriorityQueue<Scale> scalesQueue = new PriorityQueue<>((s1, s2)->s1.getLastUpdateDateTime().compareTo(s2.getLastUpdateDateTime()));
    private final HashMap<Integer, LocalDateTime> scaleMap = new HashMap<>(); //Mapa para verificar duplicados
    private final HashSet<Integer> scaleSet = new HashSet<>();
    private final Queue<Scale> scalesQueueCargaMaestra = new ArrayDeque<>();

    /**
     * Nuevas estructuras de Colas Concurrentes, Mapa y Set concurrentes.
     */
    private final ConcurrentLinkedQueue<Scale> forcedScalesQueue = new ConcurrentLinkedQueue<>();
    private final PriorityBlockingQueue<Scale> priorityQueue = new PriorityBlockingQueue<>();
    private final ConcurrentHashMap<Integer, LocalDateTime> priorityMap = new ConcurrentHashMap<>();
    private final Set<Integer> forcedSet = Collections.newSetFromMap(new ConcurrentHashMap<>());


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

    public HashSet<Integer> getScaleSet() {return scaleSet;}

    public Queue<Scale> getScalesQueueCargaMaestra() {return scalesQueueCargaMaestra;}

    public PriorityBlockingQueue<Scale> getPriorityQueue() {return priorityQueue;}

    public ConcurrentLinkedQueue<Scale> getForcedScalesQueue() {return forcedScalesQueue;}

    public ConcurrentHashMap<Integer, LocalDateTime> getPriorityMap() {return priorityMap;}

    public Set<Integer> getForcedSet() {return forcedSet;}
}