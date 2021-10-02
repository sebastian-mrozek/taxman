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
import io.septem.tax.persistence.DataAccessException;
import io.septem.tax.persistence.FileStorageService;
import io.septem.tax.persistence.StorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Factory
public class ServiceFactory {

    public static final String PERSISTENCE_PATH_PROPERTY_NAME = "PERSISTENCE_PATH";

    private Path persistencePath() {
        final var persistence_path = System.getenv(PERSISTENCE_PATH_PROPERTY_NAME);
        Path path = Path.of(Objects.requireNonNullElse(persistence_path, "sample-data"));
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new DataAccessException("Failed to create folder " + path.toAbsolutePath(), e);
            }
        }
        return path;
    }

    @Bean
    public StorageService fileStorageService() {
        return new FileStorageService(newObjectMapper(), newCsvObjectMapper(), newModelMapper(), persistencePath());
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
