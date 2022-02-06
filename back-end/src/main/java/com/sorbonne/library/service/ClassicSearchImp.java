package com.sorbonne.library.service;

import com.sorbonne.library.jaccardTreatments.JaccardAlgorithms;
import com.sorbonne.library.model.Book;
import com.sorbonne.library.model.Result;
import com.sorbonne.library.utils.BinarySerialization;
import com.sorbonne.library.utils.BooksInformation;
import com.sorbonne.library.utils.SortingTreatments;
import com.sorbonne.library.utils.SuggestionTreatments;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.sorbonne.library.config.Constants.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class ClassicSearchImp implements ClassicSearch {

    @Override
    public Result classicSearch(String word) {
        HashMap<Integer, Integer> books = new HashMap<>();
        Result result= new Result();
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
        booksFound.addAll(SortingTreatments.SortedMapDescending(BooksInformation.getBooksInfo(books)).keySet());
        result.setResult(booksFound);
        List<Book> suggestions = BooksInformation.getBooksInfoById(SuggestionTreatments.suggestion(books.keySet()));
        suggestions.removeAll(booksFound);
        result.setSuggestion(suggestions);
        return result;
    }

    public static Map<Integer,Double> classement(Map<Integer,Integer> books) throws Exception {
        return JaccardAlgorithms.closenessCentrality(books.keySet());
    }
}

