package com.andrewsreis.dundermifflin.app.dataproviders.quote;

import java.io.Serializable;

public class QuoteEntity implements Serializable {
    private String name;
    private String quote;

    public QuoteEntity(String name, String quote) {
        this.name = name;
        this.quote = quote;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
}