package com.example.demo;

import io.valkey.springframework.data.valkey.connection.ValkeyClusterConfiguration;
import io.valkey.springframework.data.valkey.connection.ValkeyConnectionFactory;
import io.valkey.springframework.data.valkey.connection.valkeyglide.ValkeyGlideClientConfiguration;
import io.valkey.springframework.data.valkey.connection.valkeyglide.ValkeyGlideConnectionFactory;
import io.valkey.springframework.data.valkey.connection.valkeyglide
        .ValkeyGlideClientConfiguration.OpenTelemetryForGlide;

import io.valkey.springframework.data.valkey.core.StringValkeyTemplate;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValkeyConfig {

    @Bean
    public OpenTelemetryForGlide openTelemetryForGlide() {
        return OpenTelemetryForGlide.defaults();
    }

    @Bean
    public ValkeyConnectionFactory valkeyConnectionFactory(
            OpenTelemetryForGlide openTelemetryForGlide
    ) {
        String hostAndPort = "clustercfg.disney-test-valkey-7-r5.nra7gl.use1.cache.amazonaws.com:6379";

        ValkeyClusterConfiguration valkeyConfig =
                new ValkeyClusterConfiguration(List.of(hostAndPort));

        ValkeyGlideClientConfiguration clientConfig =
                ValkeyGlideClientConfiguration.builder()
                        .useOpenTelemetry(openTelemetryForGlide)
                        .useSsl() // keep only if TLS is enabled
                        .build();

        return new ValkeyGlideConnectionFactory(valkeyConfig, clientConfig);
    }

    @Bean
    public StringValkeyTemplate valkeyTemplate(ValkeyConnectionFactory factory) {
        return new StringValkeyTemplate(factory);
    }
}
