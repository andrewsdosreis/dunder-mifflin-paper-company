package com.andrewsreis.dundermifflin.mappers;

import com.andrewsreis.dundermifflin.cache.entities.Quote;
import com.andrewsreis.dundermifflin.database.entities.EmployeeEntity;
import com.andrewsreis.dundermifflin.models.Employee;
import com.andrewsreis.dundermifflin.models.EmployeeQuotes;
import com.andrewsreis.dundermifflin.utils.ImageUtil;

import java.awt.image.BufferedImage;
import java.util.List;

public abstract class EmployeeMapper {
    public static Employee toModel(EmployeeEntity entity, String trivia) {
        return new Employee(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getDepartment(),
                entity.getPhoto(),
                trivia
        );
    }

    public static String toEmployeePhoto(BufferedImage photo) {
        return ImageUtil.beautify(photo);
    }

    public static String toEmployeeQuote(String quote, String name, String lastName, BufferedImage photo) {
        return ImageUtil.beautify(photo) + "\n\"" + quote + "\"\n - " + lastName + ", " + name + "\n";
    }

    public static EmployeeQuotes toEmployeeQuotes(List<Quote> quotes, String name, String lastName) {
        return new EmployeeQuotes(name, lastName, quotes.stream().map(Quote::getQuote).toList());
    }
}