# Global Spectator Chat

A small fabric add-on for Simple Voice Chat that adds a global spectator chat.

In the event that a player enters spectator mode, only other spectators can hear this player. Spectator audio is non-directional (like groups).

Speaking of groups, the way groups interact with this mod can be configured.

*All of these descriptions assume that you are a spectator*

| mode         | description                                                                                                                                    |
|--------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `GROUP_ONLY` | Only other spectators *that are in the same group as you* can hear you. If you are not in any groups, the same behavior as EXCLUDE will apply. |
| `INCLUDE`    | All spectators in the game can hear you, and all people within the same group as you can also hear you (even if they are not a spectator)      |
| `EXCLUDE`    | All spectators in the game can hear you. Groups are ignored, so you can hear all spectators no matter in which group you or they are.          |

The configuration file can be found in `./config/voicechat/voicechat-spectator.properties`