package com.sorbonne.library.config;


import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
public class Constants {

    private Constants() {
    }

    public static final String ABSOLUTE_PATH =  Paths.get("").toAbsolutePath()+
            "/src/main/java/com/sorbonne/library/";
    public static final String BOOKS= "Books/";
    public static final String TXT_EXTENSION = ".txt";
    public static final String INDEXED_BOOKS = "IndexedBooks/";
    public static final String DEX_EXTENSION = ".dex";
    public static final String INDEXED_MAP_BOOKS = "indexedMapBooks/";
    public static final String MAP_EXTENSION = ".map";
    public static String GUTENDEX_LINK = "http://gutendex.com/books/";
    public static String GUTENBERG_LINK = "http://www.gutenberg.org/files/";

}
