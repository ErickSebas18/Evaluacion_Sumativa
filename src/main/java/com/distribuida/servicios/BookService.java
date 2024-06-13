package com.distribuida.servicios;

import com.distribuida.db.Book;
import com.google.gson.JsonElement;

import java.util.List;

public interface BookService {

    List<Book> findAll();
    Book findById(Integer id);
    Book insert(Book book);
    Book update(Book book);
    void remove(Integer id);

}
