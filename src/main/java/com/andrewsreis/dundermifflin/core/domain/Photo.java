package com.andrewsreis.dundermifflin.core.domain;

import java.awt.image.BufferedImage;

public record Photo(
        String key,
        BufferedImage image
) {
}
