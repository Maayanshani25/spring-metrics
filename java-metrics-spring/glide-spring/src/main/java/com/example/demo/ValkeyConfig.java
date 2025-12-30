package com.example.demo;

import io.valkey.springframework.data.valkey.connection.ValkeyClusterConfiguration;
import io.valkey.springframework.data.valkey.connection.ValkeyConnectionFactory;
import io.valkey.springframework.data.valkey.connection.valkeyglide.ValkeyGlideClientConfiguration;
import io.valkey.springframework.data.valkey.connection.valkeyglide.ValkeyGlideConnectionFactory;
import io.valkey.springframework.data.valkey.connection.valkeyglide.ValkeyGlideOpenTelemetry;
import io.valkey.springframework.data.valkey.core.StringValkeyTemplate;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValkeyConfig {

    @Bean
    public ValkeyGlideOpenTelemetry valkeyGlideOpenTelemetry() {
        // Minimal: use defaults (localhost collector, 10% sampling, 1s flush)
        return ValkeyGlideOpenTelemetry.defaults();

        // OPTIONAL: override defaults (example)
        // return ValkeyGlideOpenTelemetry.builder()
        //     .tracesEndpoint("http://localhost:4318/v1/traces")
        //     .metricsEndpoint("http://localhost:4318/v1/metrics")
        //     .samplePercentage(10)
        //     .flushIntervalMs(1000L)
        //     .build();
    }

    @Bean
    public ValkeyConnectionFactory valkeyConnectionFactory(ValkeyGlideOpenTelemetry telemetry) {
        // Valkey glide client
        String hostAndPort = "clustercfg.disney-test-valkey-7-r5.nra7gl.use1.cache.amazonaws.com:6379";

        ValkeyClusterConfiguration valkeyConfig = new ValkeyClusterConfiguration(List.of(hostAndPort));

        ValkeyGlideClientConfiguration clientConfig =
            ValkeyGlideClientConfiguration
                .builder()
                .useOpenTelemetry(telemetry)
                .useSsl() // keep only if TLS is enabled
                .build();

        return new ValkeyGlideConnectionFactory(valkeyConfig, clientConfig);
    }

    @Bean
    public StringValkeyTemplate valkeyTemplate(ValkeyConnectionFactory factory) {
        return new StringValkeyTemplate(factory);
    }
}
