# YouTube project and code explanation script

Recommended duration: 25–35 minutes.

## 1. Introduction — 2 minutes

- Introduce both students.
- State the problem solved by Smart Library Management.
- Mention that the application exceeds the 14-screen minimum.
- Show the GitHub repository and branches/commits.

## 2. Full application demo — 8 minutes

- Splash, Register, Login and Remember Me.
- Home Bottom Navigation and four Fragments.
- Add, view, edit and delete books.
- Add, view, edit and delete members.
- Reserve a book, inspect Reserved Books and return it.
- Search Online Books and save a favorite.
- Demonstrate local mode, then Firebase mode if configured.

## 3. SQLite and Threads — 4 minutes

- Explain the five tables in `DbHelper`.
- Show CRUD methods and transactions for reserve/return.
- Explain why `AppExecutors.diskIO()` prevents UI blocking.
- Show `runOnMainThread()` for updating Android views.

## 4. ListView and RecyclerView — 3 minutes

- Show `ReservedBookListAdapter extends BaseAdapter`.
- Compare it with `LocalBookAdapter`, `MemberAdapter` and `OnlineBookAdapter` using RecyclerView.

## 5. Architecture and Design Patterns — 6 minutes

- MVC: local books and `BookController`.
- MVP: member contract, presenter, repository and view.
- MVVM: OnlineBooksActivity, ViewModel, LiveData and repository.
- Adapter, Singleton, Repository and Observer patterns.

## 6. Firebase — 4 minutes

- Show `FirebaseConfig` and manual `FirebaseOptions` initialization.
- Show Firebase email/password registration and login.
- Demonstrate Forgot Password.
- Show `users` and `favorites/{uid}/books` documents in Firestore.

## 7. Web Service — 3 minutes

- Explain REST, GET, JSON, status codes and network errors.
- Show the Open Library URL and `HttpURLConnection` code.
- Show JSON parsing into `OnlineBook` models.

## 8. Conclusion — 2 minutes

- Discuss challenges and fixes.
- Mention offline fallback, input validation and database transactions.
- Present possible future work: notifications, book covers and admin/user roles.
