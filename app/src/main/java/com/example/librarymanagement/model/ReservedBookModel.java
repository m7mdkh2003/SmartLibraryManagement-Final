package com.example.librarymanagement.model;

public class ReservedBookModel {
    private final String name;
    private final String author;
    private final String publisher;
    private final String reservedBy;

    public ReservedBookModel(String name, String author, String publisher, String reservedBy) {
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.reservedBy = reservedBy;
    }

    public String getName() { return name; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public String getReservedBy() { return reservedBy; }
}
