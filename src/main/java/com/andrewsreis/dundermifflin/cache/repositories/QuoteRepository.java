package com.andrewsreis.dundermifflin.cache.repositories;

import com.andrewsreis.dundermifflin.cache.entities.Quote;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public class QuoteRepository {

    private static final String QUOTE_KEY = "Quote";

    private final RedisTemplate<String, Object> redisTemplate;

    public QuoteRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveQuote(Quote quote) {
        String quoteId = UUID.randomUUID().toString();
        redisTemplate.opsForHash().put(QUOTE_KEY, quoteId, quote);
        redisTemplate.opsForSet().add(quote.getName(), quoteId);
    }

    public List<Quote> findAllQuotesByPerson(String name) {
        Set<Object> quoteIds = redisTemplate.opsForSet().members(name);
        List<Quote> quotes = new ArrayList<>();
        if (quoteIds != null) {
            for (Object id : quoteIds) {
                quotes.add(findQuoteById((String) id));
            }
        }
        return quotes;
    }

    public List<Quote> findAllQuotes() {
        List<Object> allQuotes = redisTemplate.opsForHash().values(QUOTE_KEY);
        List<Quote> quotes = new ArrayList<>();
        for (Object obj : allQuotes) {
            quotes.add((Quote) obj);
        }
        return quotes;
    }

    private Quote findQuoteById(String quoteId) {
        return (Quote) redisTemplate.opsForHash().get(QUOTE_KEY, quoteId);
    }
}
