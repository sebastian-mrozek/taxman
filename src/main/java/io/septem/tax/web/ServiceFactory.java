package io.septem.tax.web;

import io.avaje.inject.Bean;
import io.avaje.inject.Factory;
import io.septem.tax.persistence.JsonFileStorageService;
import io.septem.tax.persistence.StorageService;

import java.nio.file.Path;

@Factory
public class ServiceFactory {

    @Bean
    public StorageService newInstance() {
        return new JsonFileStorageService(Path.of("private"));
    }

}
