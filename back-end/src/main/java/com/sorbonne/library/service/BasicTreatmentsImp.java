package com.sorbonne.library.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sorbonne.library.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.sorbonne.library.config.Constants.ABSOLUTE_PATH;

@Service
@Slf4j
public class BasicTreatmentsImp implements  BasicTreatments{
    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        Type BOOK_TYPE = new TypeToken<List<Book>>() {}.getType();
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(ABSOLUTE_PATH + "config/info.json"));
            books = gson.fromJson(reader, BOOK_TYPE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return books;
    }
}
