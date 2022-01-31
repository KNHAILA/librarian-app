package com.sorbonne.library.service;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sorbonne.library.model.Book;
import com.sorbonne.library.regexTreatments.DFA;
import com.sorbonne.library.regexTreatments.NFA;
import com.sorbonne.library.regexTreatments.RegEx;
import com.sorbonne.library.regexTreatments.RegExTree;
import com.sorbonne.library.utils.BinarySerialization;
import com.sorbonne.library.utils.Treatments;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.sorbonne.library.config.Constants.*;

@Service
@Slf4j
public class AdvancedSearchImp implements AdvancedSearch{

    @Override
    public List<Book> searchBooksByRegex(String regex){
        HashMap<Integer,Pair<Integer,Integer>> books = new HashMap<>();
        try {
            regex = regex.toLowerCase();
            RegExTree ret = RegEx.parse(regex);
            RegEx.calculateFinalStateNumbers(ret);
            NFA.buildNfaMatrix(ret);
            ArrayList<int[][][]> output = DFA.makeDFA(NFA.matASCI, NFA.matInitTermEps);
            int[][] s=DFA.simplifyNDFA(output);

            List<Thread> threads = new ArrayList<>();
            File folder = new File(ABSOLUTE_PATH+INDEXED_MAP_BOOKS);
            for (final File indexBook : folder.listFiles()) {
                int id = Integer.parseInt(indexBook.getName().replace(".map",""));

                threads.add(new Thread( new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String,Integer> bookWords = null;
                        int match = 0;
                        int occ = 0;
                        try {
                            bookWords = BinarySerialization.loadBookIndexation(indexBook);
                            for(String word : bookWords.keySet()){
                                if(RegEx.find(word+" ",s, DFA.acceptArray)){
                                    match ++;
                                    occ += bookWords.get(word);
                                }
                            }
                            if(match > 0){
                                books.put(id, new Pair<>(match, occ));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }));
            }
            for(Thread t : threads){
                t.start();
            }

            for(Thread t : threads){
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Book> booksFound = new ArrayList<>();
        booksFound.addAll(Treatments.sortedBooksFromKeywords(getBooksInfo(books)).keySet());
        return booksFound;
    }


    public static HashMap<Book,Pair<Integer,Integer>> getBooksInfo(HashMap<Integer,Pair<Integer,Integer>> booksId) {
        HashMap<Book,Pair<Integer,Integer>> booksSearched = new HashMap<>();
        Type BOOK_TYPE = new TypeToken<List<Book>>() {}.getType();
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