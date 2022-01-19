package com.sorbonne.library.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import static com.sorbonne.library.config.Constants.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;


public class BinarySerialization {

    public static void registerBooksMap(HashMap<String,Integer> indexedBook, int id) throws IOException {
        String indexedMapBookFile = ABSOLUTE_PATH+INDEXED_MAP_BOOKS+id+MAP_EXTENSION;
        Kryo kryo = new Kryo();
        kryo.register(HashMap.class, new MapSerializer());
        Output output = new Output(new FileOutputStream(indexedMapBookFile));
        kryo.writeClassAndObject(output, indexedBook);
        output.close();
    }

    public static HashMap<String,Integer> loadBookIndexation(String file) throws IOException {
        Kryo kryo = new Kryo();
        kryo.register(HashMap.class, new MapSerializer());
        Input input = new Input(new FileInputStream(file));
        HashMap<String,Integer> bookIndexation = (HashMap<String,Integer>) kryo.readClassAndObject(input);
        input.close();
        return bookIndexation;
    }
}
