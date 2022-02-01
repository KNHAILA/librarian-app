package com.sorbonne.library.controller;


import com.sorbonne.library.model.Book;
import com.sorbonne.library.service.BasicTreatments;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/basic")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class BasicController {

    @Autowired
    private BasicTreatments basicTreatments;

    @ApiOperation(value = "Returns All the books")
    @GetMapping("/list")
    public List<Book> getAllBooks()
    {
        return basicTreatments.getAllBooks();
    }
}
