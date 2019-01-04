/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirestoreManager {

  private final Firestore db;
  private final DocumentReference userDocRef;
  private final String uid;

  public FirestoreManager(String databaseUrl, String email)
      throws IOException, FirebaseAuthException {
    if (FirebaseApp.getApps().isEmpty()) {
      // Use the application default credentials (works on GCP based hosting).
      FirebaseOptions options =
          new FirebaseOptions.Builder()
              .setCredentials(GoogleCredentials.getApplicationDefault())
              .setDatabaseUrl(databaseUrl)
              .build();
      FirebaseApp.initializeApp(options);
    }
    this.db = FirestoreClient.getFirestore();

    UserRecord userRecord;
    try {
      userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
    } catch (FirebaseAuthException e) {
      if (e.getErrorCode() == Constants.firebaseUserNotFoundError) {
        UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
            .setEmail(email);
        userRecord = FirebaseAuth.getInstance().createUser(createRequest);
      } else {
        throw e;
      }
    }
    uid = userRecord.getUid();
    userDocRef = db.collection(Constants.firestoreUsersPath).document(uid);
  }

  public String readUserColor()
      throws ExecutionException, InterruptedException {
    ApiFuture<DocumentSnapshot> future = userDocRef.get();
    // future.get() blocks on response
    DocumentSnapshot document = future.get();
    if (document.exists()) {
      return document.get(Constants.colorFieldName).toString();
    } else {
      return "";
    }
  }

  public Timestamp writeUserColor(String color)
      throws ExecutionException, InterruptedException {
    Map<String, Object> docData = new HashMap<>();
    docData.put(Constants.colorFieldName, color);
    ApiFuture<WriteResult> future = userDocRef.set(docData);
    // future.get() blocks on response
    return future.get().getUpdateTime();
  }
}
