package io.daxcess.spectatorchat;

import java.util.Objects;

public final class SpectatorChatRegistry {
    private static SpectatorChatService spectatorChatService;

    public static void register(SpectatorChatService service)
    {
        if (spectatorChatService != null)
            throw new IllegalStateException("Service already registered");

        spectatorChatService = Objects.requireNonNull(service);
    }

    public static SpectatorChatService getService() {
        return spectatorChatService;
    }
}
