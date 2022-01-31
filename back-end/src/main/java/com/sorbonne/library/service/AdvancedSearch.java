package com.sorbonne.library.service;

import com.sorbonne.library.model.Book;

import java.util.List;

public interface AdvancedSearch {
    List<Book> searchBooksByRegex(String regex);
}
