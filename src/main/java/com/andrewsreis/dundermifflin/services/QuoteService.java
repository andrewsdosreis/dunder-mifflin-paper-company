package com.andrewsreis.dundermifflin.services;

import com.andrewsreis.dundermifflin.cache.entities.Quote;
import com.andrewsreis.dundermifflin.cache.repositories.QuoteRepository;
import com.andrewsreis.dundermifflin.database.repositories.EmployeeRepository;
import com.andrewsreis.dundermifflin.exception.EmployeeNotFoundException;
import com.andrewsreis.dundermifflin.mappers.EmployeeMapper;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;
    private final EmployeeRepository employeeRepository;
    private final PhotoService photoService;

    public QuoteService(QuoteRepository quoteRepository, EmployeeRepository employeeRepository, PhotoService photoService) {
        this.quoteRepository = quoteRepository;
        this.employeeRepository = employeeRepository;
        this.photoService = photoService;
    }

    public List<Quote> findAllQuotesFromEmployee(String name, String lastName) {
        return quoteRepository.findAllQuotesByPerson(name + lastName);
    }

    public String getRandomQuote() {
        var allQuotes = quoteRepository.findAllQuotes();
        int randomIndex = ThreadLocalRandom.current().nextInt(allQuotes.size());
        Quote quote = allQuotes.get(randomIndex);

        String firstName = quote.getName().split("(?=[A-Z])")[0];
        String lastName = quote.getName().split("(?=[A-Z])")[1];

        var employee = employeeRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new EmployeeNotFoundException(firstName, lastName));

        BufferedImage photo = photoService.downloadEmployeePhoto(employee.getPhoto());

        return EmployeeMapper.toEmployeeQuote(quote.getQuote(), firstName, lastName, photo);
    }

    public void saveQuote(Quote quote) {
        quoteRepository.saveQuote(quote);
    }
}
