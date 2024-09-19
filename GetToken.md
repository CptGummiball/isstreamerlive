# How to Obtain a Twitch Access Token

To interact with the Twitch API, you need an access token. This guide will walk you through obtaining an access token using the Client Credentials Flow.

## Prerequisites

- A Twitch account
- A registered Twitch application

## Step 1: Register Your Twitch Application

1. **Log in to the Twitch Developer Console:**
    - Go to [Twitch Developer Console](https://dev.twitch.tv/console/apps).

2. **Create a New Application:**
    - Click the **"Register Your Application"** button.
    - Fill in the application details:
        - **Name:** Choose a name for your application.
        - **OAuth Redirect URL:** Enter a valid HTTPS URL. If you don't have a real redirect URL, you can use a placeholder like `https://example.com`.
        - **Category:** Select an appropriate category.
    - Click **"Create"**.

3. **Note Down Your Client ID and Client Secret:**
    - After creating the application, you will be provided with a **Client ID** and **Client Secret**. Save these credentials as you will need them to request an access token.

## Step 2: Obtain an Access Token

1. **Prepare Your Request:**

   You will use the Client Credentials Flow to obtain an access token. You need to make an HTTP POST request to the Twitch OAuth server.

2. **Send the Request:**

   Use the following `curl` command to request an access token. Replace `YOUR_TWITCH_CLIENT_ID` and `YOUR_TWITCH_CLIENT_SECRET` with the values obtained from the Twitch Developer Console:

   ```bash
   curl -X POST "https://id.twitch.tv/oauth2/token" \
     -d "client_id=YOUR_TWITCH_CLIENT_ID" \
     -d "client_secret=YOUR_TWITCH_CLIENT_SECRET" \
     -d "grant_type=client_credentials"
   ````
   
3. **Response:**

The response will include the access token. It will look something like this:

```json
{
"access_token": "YOUR_ACCESS_TOKEN",
"expires_in": 3600,
"token_type": "bearer"
}
access_token: This is the token you will use to authenticate your API requests.
expires_in: The number of seconds until the token expires (e.g., 3600 seconds = 1 hour).
token_type: The type of token (usually "bearer").
````

4. **Store the Access Token:**

Save the access token securely. You will use this token to make authenticated requests to the Twitch API.