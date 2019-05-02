# Actions on Google: Account Linking with Google Sign-In Sample

This sample demonstrates Actions on Google features for use on Google Assistant including account linking and [Google Sign In](https://developers.google.com/actions/identity/google-sign-in) -- using the [Java client library](https://github.com/actions-on-google/actions-on-google-java), [Firebase Authentication](https://firebase.google.com/docs/auth/), and deployed on [App Engine](https://cloud.google.com/appengine/docs/standard/java/quickstart).

### Setup Instructions
### Prerequisites
1. Download & install the [Google Cloud SDK](https://cloud.google.com/sdk/docs/)
1. [Gradle with App Engine Plugin](https://cloud.google.com/appengine/docs/flexible/java/using-gradle)
   + Run `gcloud auth application-default login` with your Google account
   + Install and update the App Engine component,`gcloud components install app-engine-java`
   + Update other components, `gcloud components update`

### Configuration
#### Actions Console
1. From the [Actions on Google Console](https://console.actions.google.com/), add a new project (this will become your *Project ID*) > **Create Project** > under **More options** > **Conversational**.
1. On the left navigation menu under **Advanced Options** > **Account linking**:
   + **Account creation** > select `Yes, allow users to sign up for new accounts via voice`.
   + **Linking type** > select `Google Sign In`.
   + **Client information** > copy **Client ID** > **Save**.
1. In `src/main/java/com/example/Constants.java`
   + Update `clientId` to the value copied in the previous step
   + Update `databaseUrl` to `https://<YOUR-PROJECT-ID>.firebaseio.com` with your *Project ID*.
1. From the left navigation menu under **Build** > **Actions** > **Add Your First Action** > **BUILD** (this will bring you to the Dialogflow console) > Select language and time zone > **CREATE**.
1. In Dialogflow, go to **Settings** ⚙ > **Export and Import** > **Restore from zip**.
   + Follow the directions to restore from the `agent.zip` file in this repo.

### Firestore Database
1. From the [Firebase console](https://console.firebase.google.com), find and select your Actions on Google Project ID
1. In the left navigation menu under **Develop** section > **Database** > **Create database** button > Select **Start in test mode** > **Enable**
1. Under **Develop** > **Authentication** > **Sign in method** tab > **Sign In Providers**:
   + Select **Google** > **Enable**
   + **Web SDK configuration** section > copy the **Web client ID** value and replace the `<CLIENT-ID>` placeholder string found in `src/main/webapp/webapp.html` with your web client ID.
   + Then **Save**
1. **Project Overview** > under **Add Firebase to your app** > select the **web icon** > copy the config contents into `src/main/webapp/webapp.html`

#### App Engine Deployment & Webhook Configuration
When a new project is created using the Actions Console, it also creates a Google Cloud project in the background.
1. Configure the gcloud CLI and set your Google Cloud project to the name of your Actions on Google Project ID, which you can find from the [Actions on Google console](https://console.actions.google.com/) under Settings ⚙
   + `gcloud init`
1. Deploy to [App Engine using Gradle](https://cloud.google.com/appengine/docs/flexible/java/using-gradle):
   + `gradle appengineDeploy` OR
   +  From within IntelliJ, open the Gradle tray and run the appEngineDeploy task.

#### Dialogflow Console
Return to the [Dialogflow Console](https://console.dialogflow.com), from the left navigation menu under **Fulfillment** > **Enable Webhook**, set the value of **URL** to `https://<YOUR_PROJECT_ID>.appspot.com` > **Save**.
1. From the left navigation menu, click **Integrations** > **Integration Settings** under Google Assistant > Enable **Auto-preview changes** >  **Test** to open the Actions on Google simulator then say or type `Talk to my test app`.

### Configure Google Sign-In Credentials
1. In the Google Cloud Console [Credentials](https://console.cloud.google.com/apis/credentials) page > select your *Project ID* from the dropdown.
1. From the **OAuth consent screen** tab > under **Authorized domains** > add the domain for your deployed project, `<YOUR-PROJECT-ID>.appspot.com` > **Save**.
1. Back in the **Credentials** tab > **OAuth 2.0 client IDs** > select **Web client (auto created by Google Service)**:
    + Under **Authorized JavaScript origins**, add `https://<YOUR-PROJECT-ID>.appspot.com` with your *Project ID*.
    + Under **Authorized redirect URI**, add `https://<YOUR-PROJECT-ID>.appspot.com/webapp.html` with your *Project ID* > **Save**.

### Running this Sample
+ You can test your Action on any Google Assistant-enabled device on which the Assistant is signed into the same account used to create this project. Just say or type, “OK Google, talk to my test app”.
+ You can also use the Actions on Google Console simulator to test most features and preview on-device behavior.
+ A minimal web application is published at `https://<YOUR-PROJECT-ID>.appspot.com/webapp.html`, you can see the favorite color value.

### Troubleshooting
+ If running into issues after following the above steps, clear your browser's cache and make sure pop ups are allowed.

### References & Issues
+ Questions? Go to [StackOverflow](https://stackoverflow.com/questions/tagged/actions-on-google), [Assistant Developer Community on Reddit](https://www.reddit.com/r/GoogleAssistantDev/) or [Support](https://developers.google.com/actions/support/).
+ For bugs, please report an issue on Github.
+ Actions on Google [Documentation](https://developers.google.com/actions/extending-the-assistant)
+ [Webhook Boilerplate Template](https://github.com/actions-on-google/dialogflow-webhook-boilerplate-java) for Actions on Google.
+ More info about [Gradle & the App Engine Plugin](https://cloud.google.com/appengine/docs/flexible/java/using-gradle).
+ More info about deploying [Java apps with App Engine](https://cloud.google.com/appengine/docs/standard/java/quickstart).

### Make Contributions
Please read and follow the steps in the [CONTRIBUTING.md](CONTRIBUTING.md).

### License
See [LICENSE](LICENSE).

### Terms
Your use of this sample is subject to, and by using or downloading the sample files you agree to comply with, the [Google APIs Terms of Service](https://developers.google.com/terms/).
