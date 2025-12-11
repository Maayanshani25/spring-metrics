package com.example.demo;

import io.valkey.springframework.data.valkey.core.StringValkeyTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ValkeyRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ValkeyRunner.class);

    private final StringValkeyTemplate valkeyTemplate;

    public ValkeyRunner(StringValkeyTemplate valkeyTemplate) {
        this.valkeyTemplate = valkeyTemplate;
    }

    @Override
    public void run(String... args) {
        String keyPrefix = "key";
        String valuePrefix = "hello-from-glide-";

        for (int i = 0; i < 10; i++) {
            String key = keyPrefix + i;
            String value = valuePrefix + i;

            valkeyTemplate.opsForValue().set(key, value);
            String readBack = valkeyTemplate.opsForValue().get(key);
            log.info("Iteration {}: Wrote and read back {}={}", i, key, readBack);
        }

        String readBack = valkeyTemplate.opsForValue().get(keyPrefix);
        log.info("Read from Valkey: {}={}", keyPrefix, readBack);
    }
}
