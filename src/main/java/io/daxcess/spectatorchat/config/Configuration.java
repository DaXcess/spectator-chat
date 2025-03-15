package io.daxcess.spectatorchat.config;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.configbuilder.entry.ConfigEntry;

public class Configuration {

    public ConfigEntry<GroupMode> groupMode;

    public Configuration(ConfigBuilder builder) {
        builder.header("Simple Voice Chat - Global Spectator Chat configuration");

        groupMode = builder
                .enumEntry("group_mode", GroupMode.INCLUDE,
                        "How the add-on should behave when the user is in a group",
                        "GROUP_ONLY - When you are a spectator, only other spectators within your group can hear you",
                        "INCLUDE - When you are a spectator, all other people in the group (including non-spectators) can still hear you",
                        "EXCLUDE - When you are a spectator, nobody in your group can hear you, unless they are also a spectator"
                );
    }

    public enum GroupMode {
        GROUP_ONLY,
        INCLUDE,
        EXCLUDE
    }
}
