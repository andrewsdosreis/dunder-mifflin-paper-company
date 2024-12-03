package com.andrewsreis.dundermifflin.app.dataproviders.quote;

import com.andrewsreis.dundermifflin.core.domain.Quote;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public class QuoteDataProvider implements com.andrewsreis.dundermifflin.core.dataproviders.QuoteDataProvider {

    private static final String QUOTE_KEY = "Quote";

    private final RedisTemplate<String, Object> redisTemplate;

    public QuoteDataProvider(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Quote> findAll() {
        List<Object> allQuotes = redisTemplate.opsForHash().values(QUOTE_KEY);
        List<QuoteEntity> quotes = new ArrayList<>();
        for (Object obj : allQuotes) {
            quotes.add((QuoteEntity) obj);
        }
        return QuoteMapper.toDomain(quotes);
    }

    @Override
    public List<Quote> findAllByName(String firstName, String lastName) {
        String name = QuoteMapper.toName(firstName, lastName);
        Set<Object> quoteIds = redisTemplate.opsForSet().members(name);
        List<QuoteEntity> quotes = new ArrayList<>();
        if (quoteIds != null) {
            for (Object id : quoteIds) {
                quotes.add(findQuoteById((String) id));
            }
        }
        return QuoteMapper.toDomain(quotes);
    }

    @Override
    public Quote save(Quote quote) {
        String quoteId = UUID.randomUUID().toString();
        QuoteEntity quoteToCreate = QuoteMapper.toEntity(quote);
        redisTemplate.opsForHash().put(QUOTE_KEY, quoteId, quoteToCreate);
        redisTemplate.opsForSet().add(quoteToCreate.getName(), quoteId);
        return QuoteMapper.toDomain(quoteToCreate);
    }

    private QuoteEntity findQuoteById(String quoteId) {
        return (QuoteEntity) redisTemplate.opsForHash().get(QUOTE_KEY, quoteId);
    }
}
