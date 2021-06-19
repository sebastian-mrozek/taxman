package io.septem.tax.web;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.avaje.http.api.WebRoutes;
import io.avaje.inject.SystemContext;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;
import io.septem.tax.persistence.DataAccessException;
import jakarta.inject.Inject;

import java.util.List;

public class Application {
    private final Javalin server;

    @Inject
    public Application() {
        configureObjectMapper();
        this.server = Javalin.create(cfg -> {
            cfg.enableCorsForAllOrigins();
            //TODO: add single page root to enable routing in svelte
//            cfg.addSinglePageRoot("/app", "/static/index.html");

//         TODO: make it configurable on app startup, only allow in dev mode
//         cfg.enableDevLogging();

//            cfg.addStaticFiles("/static");
        });

        registerWebRoutes();
        registerExceptionMappers();
    }

    public static void main(String[] args) {
        var app = new Application();
        app.start();
    }

    public static void configureObjectMapper() {
        JavalinJackson.getObjectMapper().registerModule(new JavaTimeModule());
    }

    private void registerExceptionMappers() {
        server.exception(DataAccessException.class, ExceptionHandlerFactory.createHandler(DataAccessException.class, 404));
    }

    private void registerWebRoutes() {
        List<WebRoutes> webRoutes = SystemContext.context().getBeans(WebRoutes.class);
        server.routes(() -> webRoutes.forEach(WebRoutes::registerRoutes));
    }

    public void start() {
        server.start();
    }

    public void start(int port) {
        server.start(port);
    }

    public void stop() {
        server.stop();
    }
}
