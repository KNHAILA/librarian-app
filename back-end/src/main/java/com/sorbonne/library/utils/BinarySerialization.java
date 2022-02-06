package com.sorbonne.library.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.sorbonne.library.jaccardTreatments.JaccardGraph;
import com.sorbonne.library.jaccardTreatments.JaccardMatrix;

import static com.sorbonne.library.config.Constants.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class BinarySerialization {

    public static void registerBooksMap(HashMap<String,Integer> indexedBook, int id) throws IOException {
        String indexedMapBookFile = ABSOLUTE_PATH+INDEXED_MAP_BOOKS+id+MAP_EXTENSION;
        Kryo kryo = new Kryo();
        kryo.register(HashMap.class, new MapSerializer());
        Output output = new Output(new FileOutputStream(indexedMapBookFile));
        kryo.writeClassAndObject(output, indexedBook);
        output.close();
    }

    public static HashMap<String,Integer> loadBookIndexation(File file) throws IOException {
        Kryo kryo = new Kryo();
        kryo.register(HashMap.class, new MapSerializer());
        Input input = new Input(new FileInputStream(file));
        HashMap<String,Integer> bookIndexation = (HashMap<String,Integer>) kryo.readClassAndObject(input);
        input.close();
        return bookIndexation;
    }

    public static void storeMatrix(JaccardMatrix matrice) throws IOException {
        String matrixFile = ABSOLUTE_PATH+JACCARD+MATRIX+TXT_EXTENSION;
        Kryo kryo = new Kryo();
        kryo.register(HashMap.class, new MapSerializer());
        Output output = new Output(new FileOutputStream(matrixFile));
        kryo.writeClassAndObject(output, matrice.matrix);
        output.close();
    }

    public static HashMap<Integer,HashMap<Integer,Double>> loadMatrix(File file) throws IOException {
        Kryo kryo = new Kryo();
        kryo.register(HashMap.class, new MapSerializer());
        Input input = new Input(new FileInputStream(file));
        HashMap<Integer,HashMap<Integer,Double>> res = (HashMap<Integer,HashMap<Integer,Double>>) kryo.readClassAndObject(input);
        input.close();
        return res;
    }


    public static void storeGraph(JaccardGraph graph) throws IOException {
        String graphFile = ABSOLUTE_PATH+JACCARD+GRAPH+TXT_EXTENSION;
        Kryo kryo = new Kryo();
        kryo.register(HashMap.class, new MapSerializer());
        kryo.register(HashSet.class);
        Output output = new Output(new FileOutputStream(graphFile));
        kryo.writeClassAndObject(output, graph.getAdjacents());
        output.close();
    }


    public static Map<Integer, Set<Integer>> loadGraph(File file) throws IOException {
        Kryo kryo = new Kryo();
        kryo.register(HashMap.class, new MapSerializer());
        kryo.register(HashSet.class);
        Input input = new Input(new FileInputStream(file));
        Map<Integer, Set<Integer>> res = (Map<Integer, Set<Integer>>) kryo.readClassAndObject(input);
        input.close();
        return res;
    }
}
