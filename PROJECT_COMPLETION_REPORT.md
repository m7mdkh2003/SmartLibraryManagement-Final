# Smart Library Management — Completion Report

## Team size and screen requirement

Team size: 2 students  
Minimum required: 14 screens  
Implemented: 16 Activities plus 4 practical Fragments

## Activity screens

1. Splash — `MainActivity`
2. Login — `Login`
3. Register — `Register`
4. Fragment dashboard host — `Home`
5. Books menu — `Book`
6. Add Book — `AddBook`
7. View Books — `ViewBook`
8. Edit Book — `EditBook`
9. Members menu — `Member`
10. Add Member — `AddMember`
11. View Members — `ViewMember`
12. Edit Member — `EditMember`
13. Reserve Book — `ReservationActivity`
14. Reserved Books — `ViewReservedBook`
15. Return Book Details — `BookDetailsActivity`
16. Online Books — `OnlineBooksActivity`

## Student distribution

### Student 1

- Splash
- Login
- Register
- Home and Dashboard Fragment
- Books menu
- Add Book
- View Books
- Edit Book

Main topics: Shared Preferences, Firebase Authentication, RecyclerView, SQLite, Threads, MVC and Fragments.

### Student 2

- Members menu
- Add Member
- View Members
- Edit Member
- Reserve Book
- Reserved Books
- Return Book Details
- Online Books

Main topics: ListView Custom Adapter, SQLite, Threads, MVP, MVVM, Web Services, Firebase Firestore and Fragments.

## Course-topic mapping

| Course topic | Implementation |
|---|---|
| ListView with Custom Adapter | `ViewReservedBook` + `ReservedBookListAdapter extends BaseAdapter` |
| RecyclerView | Local books, members and online books |
| Shared Preferences | `SessionManager`, Remember Me and saved email |
| Threads | `AppExecutors` for all SQLite and web-service operations |
| SQLite | Users fallback, books, members, reservations and local favorites |
| Design Patterns | Adapter, Singleton, Repository, Observer, MVC, MVP and MVVM |
| MVC | Local Books: Models + Activities + `BookController` |
| MVP | Members: `MemberContract`, `MemberPresenter`, `MemberRepository`, `ViewMember` |
| MVVM | Online Books: `OnlineBooksActivity`, `OnlineBooksViewModel`, `OnlineBookRepository` |
| Firebase | Manual Firebase initialization and Firestore favorites/profile |
| Firebase Authentication | Login, registration and password reset when configured |
| Web Services | Open Library REST API using HTTP GET and JSON parsing |
| Fragments theoretical/practical | Four Fragments hosted by `Home` and selected through BottomNavigationView |

## Architecture overview

```text
Local Books (MVC)
Activity View -> BookController -> DbHelper -> SQLite

Members (MVP)
ViewMember <-> MemberPresenter -> MemberRepository -> DbHelper

Online Books (MVVM)
OnlineBooksActivity observes OnlineBooksViewModel
OnlineBooksViewModel -> OnlineBookRepository -> Open Library / Firestore / SQLite
```

## Required acceptance tests

1. Register and login locally while Firebase values are placeholders.
2. Select Remember Me, close the app, and verify automatic dashboard opening.
3. Add, view, edit and delete a book.
4. Add, view, edit and delete a member.
5. Reserve an available book and verify its quantity decreases.
6. Open Reserved Books and verify the ListView custom row.
7. Return the book and verify its quantity increases.
8. Search Open Library and display JSON results in RecyclerView.
9. Save an online book to local favorites.
10. Configure Firebase, repeat registration/login, reset password and save a Firestore favorite.
11. Navigate across all four practical Fragments.
12. Rotate screens and verify that the Online Books ViewModel retains results.
