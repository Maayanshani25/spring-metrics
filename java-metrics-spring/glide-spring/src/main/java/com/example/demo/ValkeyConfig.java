package com.example.demo;

import io.valkey.springframework.data.valkey.connection.ValkeyConnectionFactory;
import io.valkey.springframework.data.valkey.connection.valkeyglide.ValkeyGlideConnectionFactory;
import io.valkey.springframework.data.valkey.core.StringValkeyTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import glide.api.OpenTelemetry;
import glide.api.OpenTelemetry.OpenTelemetryConfig;
import glide.api.OpenTelemetry.TracesConfig;
import glide.api.OpenTelemetry.MetricsConfig;

@Configuration
public class ValkeyConfig {

    @Bean
    public ValkeyConnectionFactory valkeyConnectionFactory() {
        // Opentelemetry setup for Glide
        String tracesEndpoint = "http://localhost:4318/v1/traces";
        String metricsEndpoint = "http://localhost:4318/v1/metrics";
        // String tracesEndpoint = "file:///tmp/sapns.json";
        // String metricsEndpoint = "file:///tmp/metrics.json";
        int samplePercentage = 10;
        long flushIntervalMs = 100L;


        OpenTelemetry.init(
            OpenTelemetryConfig.builder()
                .traces(
                    TracesConfig.builder()
                        .endpoint(tracesEndpoint)
                        .samplePercentage(samplePercentage) // optional
                        .build()
                )
                .metrics(
                    MetricsConfig.builder()
                        .endpoint(metricsEndpoint)
                        .build()
                )
                .flushIntervalMs(flushIntervalMs) // optional
                .build()
        );
        return new ValkeyGlideConnectionFactory();
    }

    @Bean
    public StringValkeyTemplate valkeyTemplate(ValkeyConnectionFactory factory) {
        // Spring will call afterPropertiesSet() automatically as part of bean lifecycle
        return new StringValkeyTemplate(factory);
    }
}
