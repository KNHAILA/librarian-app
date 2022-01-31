package com.sorbonne.library.utils;

import com.sorbonne.library.model.Book;
import javafx.util.Pair;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Treatments {

    public static LinkedHashMap<Book, Integer> SortedMapDescending(Map<Book, Integer> books){
       LinkedHashMap<Book, Integer> reverseSortedBooks = new LinkedHashMap<>();
        books.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedBooks.put(x.getKey(), x.getValue()));
        return reverseSortedBooks;
    }

    public static LinkedHashMap<Book, Pair<Integer, Integer>> sortedBooksFromKeywords(HashMap<Book,Pair<Integer,Integer>> books){
        //LinkedHashMap : Préserver l'ordre des élémenents
        LinkedHashMap<Book, Pair<Integer, Integer>> sortedMap;

        //Use Comparator.reverseOrder() for reverse ordering
        sortedMap = books.entrySet()
                .stream()
                .sorted((b1,b2) -> compare(b1.getValue(),b2.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return sortedMap;
    }

    private static int compare(Pair<Integer,Integer> p1, Pair<Integer,Integer> p2) {
        return p1.getKey() == p2.getKey()? p2.getValue() - p1.getValue() : p2.getKey() - p1.getKey();
    }

}
