package com.andrewsreis.dundermifflin.controllers;

import com.andrewsreis.dundermifflin.cache.entities.Quote;
import com.andrewsreis.dundermifflin.services.QuoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/quotes")
public class QuotesController {
    private final QuoteService quoteService;

    public QuotesController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping
    public ResponseEntity<List<Quote>> getQuotes() {
        return ResponseEntity.ok(quoteService.findAllQuotes());
    }
}
