package com.example.demo;

import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.tracing.MicrometerTracing;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class ObservabilityConfig {

    @Bean
    public ClientResources clientResources(ObservationRegistry observationRegistry) {
        // "my-redis-cache" will appear as the service name in spans/metrics
        return ClientResources.builder()
                .tracing(new MicrometerTracing(observationRegistry, "my-redis-cache"))
                .build();
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(ClientResources clientResources) {
        RedisConfiguration redisConfiguration =
                new RedisStandaloneConfiguration("localhost", 6379);
        // ((RedisStandaloneConfiguration) redisConfiguration).setPassword("yourPassword"); // if needed

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .clientResources(clientResources)
                .build();

        return new LettuceConnectionFactory(redisConfiguration, clientConfig);
    }
}


