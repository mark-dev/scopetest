package ru.glsv;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.glsv.scope.SampleScope;

@SpringBootApplication
@EnableAutoConfiguration
public class ScopetestApplication {
    public static final String SAMPLE_SCOPE = "sampleScope";

    @Bean
    public SampleScope sampleScope() {
        return new SampleScope();
    }

    @Bean
    public CustomScopeConfigurer config() {
        CustomScopeConfigurer scope = new CustomScopeConfigurer();
        scope.addScope(SAMPLE_SCOPE, sampleScope());
        return scope;
    }

    //https://habrahabr.ru/post/220233/
    public static void main(String[] args) {
        SpringApplication.run(ScopetestApplication.class, args);
    }
}
