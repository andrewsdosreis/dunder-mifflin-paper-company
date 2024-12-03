package com.andrewsreis.dundermifflin.core.usecases;

import com.andrewsreis.dundermifflin.core.domain.Quote;

public interface CreateQuoteUseCase {
    Quote create(String name, String quote);
}
