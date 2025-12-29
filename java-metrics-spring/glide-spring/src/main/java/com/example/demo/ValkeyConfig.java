package com.example.demo;

import io.valkey.springframework.data.valkey.connection.ValkeyClusterConfiguration;
import io.valkey.springframework.data.valkey.connection.ValkeyConnectionFactory;
import io.valkey.springframework.data.valkey.connection.valkeyglide.ValkeyGlideClientConfiguration;
import io.valkey.springframework.data.valkey.connection.valkeyglide.ValkeyGlideConnectionFactory;
import io.valkey.springframework.data.valkey.core.StringValkeyTemplate;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        long flushIntervalMs = 20L;
        
        // Valkey glide client
        String hostAndPort = "clustercfg.disney-test-valkey-7-r5.nra7gl.use1.cache.amazonaws.com:6379";

        ValkeyClusterConfiguration valkeyConfig = new ValkeyClusterConfiguration(List.of(hostAndPort));

        ValkeyGlideClientConfiguration clientConfig =
            ValkeyGlideClientConfiguration
                .builder()
                .useOpenTelemetry(
                    tracesEndpoint,
                    metricsEndpoint,
                    samplePercentage,  // (optional)
                    flushIntervalMs    //  (optional)
                )
                .useSsl() // keep only if TLS is enabled
                .build();

        return new ValkeyGlideConnectionFactory(valkeyConfig, clientConfig);
    }

    @Bean
    public StringValkeyTemplate valkeyTemplate(ValkeyConnectionFactory factory) {
        // Spring will call afterPropertiesSet() automatically as part of bean lifecycle
        return new StringValkeyTemplate(factory);
    }
}
