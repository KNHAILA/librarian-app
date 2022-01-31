package com.sorbonne.library.controller;

import com.sorbonne.library.model.Book;
import com.sorbonne.library.service.ClassicSearch;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classicSearch")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class ClassicSearchController {
    @Autowired
    private ClassicSearch classicSearch;

    @ApiOperation(value = "Returns the books that contains the word")
    @GetMapping("/list")
    public List<Book> search(@RequestParam String name) throws Exception {
       return classicSearch.classicSearch(name);
    }
}
