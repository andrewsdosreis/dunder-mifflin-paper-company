package com.andrewsreis.dundermifflin.app.entrypoints.quotes;

import com.andrewsreis.dundermifflin.core.usecases.GetRandomQuoteUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quotes")
public class QuotesRestController {
    private final GetRandomQuoteUseCase getRandomQuoteUseCase;

    public QuotesRestController(GetRandomQuoteUseCase getRandomQuoteUseCase) {
        this.getRandomQuoteUseCase = getRandomQuoteUseCase;
    }

    @GetMapping
    public ResponseEntity<String> getRandomQuote() {
        return ResponseEntity.ok(getRandomQuoteUseCase.getRandom());
    }
}
