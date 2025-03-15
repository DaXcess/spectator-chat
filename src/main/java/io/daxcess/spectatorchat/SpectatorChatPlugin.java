package io.daxcess.spectatorchat;

import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.PlayerConnectedEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SpectatorChatPlugin implements VoicechatPlugin {

    @Nullable
    public static VoicechatServerApi SERVER_API;
    public static Group GLOBAL_GROUP;

    @Override
    public void initialize(VoicechatApi api) {

    }

    @Override
    public String getPluginId() {
        return "svc-global-spectator";
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
        registration.registerEvent(PlayerConnectedEvent.class, this::onPlayerConnected);
    }

    private void onServerStarted(VoicechatServerStartedEvent event) {
        SERVER_API = event.getVoicechat();
        GLOBAL_GROUP = event.getVoicechat().groupBuilder().setHidden(true).setName("Spectator Chat").setPersistent(true).build();
    }

    private void onPlayerConnected(PlayerConnectedEvent event) {
        if (SpectatorChatMod.SERVER == null)
            return;

        var uuid = event.getConnection().getPlayer().getUuid();
        var player = SpectatorChatMod.SERVER.getPlayerManager().getPlayer(uuid);

        if (player == null)
            return;

        changePlayerGlobalGroup(player.getUuid(), player.isSpectator());
    }

    public static void changePlayerGlobalGroup(UUID id, boolean join) {
        if (SERVER_API == null)
            return;

        var connection = SERVER_API.getConnectionOf(id);
        if (connection == null)
            return;

        connection.setGroup(join ? GLOBAL_GROUP : null);
    }
}
