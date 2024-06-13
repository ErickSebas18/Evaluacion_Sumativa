package com.distribuida;

import com.distribuida.db.Book;
import com.distribuida.servicios.BookService;
import com.google.gson.Gson;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import jakarta.enterprise.inject.spi.CDI;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;

public class Main {

    static ContainerLifecycle lifecycle = null;

    static BookService servicio;

    static Gson gson = new Gson();

    static void findAll(ServerRequest req, ServerResponse res){
        res.send(gson.toJson(servicio.findAll()));
    }

    static void findById(ServerRequest req, ServerResponse res){
        res.send(gson.toJson(servicio.findById(Integer.valueOf(req.path().pathParameters().get("id")))));
    }

    static void insert(ServerRequest req, ServerResponse res) {

        Book nuevoBook = gson.fromJson(req.content().as(String.class), Book.class);

        if(nuevoBook == null){
            res.send("Datos vacios");
        }

        res.send(gson.toJson(servicio.insert(nuevoBook)));

        //res.send(gson.toJson(servicio.insert(gson.fromJson(req.content().toString(), Book.class))));
    }

    static void update(ServerRequest req, ServerResponse res){
        var bookActualizado = gson.fromJson(req.content().as(String.class), Book.class);

        var book = servicio.findById(bookActualizado.getId());

        if(book != null){
            res.send(gson.toJson(servicio.update(book)));
        }
    }

    static void remove(ServerRequest req, ServerResponse res){
        servicio.remove(Integer.valueOf(req.path().pathParameters().get("id")));
        res.send("Se ha eliminado");
    }

    public static void main(String[] args) {

        lifecycle = WebBeansContext.currentInstance().getService(ContainerLifecycle.class);
        lifecycle.startApplication(null);

        servicio = CDI.current().select(BookService.class).get();

        WebServer.builder()
                .routing(it -> it
                        .get("/books", Main::findAll)
                        .get("/books/{id}", Main::findById)
                        .post("/books", Main::insert)
                        .put("/books", Main::update)
                        .delete("/books/{id}", Main::remove)
                )
                .port(8080).build().start();

    }

}