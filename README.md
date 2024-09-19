# Twitch Streamer Live Notification Plugin
![Spigot](https://img.shields.io/badge/Spigot-1.20--1.21.1-yellow.svg)
![Paper](https://img.shields.io/badge/PaperMC-1.20--1.21.1-blue.svg)
![Version](https://img.shields.io/badge/Version-1.0-gray.svg)
![MIT License](https://img.shields.io/badge/License-MIT-green.svg)


## Overview
This Spigot plugin notifies players in Minecraft when specific Twitch streamers go live. The notifications can be displayed as chat messages or titles in-game. Additionally, a sound can be played when a streamer starts streaming.

## Features
Monitors multiple Twitch streamers for live status.
Configurable notifications via chat or title.
Plays a sound when a streamer goes live.
Notifies players when they join if a streamer is currently live.
## Requirements
- Spigot 1.20.4 or higher
- Java 17 or higher
- A Twitch Developer account for Client ID and [Access Token](GetToken.md)

## Installation
Download the plugin JAR file and place it in your Minecraft server's plugins folder.
Restart your server to generate the default configuration file.
Configuration
Edit the ``config.yml`` file in the plugins/isstreamerlive directory:

```yaml
twitch:
  notification_title: "Now Live!"
  check_interval: 60  # in seconds
  APIClientID: "YOUR_TWITCH_CLIENT_ID"
  TwitchAccessToken: "YOUR_TWITCH_ACCESS_TOKEN"
  streamers:
    - streamer_name: "streamer"
      streamer_showed_name: "Streamer"
      notification_type: "title"  # or "chat"
      notification_text: "is live on Twitch!"
      played_sound: "ENTITY_PLAYER_LEVELUP"
    - streamer_name: "otherstreamer"
      streamer_showed_name: "OtherStreamer"
      notification_type: "chat"
      notification_text: "is live on Twitch!"
      played_sound: "ENTITY_PLAYER_LEVELUP"
````

- **notification_title:** The title text shown in-game when a streamer goes live.
- **check_interval:** How often the plugin checks if the streamers are live, in seconds.
- **APIClientID and TwitchAccessToken:** Your Twitch Developer credentials.
- **streamers:** List of streamers to monitor. For each streamer, provide:
- **streamer_name:** The Twitch username.
- **streamer_showed_name:** The name to display in notifications.
- **notification_type:** The type of notification ("chat" or "title").
- **notification_text:** The text to include in the notification message.
- **played_sound:** The sound that's played when the streamer goes live.


## Commands
There are no commands available for this plugin.

## Permissions
This plugin does not use any specific permissions.

## Troubleshooting
Ensure your Twitch Client ID and Access Token are correct.
Check your server's console for any errors related to HTTP requests or configuration issues.
Verify that your server can access the Twitch API and that the API rate limits are not exceeded.

## Development
If you want to contribute or modify the plugin, you can clone the repository:
```bash
git clone https://github.com/CptGummiball/isstreamerlive.git
```
### Building the Plugin
1. Make sure you have Maven installed.

2. Navigate to the project directory and run:
```bash
mvn clean package
```
3. The compiled isstreamerlive.jar file will be available in the target/ directory.


## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact
For any questions, suggestions, or issues, feel free to open an issue on the [GitHub Issues](https://github.com/CptGummiball/isstreamerlive/issues) page.