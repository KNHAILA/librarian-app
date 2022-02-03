package com.sorbonne.library.jaccardTreatments;

import java.io.File;

import static com.sorbonne.library.config.Constants.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JaccardGraph {

    private static Map<Integer, Set<Integer>> adjacents = new HashMap<>();

    public JaccardGraph(Map<Integer, Set<Integer>> adjacents) {
    }


    public static JaccardGraph createIndexGraph() {
        File folder = new File (ABSOLUTE_PATH+INDEXED_MAP_BOOKS);
        for (final File indexBook : folder.listFiles()) {
            int id = Integer.parseInt(indexBook.getName().replace(MAP_EXTENSION,""));
            adjacents.put(id, new HashSet<>());
        }
        return new JaccardGraph(adjacents);
    }

    public void addEdge(int i, int j) {
        adjacents.get(i).add(j);
        adjacents.get(j).add(i);
    }

    public int degree(int n) {
        return adjacents.get(n).size();
    }


    public Set<Integer> neighbors(int n){
        return adjacents.get(n);
    }

    public int size() {
        return adjacents.size();
    }

    public Map<Integer, Set<Integer>> getAdjacents(){
        return adjacents;
    }

}
