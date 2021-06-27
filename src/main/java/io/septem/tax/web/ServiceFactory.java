package io.septem.tax.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.avaje.inject.Bean;
import io.avaje.inject.Factory;
import io.septem.tax.logic.TaxReturnService;
import io.septem.tax.persistence.JsonFileStorageService;
import io.septem.tax.persistence.StorageService;

import java.nio.file.Path;

@Factory
public class ServiceFactory {

    @Bean
    public StorageService newStorageService() {
        return new JsonFileStorageService(Path.of("private"));
    }

    @Bean
    public TaxReturnService newTaxReturnService() {
        return new TaxReturnService();
    }

    @Bean
    public ObjectMapper newObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

}
