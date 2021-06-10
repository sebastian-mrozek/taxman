package io.septem.tax.web;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.avaje.http.api.WebRoutes;
import io.avaje.inject.SystemContext;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.plugin.json.JavalinJson;
import io.septem.tax.persistence.DataAccessException;

import java.util.List;

public class Application {
    private final Javalin server;

    public static void main(String[] args) {
        JavalinJackson.getObjectMapper().registerModule(new JavaTimeModule());
        var app = new Application();
        app.start();
    }

    public Application() {
        this.server = Javalin.create(cfg -> {
            cfg.enableCorsForAllOrigins();

//         TODO: make it configurable on app startup, only allow in dev mode
//         cfg.enableDevLogging();

//            cfg.addStaticFiles("/static");
        });

        registerWebRoutes();
        registerExceptionMappers();
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
