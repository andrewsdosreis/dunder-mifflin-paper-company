package com.andrewsreis.dundermifflin.core.dataproviders;

import com.andrewsreis.dundermifflin.core.domain.Quote;

import java.util.List;

public interface QuoteDataProvider {
    List<Quote> findAll();

    List<Quote> findAllByName(String firstName, String lastName);

    Quote save(Quote quote);
}
