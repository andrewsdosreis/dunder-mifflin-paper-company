package com.andrewsreis.dundermifflin.core.usecases.quotes;

import com.andrewsreis.dundermifflin.core.dataproviders.QuoteDataProvider;
import com.andrewsreis.dundermifflin.core.domain.Quote;
import com.andrewsreis.dundermifflin.core.usecases.CreateQuoteUseCase;
import org.springframework.stereotype.Service;

@Service
public class CreateQuoteService implements CreateQuoteUseCase {
    public static final String NAME_SEPARATOR = "(?=[A-Z])";

    private final QuoteDataProvider quoteDataProvider;

    public CreateQuoteService(QuoteDataProvider quoteDataProvider) {
        this.quoteDataProvider = quoteDataProvider;
    }

    @Override
    public Quote create(String name, String quote) {
        String firstName = name.split(NAME_SEPARATOR)[0];
        String lastName = name.split(NAME_SEPARATOR)[1];
        Quote quoteToCreate = new Quote(firstName, lastName, quote);
        return quoteDataProvider.save(quoteToCreate);
    }
}
