# Firebase setup (required only for cloud mode)

The application builds and runs in local SQLite mode without Firebase credentials. To activate Firebase Authentication and Firestore:

1. Open Firebase Console and create a project.
2. Add an Android application with package name:

   `com.example.librarymanagement`

3. In Firebase Authentication, enable **Email/Password**.
4. Create a Cloud Firestore database.
5. Open **Project settings > General > Your apps > SDK setup and configuration**.
6. In `app/src/main/res/values/strings.xml`, replace:

```xml
<string name="firebase_web_api_key" translatable="false">REPLACE_FIREBASE_WEB_API_KEY</string>
<string name="firebase_application_id" translatable="false">REPLACE_FIREBASE_ANDROID_APP_ID</string>
<string name="firebase_project_id" translatable="false">REPLACE_FIREBASE_PROJECT_ID</string>
```

No `google-services.json` is required in this implementation because `FirebaseApp` is initialized manually through `FirebaseOptions`.

## Suggested Firestore rules

```text
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }

    match /favorites/{userId}/books/{bookId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

## How to verify Firebase mode

- Create a new account from Register.
- Login with the account.
- The dashboard displays that Firebase mode is active.
- Open Online Books and save a favorite.
- Verify the document under `favorites/{uid}/books` in Firestore.
- Test Forgot Password from the Login screen.
