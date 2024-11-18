package com.andrewsreis.dundermifflin.services;

import com.andrewsreis.dundermifflin.cache.entities.Quote;
import com.andrewsreis.dundermifflin.cache.repositories.QuoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public List<Quote> findAllQuotesFromEmployee(String name, String lastName) {
        return quoteRepository.findAllQuotesByPerson(name + lastName);
    }

    public List<Quote> findAllQuotes() {
        return quoteRepository.findAllQuotes();
    }

    public void saveQuote(Quote quote) {
        quoteRepository.saveQuote(quote);
    }
}
