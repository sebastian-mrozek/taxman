package io.septem.tax.web;

import io.avaje.inject.Bean;
import io.avaje.inject.Factory;
import io.septem.tax.logic.GstReturnService;
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
    public GstReturnService newGstReturnService() {
        return new GstReturnService();
    }

}
