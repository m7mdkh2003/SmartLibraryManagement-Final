package com.example.librarymanagement;

import android.content.Context;
import android.database.Cursor;

import com.example.librarymanagement.core.AppExecutors;
import com.example.librarymanagement.model.BookModel;

import java.util.List;

/** MVC Controller for the local Books module. */
public class BookController {
    public interface BooksCallback {
        void onLoaded(List<BookModel> books);
        void onError(String message);
    }

    public interface BookCallback {
        void onLoaded(BookModel book);
        void onError(String message);
    }

    public interface OperationCallback {
        void onComplete(boolean success, String message);
    }

    private final Context appContext;

    public BookController(Context context) {
        appContext = context.getApplicationContext();
    }

    public void loadBooks(BooksCallback callback) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            try (DbHelper helper = new DbHelper(appContext)) {
                List<BookModel> books = helper.getAllBooksList();
                AppExecutors.getInstance().runOnMainThread(() -> callback.onLoaded(books));
            } catch (Exception error) {
                AppExecutors.getInstance().runOnMainThread(() -> callback.onError("Unable to load books"));
            }
        });
    }

    public void getBook(String name, BookCallback callback) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            try (DbHelper helper = new DbHelper(appContext); Cursor cursor = helper.getBookByName(name)) {
                if (!cursor.moveToFirst()) {
                    AppExecutors.getInstance().runOnMainThread(() -> callback.onError("Book not found"));
                    return;
                }
                BookModel book = new BookModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.getBookDetails("BOOK_ID"))),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.getBookDetails("BOOK_NAME"))),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.getBookDetails("BOOK_AUTHOR"))),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.getBookDetails("BOOK_PUBLISHER"))),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.getBookDetails("BOOK_QUANTITY"))),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.getBookDetails("BOOK_AVAILABLE"))) == 1);
                AppExecutors.getInstance().runOnMainThread(() -> callback.onLoaded(book));
            } catch (Exception error) {
                AppExecutors.getInstance().runOnMainThread(() -> callback.onError("Unable to read book"));
            }
        });
    }

    public void addBook(String name, String author, String publisher, int quantity, OperationCallback callback) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            boolean success;
            try (DbHelper helper = new DbHelper(appContext)) {
                success = helper.addBook(name, author, publisher, quantity, quantity > 0);
            }
            boolean result = success;
            AppExecutors.getInstance().runOnMainThread(() -> callback.onComplete(result,
                    result ? "Book added successfully" : "Unable to add book"));
        });
    }

    public void updateBook(BookModel book, OperationCallback callback) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            boolean success;
            try (DbHelper helper = new DbHelper(appContext)) {
                success = helper.updateBook(book.getId(), book.getName(), book.getAuthor(),
                        book.getPublisher(), book.getQuantity(), book.isAvailable());
            }
            boolean result = success;
            AppExecutors.getInstance().runOnMainThread(() -> callback.onComplete(result,
                    result ? "Book updated successfully" : "Unable to update book"));
        });
    }

    public void deleteBook(int id, OperationCallback callback) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            boolean success;
            try (DbHelper helper = new DbHelper(appContext)) {
                success = helper.deleteBook(id);
            }
            boolean result = success;
            AppExecutors.getInstance().runOnMainThread(() -> callback.onComplete(result,
                    result ? "Book deleted successfully" : "Unable to delete book"));
        });
    }
}
