package com.sorbonne.library.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sorbonne.library.model.Book;
import com.sorbonne.library.utils.BinarySerialization;
import com.sorbonne.library.utils.Treatments;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.sorbonne.library.config.Constants.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@Service
@Slf4j
public class ClassicSearchImp implements ClassicSearch {

    @Override
    public List<Book> classicSearch(String word) {
        HashMap<Integer, Integer> books = new HashMap<>();
        File folder = new File(ABSOLUTE_PATH + INDEXED_MAP_BOOKS);
        for (final File indexBook : folder.listFiles()) {
            int id = Integer.parseInt(indexBook.getName().replace(".map", "")); //Id of the book
            HashMap<String, Integer> bookWords = null;
            try {
                bookWords = BinarySerialization.loadBookIndexation(indexBook);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bookWords.containsKey(word)) {
                books.put(id, bookWords.get(word));
            }
        }
        List<Book> booksFound = new ArrayList<>();
        booksFound.addAll(Treatments.SortedMapDescending(getBooksInfo(books)).keySet());
        return booksFound;
    }

    public static HashMap<Book, Integer> getBooksInfo(Map<Integer, Integer>  booksId) {
        HashMap<Book, Integer> booksSearched = new HashMap<>();
        Type BOOK_TYPE = new TypeToken<List<Book>>() {
        }.getType();
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(ABSOLUTE_PATH + "config/info.json"));
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

