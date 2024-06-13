package com.distribuida.servicios;

import com.distribuida.db.Book;
import com.google.gson.JsonElement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class BookServiceImpl implements BookService{

    @Inject
    private EntityManager em;

    @Override
    public List<Book> findAll() {
        return em.createQuery("Select b from Book b order by b.id asc", Book.class).getResultList();
    }

    @Override
    public Book findById(Integer id) {
        return em.find(Book.class, id);
    }

    @Override
    public Book insert(Book book) {
        em.getTransaction().begin();
        em.persist(book);
        em.getTransaction().commit();
        return book;
    }

    @Override
    public Book update(Book book) {
        em.getTransaction().begin();
        em.merge(book);
        em.getTransaction().commit();
        return book;
    }

    @Override
    public void remove(Integer id) {
        em.getTransaction().begin();
        em.remove(this.findById(id));
        em.getTransaction().commit();
    }

}
