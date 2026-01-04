# Global Spectator Voice Chat

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)

An addon for the "Simple Voice Chat" mod that enhances the spectator experience in Minecraft.

This server-sided mod provides a dedicated communication channel for players in spectator mode. When you switch to spectator mode, you can freely communicate with all other spectators on the server, regardless of your distance, dimension, or location. The voice chat is non-positional, ensuring that you can always hear everyone clearly.

## Configuration

This mod provides commands to control its functionality. You need to be OP or have access to command blocks to be able to run these commands. If you are using any permission plugin, you require the `global_spectator_vc.permission.command` permission to be able to execute these commands.

### Commands

*   `/spectatorchat enable`
    *   Enables the global spectator voice chat functionality. (Enabled by default)
*   `/spectatorchat disable`
    *   Disables the global spectator voice chat functionality.
*   `/spectatorchat groupmode <mode>`
    *   Changes how spectators interact with voice chat groups.
    *   Replace `<mode>` with one of the following options:
        *   `group_only`: Spectators can only hear and speak with other spectator members of their Simple Voice Chat group. Non-spectator group members will **not** hear the spectators in their own group. (Default)
        *   `group_plus_spectators`: Spectators can communicate with their group normally, but also hear and speak with all other spectators regardless of group.
        *   `spectator_only`: Spectators ignore all group functionality. They can only hear and speak with other spectators. Group functionality is effectively disabled while spectating.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
