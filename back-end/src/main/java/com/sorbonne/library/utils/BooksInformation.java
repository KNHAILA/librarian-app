package com.sorbonne.library.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sorbonne.library.model.Book;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

import static com.sorbonne.library.config.Constants.ABSOLUTE_PATH;
import static com.sorbonne.library.config.Constants.CONFIG;

public class BooksInformation {
    public static List<Book> getBooksInfoById(Set<Integer> booksId) {
        List<Book> booksSearched = new ArrayList<>();
        Type BOOK_TYPE = new TypeToken<List<Book>>() {
        }.getType();
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(ABSOLUTE_PATH + CONFIG));
            List<Book> books = gson.fromJson(reader, BOOK_TYPE);
            for(Book book: books)
            {
                if(booksId.contains(book.getId()))
                    booksSearched.add(book);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return booksSearched;
    }

    public static HashMap<Book, Pair<Integer,Integer>> getBooksInfo(HashMap<Integer,Pair<Integer,Integer>> booksId) {
        HashMap<Book,Pair<Integer,Integer>> booksSearched = new HashMap<>();
        Type BOOK_TYPE = new TypeToken<List<Book>>() {}.getType();
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(ABSOLUTE_PATH + CONFIG));
            List<Book> books = gson.fromJson(reader, BOOK_TYPE);
            for(Book book: books)
            {
                if(booksId.containsKey(book.getId()))
                    booksSearched.put(book,booksId.get(book.getId()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return booksSearched;
    }

    public static HashMap<Book, Integer> getBooksInfo(Map<Integer, Integer> booksId) {
        HashMap<Book, Integer> booksSearched = new HashMap<>();
        Type BOOK_TYPE = new TypeToken<List<Book>>() {
        }.getType();
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(ABSOLUTE_PATH + CONFIG));
            List<Book> books = gson.fromJson(reader, BOOK_TYPE);
            for(Book book: books)
            {
                if(booksId.containsKey(book.getId()))
                    booksSearched.put(book,booksId.get(book.getId()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return booksSearched;
    }
}
