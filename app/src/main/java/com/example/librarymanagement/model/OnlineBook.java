package com.example.librarymanagement.model;

public class OnlineBook {
    private final String key;
    private final String title;
    private final String author;
    private final String firstPublishYear;

    public OnlineBook(String key, String title, String author, String firstPublishYear) {
        this.key = key;
        this.title = title;
        this.author = author;
        this.firstPublishYear = firstPublishYear;
    }

    public String getKey() { return key; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getFirstPublishYear() { return firstPublishYear; }
}
