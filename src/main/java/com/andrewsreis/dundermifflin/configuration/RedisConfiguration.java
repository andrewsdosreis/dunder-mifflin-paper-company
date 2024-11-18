package com.andrewsreis.dundermifflin.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
    private final AwsProperties awsProperties;

    public RedisConfiguration(AwsProperties awsProperties) {
        this.awsProperties = awsProperties;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(
                awsProperties.getElasticache().getHost(),
                awsProperties.getElasticache().getPort()
        );
        connectionFactory.setUseSsl(false);
        return connectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
