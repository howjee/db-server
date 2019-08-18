package com.rh.service;

import com.rh.mapper.BookMapper;
import com.rh.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookMapper bookMapper;

    public List<Book> getAllBooks() {
        return bookMapper.getAll();
    }

    public List<Book> getBooksByName(String name) {
        Book book = new Book();
        book.setName(name);
        return bookMapper.getBookByName(book);
    }
}
