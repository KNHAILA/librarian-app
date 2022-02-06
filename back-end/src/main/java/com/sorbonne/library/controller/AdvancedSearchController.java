package com.sorbonne.library.controller;


import com.sorbonne.library.model.Book;
import com.sorbonne.library.model.Result;
import com.sorbonne.library.service.AdvancedSearch;
import com.sorbonne.library.service.ClassicSearch;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/advancedSearch")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class AdvancedSearchController {

    @Autowired
    private AdvancedSearch advancedSearch;

    @ApiOperation(value = "Returns the books that contains words that match the regex")
    @GetMapping("/regexSearch")
    public Result regexSearch(@RequestParam String name){
        return advancedSearch.searchBooksByRegex(name);
    }
}
