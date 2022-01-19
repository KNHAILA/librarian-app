package com.sorbonne.library.utils;


import java.io.FileNotFoundException;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sorbonne.library.config.Constants.*;

public class BooksMapIndexation {
    public static void main(String[] args) throws Exception {
        createIndexedMapBooksDataBase();
    }

    public static void createIndexedMapBooksDataBase() throws Exception {
        File indexedBooks = new File (ABSOLUTE_PATH+INDEXED_BOOKS);
        for (final File indexedBook : indexedBooks.listFiles()) {
            HashMap<String, Integer> indexMapBook = fileToMap(indexedBook);
            int id = Integer.parseInt(indexedBook.getName().replace(DEX_EXTENSION,""));
            BinarySerialization.registerBooksMap(indexMapBook,id);
        }
    }


    public static HashMap<String,Integer> fileToMap(File index) throws FileNotFoundException {
        HashMap<String,Integer> keywords = new HashMap();
        Pattern p = Pattern.compile("(.*) : \\[.* : (.*)\\]");
        Scanner text = new Scanner(index);
        while (text.hasNextLine()) {
            Matcher matcher = p.matcher(text.nextLine());
            if(matcher.find()){
                keywords.put(matcher.group(1),Integer.parseInt(matcher.group(2)));
            }
        }
        text.close();
        return keywords;
    }
}
