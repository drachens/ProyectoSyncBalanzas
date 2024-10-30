package com.marsol.sync.utils;

import com.marsol.sync.model.Scale;

import java.time.LocalDateTime;
import java.util.*;

public class GlobalStore {
    private static final GlobalStore INSTANCE = new GlobalStore();

    private final PriorityQueue<Scale> scalesQueue = new PriorityQueue<>((s1, s2)->s1.getLastUpdateDateTime().compareTo(s2.getLastUpdateDateTime()));
    private final HashMap<Integer, LocalDateTime> scaleMap = new HashMap<>(); //Mapa para verificar duplicados
    private final HashSet<Integer> scaleSet = new HashSet<>();
    private final Queue<Scale> scalesQueueCargaMaestra = new ArrayDeque<>();

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
}

/*
1- Validar que el cambio de los productos en balanza HPRT se haga en "simultaneo" con los otro equipos.
2- Validacion de información impresa de las balanzas (Etiqueta; Comparacion campos etiqueta)
3- Validación de carga forzada
 */