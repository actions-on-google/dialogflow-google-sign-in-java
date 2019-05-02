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

import com.google.actions.api.ActionRequest;
import com.google.actions.api.ActionResponse;
import com.google.actions.api.DialogflowApp;
import com.google.actions.api.ForIntent;
import com.google.actions.api.response.ResponseBuilder;
import com.google.actions.api.response.helperintent.SignIn;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.cloud.Timestamp;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tip: Sign In should not happen in the Default Welcome Intent, instead
 * later in the conversation.
 * See `Action discovery` docs:
 * https://developers.google.com/actions/discovery/implicit#action_discovery
 */

public class MyActionsApp extends DialogflowApp {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(MyActionsApp.class);
  private final TokenDecoder tokenDecoder;

  public MyActionsApp() {
    tokenDecoder = new TokenDecoder(Constants.clientId);
  }

  @ForIntent("Default Welcome Intent")
  public ActionResponse welcome(ActionRequest request) {
    LOGGER.info("Welcome intent start.");
    ResponseBuilder responseBuilder = getResponseBuilder(request);
    ResourceBundle rb = ResourceBundle.getBundle("resources");
    if (userIsSignedIn(request)) {
      GoogleIdToken.Payload profile = getUserProfile(
          request.getUser().getIdToken());
      responseBuilder.add(String
          .format(rb.getString("welcome_back"), profile.get("given_name")));
      String color = null;
      try {
        FirestoreManager firestoreManager =
            new FirestoreManager(Constants.databaseUrl, profile.getEmail());
        color = firestoreManager.readUserColor();
      } catch (Exception e) {
        LOGGER.error(e.toString());
      }
      LOGGER.info(String.format("Color: %s", color));
      responseBuilder
          .add(String.format(rb.getString("color_question_back"), color));
    } else {
      responseBuilder.add(String.format(rb.getString("welcome"), ""));
      responseBuilder.add(rb.getString("color_question"));
    }
    responseBuilder.addSuggestions(new String[]{"Red", "Green", "Blue"});
    LOGGER.info("Welcome intent end.");
    return responseBuilder.build();
  }

  @ForIntent("Give Color")
  public ActionResponse giveColor(ActionRequest request) {
    LOGGER.info("Give color intent start.");
    ResponseBuilder responseBuilder = getResponseBuilder(request);
    ResourceBundle rb = ResourceBundle.getBundle("resources");
    String color = request.getParameter(Constants.colorFieldName).toString();
    LOGGER.info(String.format("Color: %s.", color));
    responseBuilder.getConversationData().put(Constants.colorFieldName, color);
    if (userIsSignedIn(request)) {
      GoogleIdToken.Payload profile = getUserProfile(
          request.getUser().getIdToken());
      try {
        FirestoreManager firestoreManager =
            new FirestoreManager(Constants.databaseUrl, profile.getEmail());
        saveColor(firestoreManager, color);
      } catch (Exception e) {
        LOGGER.error(e.toString());
      }
      responseBuilder
          .add(String.format(rb.getString("got_color"), color))
          .add(rb.getString("next_time_prompt"))
          .endConversation();
      LOGGER.info("Give color intent end.");
      return responseBuilder.build();
    }
    responseBuilder.add(
        new SignIn()
            .setContext(String.format(rb.getString("signin_prompt"), color)));
    LOGGER.info("Give color intent end.");
    return responseBuilder.build();
  }

  @ForIntent("Get Sign In")
  public ActionResponse getSignIn(ActionRequest request) {
    LOGGER.info("Get sign in intent start.");
    ResponseBuilder responseBuilder = getResponseBuilder(request);
    ResourceBundle rb = ResourceBundle.getBundle("resources");
    if (request.isSignInGranted()) {
      String color = request.getConversationData().get(Constants.colorFieldName)
          .toString();
      GoogleIdToken.Payload profile = getUserProfile(
          request.getUser().getIdToken());
      try {
        FirestoreManager firestoreManager =
            new FirestoreManager(Constants.databaseUrl, profile.getEmail());
        saveColor(firestoreManager, color);
      } catch (Exception e) {
        LOGGER.error(e.toString());
      }
      responseBuilder
          .add(String.format(rb.getString("got_color"), color))
          .add(rb.getString("next_time_prompt"))
          .endConversation();
    } else {
      responseBuilder.add(rb.getString("bye")).endConversation();
    }
    LOGGER.info("Get sign in intent end.");
    return responseBuilder.build();
  }

  private boolean userIsSignedIn(ActionRequest request) {
    String idToken = request.getUser().getIdToken();
    LOGGER.info(String.format("Id token: %s", idToken));
    if (idToken == null || idToken.isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  private GoogleIdToken.Payload getUserProfile(String idToken) {
    GoogleIdToken.Payload profile = null;
    try {
      profile = tokenDecoder.decodeIdToken(idToken);
    } catch (Exception e) {
      LOGGER.error("error decoding idtoken");
      LOGGER.error(e.toString());
    }
    return profile;
  }

  private void saveColor(FirestoreManager firestoreManager, String color) {
    try {
      Timestamp updateTime = firestoreManager.writeUserColor(color);
      LOGGER.info(String.format("Update time: %s", updateTime.toString()));
    } catch (Exception e) {
      LOGGER.error(e.toString());
    }
  }
}
