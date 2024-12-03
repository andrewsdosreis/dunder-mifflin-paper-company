package com.andrewsreis.dundermifflin.app.dataproviders.quote;

import com.andrewsreis.dundermifflin.core.domain.Quote;

import java.util.List;

public class QuoteMapper {
    public static final String SPACE = " ";

    public static Quote toDomain(QuoteEntity quoteEntity) {
        return new Quote(
                quoteEntity.getName().split(SPACE)[0],
                quoteEntity.getName().split(SPACE)[1],
                quoteEntity.getQuote()
        );
    }

    public static List<Quote> toDomain(List<QuoteEntity> quotes) {
        return quotes.stream().map(QuoteMapper::toDomain).toList();
    }

    public static QuoteEntity toEntity(Quote quote) {
        return new QuoteEntity(QuoteMapper.toName(quote.firstName(), quote.lastName()), quote.quote());
    }

    public static String toName(String firstName, String lastName) {
        return firstName + SPACE + lastName;
    }
}
