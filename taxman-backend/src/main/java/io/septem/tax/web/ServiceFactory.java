package io.septem.tax.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.avaje.inject.Bean;
import io.avaje.inject.Factory;
import io.septem.tax.logic.TaxReturnService;
import io.septem.tax.mapper.ModelMapper;
import io.septem.tax.persistence.FileStorageService;
import io.septem.tax.persistence.StorageService;

import java.nio.file.Path;

@Factory
public class ServiceFactory {

    @Bean
    public StorageService newStorageService(ObjectMapper jsonMapper, CsvMapper csvMapper, ModelMapper modelMapper) {
        return new FileStorageService(jsonMapper, csvMapper, modelMapper, Path.of("private"));
    }

    @Bean
    public TaxReturnService newTaxReturnService() {
        return new TaxReturnService();
    }

    @Bean
    public ObjectMapper newObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        configureMapper(objectMapper);
        return objectMapper;
    }

    @Bean
    public CsvMapper newCsvObjectMapper() {
        CsvMapper csvMapper = new CsvMapper();
        configureMapper(csvMapper);
        return csvMapper;
    }

    @Bean
    public ModelMapper newModelMapper() {
        return new ModelMapper();
    }

    private void configureMapper(ObjectMapper mapper) {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

}
