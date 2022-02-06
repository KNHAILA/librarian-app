package com.sorbonne.library.service;

import com.sorbonne.library.model.Book;
import com.sorbonne.library.model.Result;

import java.util.List;

public interface ClassicSearch {
    Result classicSearch(String word);
}
