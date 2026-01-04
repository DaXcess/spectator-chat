package io.daxcess.spectatorchat.data;

public enum GroupMode {
    /**
     * Spectators can only hear and speak with other spectator members of their group.
     * Non-spectator group members do NOT hear the spectators in their own group.
     */
    GROUP_ONLY,

    /**
     * Spectators can communicate with their group normally,
     * but also hear and speak with all other spectators regardless of group.
     */
    GROUP_PLUS_SPECTATORS,

    /**
     * Spectators ignore all group functionality.
     * They can only hear and speak with other spectators.
     * Group functionality is effectively disabled while spectating.
     */
    SPECTATOR_ONLY
}
