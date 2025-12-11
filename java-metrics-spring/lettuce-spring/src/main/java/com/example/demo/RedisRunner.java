package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.StringRedisTemplate;

@Component
public class RedisRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RedisRunner.class);

    private final StringRedisTemplate redisTemplate;

    public RedisRunner(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run(String... args) {
        String key = "key";
        String value = "hello-from-lettuce";

        //run the loop 100 times to generate more metrics
        for (int i = 0; i < 10; i++) {
            redisTemplate.opsForValue().set(key + i, value + i);
            String readBack = redisTemplate.opsForValue().get(key + i);
            log.info("Iteration {}: Wrote and read back {}={}", i, key + i, readBack);
        }
        // log.info("Writing to Redis: {}={}", key, value);
        // redisTemplate.opsForValue().set(key, value);

        String readBack = redisTemplate.opsForValue().get(key);
        log.info("Read from Redis: {}={}", key, readBack);
    }
}
