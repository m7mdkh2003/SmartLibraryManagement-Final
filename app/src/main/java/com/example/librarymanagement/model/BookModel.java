package com.example.librarymanagement.model;

public class BookModel {
    private final int id;
    private final String name;
    private final String author;
    private final String publisher;
    private final int quantity;
    private final boolean available;

    public BookModel(int id, String name, String author, String publisher, int quantity, boolean available) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.quantity = quantity;
        this.available = available;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public int getQuantity() { return quantity; }
    public boolean isAvailable() { return available; }
}
