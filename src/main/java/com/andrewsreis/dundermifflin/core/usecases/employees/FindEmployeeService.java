package com.andrewsreis.dundermifflin.core.usecases.employees;

import com.andrewsreis.dundermifflin.core.dataproviders.EmployeeDataProvider;
import com.andrewsreis.dundermifflin.core.dataproviders.QuoteDataProvider;
import com.andrewsreis.dundermifflin.core.dataproviders.TriviaDataProvider;
import com.andrewsreis.dundermifflin.core.domain.Employee;
import com.andrewsreis.dundermifflin.core.domain.Quote;
import com.andrewsreis.dundermifflin.core.usecases.FindEmployeeUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindEmployeeService implements FindEmployeeUseCase {
    private final EmployeeDataProvider employeeDataProvider;
    private final QuoteDataProvider quoteDataProvider;
    private final TriviaDataProvider triviaDataProvider;

    public FindEmployeeService(EmployeeDataProvider employeeDataProvider, QuoteDataProvider quoteDataProvider, TriviaDataProvider triviaDataProvider) {
        this.employeeDataProvider = employeeDataProvider;
        this.quoteDataProvider = quoteDataProvider;
        this.triviaDataProvider = triviaDataProvider;
    }

    @Override
    public Employee find(Long id) {
        Employee employee = employeeDataProvider.findById(id);
        String trivia = triviaDataProvider.getTrivia(employee.firstName(), employee.lastName());
        List<Quote> quotes = quoteDataProvider.findAllByName(employee.firstName(), employee.lastName());
        return employee.withAggregatedData(employee.photo(), trivia, quotes);
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = employeeDataProvider.findAll();
        return employees.stream()
                .map(employee -> {
                    String trivia = triviaDataProvider.getTrivia(employee.firstName(), employee.lastName());
                    List<Quote> quotes = quoteDataProvider.findAllByName(employee.firstName(), employee.lastName());
                    return employee.withAggregatedData(employee.photo(), trivia, quotes);
                })
                .toList();
    }
}
