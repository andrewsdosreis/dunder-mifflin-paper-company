package com.andrewsreis.dundermifflin.core.usecases.quotes;

import com.andrewsreis.dundermifflin.core.dataproviders.EmployeeDataProvider;
import com.andrewsreis.dundermifflin.core.dataproviders.PhotoDataProvider;
import com.andrewsreis.dundermifflin.core.dataproviders.QuoteDataProvider;
import com.andrewsreis.dundermifflin.core.domain.Employee;
import com.andrewsreis.dundermifflin.core.domain.Photo;
import com.andrewsreis.dundermifflin.core.domain.Quote;
import com.andrewsreis.dundermifflin.core.usecases.GetRandomQuoteUseCase;
import com.andrewsreis.dundermifflin.core.utils.ImageUtil;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GetRandomQuoteService implements GetRandomQuoteUseCase {
    private final EmployeeDataProvider employeeDataProvider;
    private final QuoteDataProvider quoteDataProvider;
    private final PhotoDataProvider photoDataProvider;

    public GetRandomQuoteService(EmployeeDataProvider employeeDataProvider, QuoteDataProvider quoteDataProvider, PhotoDataProvider photoDataProvider) {
        this.employeeDataProvider = employeeDataProvider;
        this.quoteDataProvider = quoteDataProvider;
        this.photoDataProvider = photoDataProvider;
    }

    @Override
    public String getRandom() {
        List<Quote> allQuotes = quoteDataProvider.findAll();
        int randomIndex = ThreadLocalRandom.current().nextInt(allQuotes.size());
        Quote quote = allQuotes.get(randomIndex);
        Employee employee = employeeDataProvider.findByName(quote.firstName(), quote.lastName());
        Photo photo = photoDataProvider.downloadPhoto(employee.photo().key());
        return toEmployeeQuote(quote.quote(), employee.firstName(), employee.lastName(), photo.image());
    }

    private String toEmployeeQuote(String quote, String name, String lastName, BufferedImage photo) {
        return ImageUtil.beautify(photo) + "\n\"" + quote + "\"\n - " + lastName + ", " + name + "\n";
    }
}
