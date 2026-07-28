package com.example.librarymanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.librarymanagement.model.BookModel;
import com.example.librarymanagement.model.MemberModel;
import com.example.librarymanagement.model.OnlineBook;
import com.example.librarymanagement.model.ReservedBookModel;

import java.util.ArrayList;
import java.util.List;

/** SQLite data source used for the local/offline mode. */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "LDB";
    private static final int DB_VERSION = 3;

    private static final String USER_TABLE = "user";
    private static final String BOOK_TABLE = "book";
    private static final String TABLE_MEMBER = "members";
    private static final String RESERVE_TABLE = "book_reserve";
    private static final String FAVORITES_TABLE = "online_favorites";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String CONFIRM_PASSWORD = "confirm_password";

    private static final String BOOK_ID = "bid";
    private static final String BOOK_NAME = "bname";
    private static final String BOOK_AUTHOR = "bauthor";
    private static final String BOOK_PUBLISHER = "bpublisher";
    private static final String BOOK_QUANTITY = "bquantity";
    private static final String BOOK_AVAILABLE = "bavailable";

    private static final String MEMBER_ID = "id";
    private static final String MEMBER_NAME = "name";
    private static final String MEMBER_EMAIL = "email";
    private static final String MEMBER_PHONE = "phone";
    private static final String MEMBER_ADDRESS = "address";

    private static final String RESERVE_ID = "reserve_id";
    private static final String RESERVE_BOOK_ID = "book_id";
    private static final String RESERVE_MEMBER_NAME = "member_name";

    private static final String FAVORITE_KEY = "book_key";
    private static final String FAVORITE_TITLE = "title";
    private static final String FAVORITE_AUTHOR = "author";
    private static final String FAVORITE_YEAR = "publish_year";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + USER_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT NOT NULL, "
                + EMAIL + " TEXT NOT NULL UNIQUE, "
                + PASSWORD + " TEXT NOT NULL, "
                + CONFIRM_PASSWORD + " TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + BOOK_TABLE + " ("
                + BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BOOK_NAME + " TEXT NOT NULL, "
                + BOOK_AUTHOR + " TEXT NOT NULL, "
                + BOOK_PUBLISHER + " TEXT NOT NULL, "
                + BOOK_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + BOOK_AVAILABLE + " INTEGER NOT NULL DEFAULT 1)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_MEMBER + " ("
                + MEMBER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MEMBER_NAME + " TEXT NOT NULL, "
                + MEMBER_EMAIL + " TEXT NOT NULL, "
                + MEMBER_PHONE + " TEXT NOT NULL, "
                + MEMBER_ADDRESS + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + RESERVE_TABLE + " ("
                + RESERVE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RESERVE_BOOK_ID + " INTEGER NOT NULL, "
                + RESERVE_MEMBER_NAME + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + RESERVE_BOOK_ID + ") REFERENCES " + BOOK_TABLE + "(" + BOOK_ID + ") ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + FAVORITES_TABLE + " ("
                + FAVORITE_KEY + " TEXT PRIMARY KEY, "
                + FAVORITE_TITLE + " TEXT NOT NULL, "
                + FAVORITE_AUTHOR + " TEXT, "
                + FAVORITE_YEAR + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + FAVORITES_TABLE + " ("
                    + FAVORITE_KEY + " TEXT PRIMARY KEY, "
                    + FAVORITE_TITLE + " TEXT NOT NULL, "
                    + FAVORITE_AUTHOR + " TEXT, "
                    + FAVORITE_YEAR + " TEXT)");
        }
        if (oldVersion < 3) {
            // Existing user, book, member and reservation data is intentionally preserved.
            db.execSQL("CREATE INDEX IF NOT EXISTS index_user_email ON " + USER_TABLE + "(" + EMAIL + ")");
        }
    }

    public boolean loginUser(String email, String password) {
        try (SQLiteDatabase db = getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT " + ID + " FROM " + USER_TABLE
                     + " WHERE " + EMAIL + " = ? AND " + PASSWORD + " = ? LIMIT 1",
                     new String[]{email, password})) {
            return cursor.moveToFirst();
        }
    }

    public boolean registerUser(String name, String email, String password, String confirmPassword) {
        if (emailExists(email)) return false;
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(EMAIL, email);
        values.put(PASSWORD, password);
        values.put(CONFIRM_PASSWORD, confirmPassword);
        return getWritableDatabase().insert(USER_TABLE, null, values) != -1;
    }

    private boolean emailExists(String email) {
        try (Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT " + ID + " FROM " + USER_TABLE + " WHERE " + EMAIL + " = ? LIMIT 1",
                new String[]{email})) {
            return cursor.moveToFirst();
        }
    }

    public static String getBookDetails(String detailType) {
        switch (detailType) {
            case "BOOK_ID": return BOOK_ID;
            case "BOOK_NAME": return BOOK_NAME;
            case "BOOK_AUTHOR": return BOOK_AUTHOR;
            case "BOOK_PUBLISHER": return BOOK_PUBLISHER;
            case "BOOK_QUANTITY": return BOOK_QUANTITY;
            case "BOOK_AVAILABLE": return BOOK_AVAILABLE;
            default: return null;
        }
    }

    public static String getMemberDetails(String detailType) {
        switch (detailType) {
            case "MEMBER_ID": return MEMBER_ID;
            case "MEMBER_NAME": return MEMBER_NAME;
            case "MEMBER_EMAIL": return MEMBER_EMAIL;
            case "MEMBER_PHONE": return MEMBER_PHONE;
            case "MEMBER_ADDRESS": return MEMBER_ADDRESS;
            default: return null;
        }
    }

    public static String getReservedBookDetails(String detailType) {
        switch (detailType) {
            case "RESERVE_ID": return RESERVE_ID;
            case "RESERVE_BOOK_ID": return RESERVE_BOOK_ID;
            case "RESERVE_MEMBER_NAME": return RESERVE_MEMBER_NAME;
            default: return null;
        }
    }

    public boolean addBook(String name, String author, String publisher, int quantity, boolean available) {
        ContentValues values = new ContentValues();
        values.put(BOOK_NAME, name);
        values.put(BOOK_AUTHOR, author);
        values.put(BOOK_PUBLISHER, publisher);
        values.put(BOOK_QUANTITY, quantity);
        values.put(BOOK_AVAILABLE, available ? 1 : 0);
        return getWritableDatabase().insert(BOOK_TABLE, null, values) != -1;
    }

    public boolean updateBook(int id, String name, String author, String publisher, int quantity, boolean available) {
        ContentValues values = new ContentValues();
        values.put(BOOK_NAME, name);
        values.put(BOOK_AUTHOR, author);
        values.put(BOOK_PUBLISHER, publisher);
        values.put(BOOK_QUANTITY, quantity);
        values.put(BOOK_AVAILABLE, available ? 1 : 0);
        return getWritableDatabase().update(BOOK_TABLE, values, BOOK_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteBook(int id) {
        return getWritableDatabase().delete(BOOK_TABLE, BOOK_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public Cursor getBookByName(String bookName) {
        return getReadableDatabase().query(BOOK_TABLE,
                new String[]{BOOK_ID, BOOK_NAME, BOOK_AUTHOR, BOOK_PUBLISHER, BOOK_QUANTITY, BOOK_AVAILABLE},
                BOOK_NAME + " = ?", new String[]{bookName}, null, null, null);
    }

    public Cursor getAllBook() {
        return getReadableDatabase().rawQuery("SELECT * FROM " + BOOK_TABLE + " ORDER BY " + BOOK_NAME, null);
    }

    public List<BookModel> getAllBooksList() {
        List<BookModel> books = new ArrayList<>();
        try (Cursor cursor = getAllBook()) {
            int idIndex = cursor.getColumnIndexOrThrow(BOOK_ID);
            int nameIndex = cursor.getColumnIndexOrThrow(BOOK_NAME);
            int authorIndex = cursor.getColumnIndexOrThrow(BOOK_AUTHOR);
            int publisherIndex = cursor.getColumnIndexOrThrow(BOOK_PUBLISHER);
            int quantityIndex = cursor.getColumnIndexOrThrow(BOOK_QUANTITY);
            int availableIndex = cursor.getColumnIndexOrThrow(BOOK_AVAILABLE);
            while (cursor.moveToNext()) {
                books.add(new BookModel(cursor.getInt(idIndex), cursor.getString(nameIndex),
                        cursor.getString(authorIndex), cursor.getString(publisherIndex),
                        cursor.getInt(quantityIndex), cursor.getInt(availableIndex) == 1));
            }
        }
        return books;
    }

    public boolean addMember(String name, String email, String phone, String address) {
        ContentValues values = new ContentValues();
        values.put(MEMBER_NAME, name);
        values.put(MEMBER_EMAIL, email);
        values.put(MEMBER_PHONE, phone);
        values.put(MEMBER_ADDRESS, address);
        return getWritableDatabase().insert(TABLE_MEMBER, null, values) != -1;
    }

    public Cursor getAllMembers() {
        return getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_MEMBER + " ORDER BY " + MEMBER_NAME, null);
    }

    public List<MemberModel> getAllMembersList() {
        List<MemberModel> members = new ArrayList<>();
        try (Cursor cursor = getAllMembers()) {
            int idIndex = cursor.getColumnIndexOrThrow(MEMBER_ID);
            int nameIndex = cursor.getColumnIndexOrThrow(MEMBER_NAME);
            int emailIndex = cursor.getColumnIndexOrThrow(MEMBER_EMAIL);
            int phoneIndex = cursor.getColumnIndexOrThrow(MEMBER_PHONE);
            int addressIndex = cursor.getColumnIndexOrThrow(MEMBER_ADDRESS);
            while (cursor.moveToNext()) {
                members.add(new MemberModel(cursor.getInt(idIndex), cursor.getString(nameIndex),
                        cursor.getString(emailIndex), cursor.getString(phoneIndex), cursor.getString(addressIndex)));
            }
        }
        return members;
    }

    public Cursor getMemberByName(String memberName) {
        return getReadableDatabase().query(TABLE_MEMBER,
                new String[]{MEMBER_ID, MEMBER_NAME, MEMBER_EMAIL, MEMBER_PHONE, MEMBER_ADDRESS},
                MEMBER_NAME + " = ?", new String[]{memberName}, null, null, null);
    }

    public boolean updateMember(int id, String name, String email, String phone, String address) {
        ContentValues values = new ContentValues();
        values.put(MEMBER_NAME, name);
        values.put(MEMBER_EMAIL, email);
        values.put(MEMBER_PHONE, phone);
        values.put(MEMBER_ADDRESS, address);
        return getWritableDatabase().update(TABLE_MEMBER, values, MEMBER_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteMember(int id) {
        return getWritableDatabase().delete(TABLE_MEMBER, MEMBER_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean reserveBook(int bookId, String memberName) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try (Cursor cursor = db.query(BOOK_TABLE, new String[]{BOOK_QUANTITY}, BOOK_ID + " = ?",
                new String[]{String.valueOf(bookId)}, null, null, null)) {
            if (!cursor.moveToFirst() || cursor.getInt(cursor.getColumnIndexOrThrow(BOOK_QUANTITY)) <= 0) {
                return false;
            }
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(BOOK_QUANTITY));
            ContentValues bookValues = new ContentValues();
            bookValues.put(BOOK_QUANTITY, quantity - 1);
            bookValues.put(BOOK_AVAILABLE, quantity - 1 > 0 ? 1 : 0);
            db.update(BOOK_TABLE, bookValues, BOOK_ID + " = ?", new String[]{String.valueOf(bookId)});

            ContentValues reserveValues = new ContentValues();
            reserveValues.put(RESERVE_BOOK_ID, bookId);
            reserveValues.put(RESERVE_MEMBER_NAME, memberName);
            boolean inserted = db.insert(RESERVE_TABLE, null, reserveValues) != -1;
            if (inserted) db.setTransactionSuccessful();
            return inserted;
        } finally {
            db.endTransaction();
        }
    }

    public Cursor getAllReservedBooks() {
        String query = "SELECT " + BOOK_TABLE + "." + BOOK_NAME + ", "
                + BOOK_TABLE + "." + BOOK_AUTHOR + ", "
                + BOOK_TABLE + "." + BOOK_PUBLISHER + ", "
                + RESERVE_TABLE + "." + RESERVE_MEMBER_NAME
                + " FROM " + BOOK_TABLE + " INNER JOIN " + RESERVE_TABLE
                + " ON " + BOOK_TABLE + "." + BOOK_ID + " = " + RESERVE_TABLE + "." + RESERVE_BOOK_ID
                + " ORDER BY " + RESERVE_TABLE + "." + RESERVE_ID + " DESC";
        return getReadableDatabase().rawQuery(query, null);
    }

    public List<ReservedBookModel> getAllReservedBooksList() {
        List<ReservedBookModel> items = new ArrayList<>();
        try (Cursor cursor = getAllReservedBooks()) {
            int nameIndex = cursor.getColumnIndexOrThrow(BOOK_NAME);
            int authorIndex = cursor.getColumnIndexOrThrow(BOOK_AUTHOR);
            int publisherIndex = cursor.getColumnIndexOrThrow(BOOK_PUBLISHER);
            int memberIndex = cursor.getColumnIndexOrThrow(RESERVE_MEMBER_NAME);
            while (cursor.moveToNext()) {
                items.add(new ReservedBookModel(cursor.getString(nameIndex), cursor.getString(authorIndex),
                        cursor.getString(publisherIndex), cursor.getString(memberIndex)));
            }
        }
        return items;
    }

    public boolean returnBook(String bookName) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            int deleted = db.delete(RESERVE_TABLE, RESERVE_ID + " = (SELECT " + RESERVE_ID + " FROM "
                    + RESERVE_TABLE + " WHERE " + RESERVE_BOOK_ID + " = (SELECT " + BOOK_ID + " FROM "
                    + BOOK_TABLE + " WHERE " + BOOK_NAME + " = ?) LIMIT 1)", new String[]{bookName});
            if (deleted == 0) return false;
            db.execSQL("UPDATE " + BOOK_TABLE + " SET " + BOOK_QUANTITY + " = " + BOOK_QUANTITY + " + 1, "
                    + BOOK_AVAILABLE + " = 1 WHERE " + BOOK_NAME + " = ?", new Object[]{bookName});
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    public List<String> getBookSuggestions(String query) {
        List<String> suggestions = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().query(BOOK_TABLE, new String[]{BOOK_NAME},
                BOOK_NAME + " LIKE ?", new String[]{"%" + query + "%"}, null, null, BOOK_NAME + " LIMIT 10")) {
            while (cursor.moveToNext()) {
                suggestions.add(cursor.getString(cursor.getColumnIndexOrThrow(BOOK_NAME)));
            }
        }
        return suggestions;
    }

    public boolean saveFavorite(OnlineBook book) {
        ContentValues values = new ContentValues();
        values.put(FAVORITE_KEY, book.getKey());
        values.put(FAVORITE_TITLE, book.getTitle());
        values.put(FAVORITE_AUTHOR, book.getAuthor());
        values.put(FAVORITE_YEAR, book.getFirstPublishYear());
        return getWritableDatabase().insertWithOnConflict(FAVORITES_TABLE, null, values,
                SQLiteDatabase.CONFLICT_REPLACE) != -1;
    }
}
