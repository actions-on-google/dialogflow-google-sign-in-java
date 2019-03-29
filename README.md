# Actions on Google: Account Linking with Google Sign-In Sample using Java and Cloud Functions for Firebase

This sample shows you how to create, save, read, and link user data using
[Firebase Authentication](https://firebase.google.com/docs/auth/) and [Google Sign-In for the Assistant](https://developers.google.com/actions/identity/google-sign-in).

## Setup instructions

### Action configuration
1. From the [Actions on Google Console](https://console.actions.google.com/), add a new project (this will become your *Project ID*) > **Create Project**.
1. Scroll down to the **More Options** section, and click on the **Conversational** card.
1. On the left navigation menu under *ADVANCED OPTIONS*, click on *Account linking*
   1. Under *Account creation*, select `Yes, allow users to sign up for new accounts via voice`.
   1. Under *Linking type*, select `Google Sign In`.
   1. Under *Client information*, select and copy the text content in the box under *Client ID issued by Google to your Actions*, now referred to as `<CLIENT_ID>`.
   1. Open `src/main/java/com/example/Constants.java`, and set the value of `clientId` to the value copied in the previous step.
1. From the left navigation menu under **Build** > **Actions** > **Add Your First Action** > **BUILD** (this will bring you to the Dialogflow console) > Select language and time zone > **CREATE**.
1. In Dialogflow, go to **Settings** ⚙ > **Export and Import** > **Restore from zip**.
    + Follow the directions to restore from the `agent.zip` file in this repo.

### Firestore database configuration
1. Go to the [Firebase console](https://console.firebase.google.com) and select the project that you have created on the Actions on Google console.
   1. In the *Database* section, click *Create database* under `Cloud Firestore`.
   1. Click *Enable*. While testing this sample, you can keep the database world readable.
   1. Navigate to the *Authentication* section.
   1. Under the Sign in method tab,
      1. *Enable* the Google sign-in method.
      1. Under *Web SDK configuration*, copy the *Web client ID* value and replace the `<CLIENT-ID>` placeholder string found in `src/main/webapp/webapp.html` with your web client ID.
      1. Click *Save*.
   1. Make sure `One account per email address` is set to `Prevent creation of multiple accounts with the same email address` which should be selected by default.
   1. In the top right of the *Authentication* page, click the *Web setup* button.
      1. Copy and paste the provided code snippet for initializing Firebase into `src/main/webapp/webapp.html`, replacing the existing placeholder script tags used to initialize Firebase.
   1. Open `src/main/java/com/example/Constants.java`, and set the value of `databaseUrl` to the URL of your database. The URL should be of the form: `https://<YOUR-PROJECT-ID>.firebaseio.com`.

### App Engine deployment & webhook configuration
When a new project is created using the Actions Console, it also creates a Google Cloud project in the background.
1. Download & install the [Google Cloud SDK](https://cloud.google.com/sdk/docs/)
1. Configure the gcloud CLI and set your Google Cloud project to the name of your Actions on Google Project ID, which you can find from the [Actions on Google console](https://console.actions.google.com/) under Settings ⚙
    + `gcloud init`
    + `gcloud auth application-default login`
    + `gcloud components install app-engine-java`
    + `gcloud components update`
1. Deploy to [App Engine using Gradle](https://cloud.google.com/appengine/docs/flexible/java/using-gradle):
    + `gradle appengineDeploy` OR
    +  From within IntelliJ, open the Gradle tray and run the appEngineDeploy task.
1. Back in the [Dialogflow console](https://console.dialogflow.com), from the left navigation menu under **Fulfillment** > **Enable Webhook**, set the value of **URL** to `https://<YOUR_PROJECT_ID>.appspot.com` > **Save**.

### Configure Google Sign-in credentials
1. Visit the Google Cloud Console [Credentials page](https://console.cloud.google.com/apis/credentials), and select your project.
1. Navigate to the *OAuth consent screen* tab.
1. Under *Authorized domains*, add the domain for your deployed project. The domain should be of the form: `<YOUR-PROJECT-ID>.appspot.com`.
1. Navigate to the *Credentials* tab.
1. Under *OAuth 2.0 client IDs*, find the 'Web client (auto created by Google Service)' client ID, and click the edit button.
1. Under *Authorized JavaScript origins*, add the origin for your deployed project. The origin should be of the form: `https://<YOUR-PROJECT-ID>.appspot.com`.
1. Under *Authorized redirect URI*, add the redirect URI for your deployed project. The URI should be of the form: `https://<YOUR-PROJECT-ID>.appspot.com/webapp.html`.
1. Click *Save*

## Testing this sample
1. In the [Dialogflow console](https://console.dialogflow.com), from the left navigation menu > **Integrations** > **Integration Settings** under Google Assistant > Enable **Auto-preview changes** >  **Test** to open the Actions on Google simulator.
1. Type `Talk to my test app` in the simulator, or say `OK Google, talk to my test app` to Google Assistant on a mobile device associated with your Action's account.
1. After you have saved your favorite color, you can navigate to `<YOUR-PROJECT-ID>.appspot.com` to save and read your favorite color.
   1. You may need to allow popups in your browser for this page.
1. For developer testing to reset sign in for a user, fill out the `Invocation` and `Directory information` fields on the Actions Console, then you can go to the Action page on the [Actions directory](https://developers.google.com/actions/distribute/directory) with a phone even while it's not published and tap `Reset app`.
   1. Note that it make take some time to show up on the Actions directory.

## Testing the web application (optional)
A minimal web application is published at `<YOUR-PROJECT-ID>.appspot.com/webapp.html`. If you sign in with the same
Google account used from the Assistant, you can see the favorite color set using the Action.

## Troubleshooting
1. If sign-in hangs when testing the web application, clear your browser's cache and try again.
  1. If using Chrome: *Settings* > *Advanced* > *Clear browsing data* > *Cached images and files*

## References & Issues
+ Questions? Go to [StackOverflow](https://stackoverflow.com/questions/tagged/actions-on-google), [Assistant Developer Community on Reddit](https://www.reddit.com/r/GoogleAssistantDev/), or [Support](https://developers.google.com/actions/support/).
+ For bugs, please report an issue on Github.
+ Actions on Google [Documentation](https://developers.google.com/actions/).
+ More info about [Gradle & the App Engine Plugin](https://cloud.google.com/appengine/docs/flexible/java/using-gradle).
+ More info on deploying [Java apps with App Engine](https://cloud.google.com/appengine/docs/standard/java/quickstart).

## Make Contributions
Please read and follow the steps in the [CONTRIBUTING.md](CONTRIBUTING.md).

## License
See [LICENSE](LICENSE).

## Terms
Your use of this sample is subject to, and by using or downloading the sample files you agree to comply with, the [Google APIs Terms of Service](https://developers.google.com/terms/).
