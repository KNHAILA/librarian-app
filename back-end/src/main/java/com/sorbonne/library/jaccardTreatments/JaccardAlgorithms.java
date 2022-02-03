package com.sorbonne.library.jaccardTreatments;


import com.sorbonne.library.utils.BinarySerialization;

import static com.sorbonne.library.config.Constants.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;



public class JaccardAlgorithms {

    public static void main(String[] args) throws Exception {

        createMatrixJaccardToFile();

        HashMap<Integer, HashMap<Integer, Double>> matrix = BinarySerialization.loadMatrix(
                new File(ABSOLUTE_PATH+JACCARD+MATRIX+TXT_EXTENSION));

        JaccardGraph g = createVertexForAllIndexBooks(0.7,matrix);
        BinarySerialization.storeGraph(g);



    }

    public static JaccardGraph createVertexForAllIndexBooks(double constanteJaccard,
                                                     HashMap<Integer, HashMap<Integer, Double>> cache) throws Exception {
        JaccardGraph graph = JaccardGraph.createIndexGraph();

        for(Map.Entry<Integer,HashMap<Integer,Double>> key: cache.entrySet()){
            for(Map.Entry<Integer,Double> value: key.getValue().entrySet()) {
                if (key.getKey() != value.getKey() && value.getValue() < constanteJaccard) {
                    graph.addEdge(key.getKey(), value.getKey());
                }
            }
        }
        return graph;
    }


    private static double closenessCentrality(Map<Integer,Set<Integer>> mongraph,
                                              HashMap<Integer, HashMap<Integer, Double>> matrix,
                                              int noeud)
             {
        double sumOfDist =0.0;
        for(int sommet : mongraph.keySet()){
            if(!mongraph.get(sommet).isEmpty())
                sumOfDist=matrix.get(noeud).get(sommet);
        }
        return 1/sumOfDist;
    }


    public static  LinkedHashMap<Integer,Double> closenessCentrality(Set<Integer> books) throws Exception {

        LinkedHashMap<Integer,Double> ranks = new LinkedHashMap<Integer, Double>();
        Map<Integer,Set<Integer>> graph = BinarySerialization.loadGraph(
                new File(ABSOLUTE_PATH+JACCARD+GRAPH+TXT_EXTENSION));
        HashMap<Integer, HashMap<Integer, Double>> matrixJaccard = JaccardMatrix.readMatrixFromFile();

        for(Integer book : books){
            double sumOfDist =closenessCentrality(graph,matrixJaccard,book);
            if (sumOfDist!=0)
                ranks.put(book,1/sumOfDist);
            else
                ranks.put(book,0.0);
        }

        LinkedHashMap<Integer, Double> reverseSortedMap = new LinkedHashMap<>();
        ranks.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
        return reverseSortedMap;
    }


    public static double distanceJaccard(Set<String> set1, Set<String>  set2) {
        Set<String> intersection = set1.parallelStream()
                .collect(Collectors.toSet());;
        intersection.retainAll(set2);
        double union = set1.size() + set2.size() - intersection.size();
        return (union - intersection.size()) / union;
    }


    public static double distanceJaccard(Map<String, Integer> f1, Map<String,Integer> f2) {
        ArrayList<String> monsetA= new ArrayList<String>();
        ArrayList<String> monsetB= new ArrayList<String>();
        for(Map.Entry<String,Integer> key : f1.entrySet()){
            for(int i=0;i<key.getValue();i++){
                monsetA.add(key.getKey());
            }
        }
        for(Map.Entry<String,Integer> key : f2.entrySet()){
            for(int i=0;i<key.getValue();i++){
                monsetB.add(key.getKey());
            }
        }
        ArrayList<String> intersection = (ArrayList<String>) monsetA.clone();
        intersection.retainAll(monsetB);

        double union = monsetA.size()+monsetB.size() - intersection.size();
        return (union-intersection.size())/union;
    }

    public static void createMatrixJaccardToFile() throws Exception {
        File folder = new File(ABSOLUTE_PATH+INDEXED_MAP_BOOKS);

        JaccardMatrix cache = new JaccardMatrix();

        for (final File f1 : folder.listFiles()) {
            int id = Integer.parseInt(f1.getName().replace(MAP_EXTENSION, ""));
            Map<String,Integer> map1= BinarySerialization.loadBookIndexation(f1);

            for (final File f2 : folder.listFiles()) {
                int id2 = Integer.parseInt(f2.getName().replace(MAP_EXTENSION, ""));
                Map<String,Integer> map2=BinarySerialization.loadBookIndexation(f2);
                if(cache.getElemMatrix(id,id2)== -1.0) {
                    double distanceJaccard = distanceJaccard(map1.keySet(),map2.keySet());
                    cache.addToMatrix(id,id2,distanceJaccard);
                }
            }
        }
        BinarySerialization.storeMatrix(cache);
    }
}
