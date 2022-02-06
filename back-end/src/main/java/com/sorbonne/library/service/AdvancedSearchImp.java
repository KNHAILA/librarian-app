package com.sorbonne.library.service;


import com.sorbonne.library.model.Book;
import com.sorbonne.library.model.Result;
import com.sorbonne.library.regexTreatments.DFA;
import com.sorbonne.library.regexTreatments.NFA;
import com.sorbonne.library.regexTreatments.RegEx;
import com.sorbonne.library.regexTreatments.RegExTree;
import com.sorbonne.library.utils.BinarySerialization;
import com.sorbonne.library.utils.BooksInformation;
import com.sorbonne.library.utils.SortingTreatments;
import com.sorbonne.library.utils.SuggestionTreatments;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.sorbonne.library.config.Constants.*;

@Service
@Slf4j
public class AdvancedSearchImp implements AdvancedSearch{

    @Override
    public Result searchBooksByRegex(String regex){
        Result result= new Result();
        HashMap<Integer,Pair<Integer,Integer>> books = new HashMap<>();
        try {
            regex = regex.toLowerCase();
            RegEx.initializeRegEx();
            RegExTree ret = RegEx.parse(regex);
            RegEx.calculateFinalStateNumbers(ret);
            NFA nfa= new NFA();
            nfa.buildNfaMatrix(ret);
            DFA dfa = new DFA();
            ArrayList<int[][][]> output = dfa.makeDFA(nfa.matASCI, nfa.matInitTermEps);
            int[][] s=dfa.simplifyNDFA(output);

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
                                if(RegEx.find(word+" ",s, dfa.acceptArray)){
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
        booksFound.addAll(SortingTreatments.sortedBooksFromKeywords(BooksInformation.getBooksInfo(books)).keySet());

        result.setResult(booksFound);
        List<Book> suggestions = BooksInformation.getBooksInfoById(SuggestionTreatments.suggestion(books.keySet()));
        suggestions.removeAll(booksFound);
        result.setSuggestion(suggestions);


        return result;
    }
}