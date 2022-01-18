package com.sorbonne.library.utils;

import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class BooksIndexation {
    private static String absolutePath = Paths.get("").toAbsolutePath()+
            "/src/main/java/com/sorbonne/library/";

    public static void main(String[] args) throws Exception {
        indexBooksDatabase();
    }

    public static void indexBooksDatabase() throws Exception {
        File dataBaseFolder = new File (absolutePath+"Books");
        int i = 1;
        for (final File book : dataBaseFolder.listFiles()) {
            System.out.println("Indexation en cours : "+i+"/"+dataBaseFolder.listFiles().length);
            i++;
            int id = Integer.parseInt(book.getName().replace(".txt",""));
                indexBook(id);
        }
    }

    public static void indexBook(int id) throws IOException {
        Map<String, Pair<Integer, Integer>> index = new HashMap<String,Pair<Integer, Integer>>();
        File bookFile = new File(absolutePath+"Books/"+id+".txt");
        Scanner book = new Scanner(bookFile);
        while (book.hasNext()) {
            String word =book.next().replaceAll("\\p{Punct}", "")
                    .replaceAll("[\"\']", "").toLowerCase();
            if(word.isEmpty() || word.length()<3)
                continue;
            Pair<Integer, Integer> wordCurrentOccurrence;
            if(index.containsKey(word)){
                Pair<Integer, Integer> wordOccurrence = index.get(word);
                int currentOccurrence= wordOccurrence.getValue()+1;
                wordCurrentOccurrence = new Pair(id, currentOccurrence);
            }
            else {
                wordCurrentOccurrence = new Pair(id, 1);
            }
            index.put(word,wordCurrentOccurrence);
        }
        book.close();
        writeIntoFile(index,id);
    }

    private static void writeIntoFile(Map<String,Pair<Integer, Integer>> index, int id)
            throws IOException
    {
        FileWriter writer = new FileWriter(absolutePath+"IndexedBooks/"+id+".dex");
        index.forEach((key,value) -> {
            try {
                writer.write(key + " : [" + id + " : " + value.getValue() + "]" + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writer.close();
    }
}
