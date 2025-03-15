package io.daxcess.spectatorchat;

import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import net.minecraft.server.network.ServerPlayerEntity;

public class SpectatorChatPlugin implements VoicechatPlugin {

    @Override
    public String getPluginId() {
        return "svc-global-spectator";
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophonePacket);
    }

    private void onMicrophonePacket(MicrophonePacketEvent event) {
        if (event.getSenderConnection() == null)
            return;

        // Ignore if we have no reference to the server
        if (SpectatorChatMod.SERVER == null)
            return;

        if (!(event.getSenderConnection().getPlayer().getPlayer() instanceof ServerPlayerEntity player))
            return;

        // Only broadcast if we're a spectator
        if (!player.isSpectator())
            return;

        event.cancel();

        var group = event.getSenderConnection().getGroup();
        var api = event.getVoicechat();

        for (var serverPlayer : SpectatorChatMod.SERVER.getPlayerManager().getPlayerList()) {
            // Don't send to self
            if (serverPlayer.getUuid().equals(player.getUuid()))
                continue;

            var connection = api.getConnectionOf(serverPlayer.getUuid());
            if (connection == null)
                continue;

            switch (SpectatorChatMod.CONFIG.groupMode.get()) {
                case GROUP_ONLY: // Only send audio to group members that are also dead
                    // Spectators only
                    if (!serverPlayer.isSpectator())
                        continue;

                    // If we are not in a group, fall back to EXCLUDE behavior
                    if (group == null)
                        break;

                    // Must be in the same group
                    if (connection.getGroup() == null || !group.getId().equals(connection.getGroup().getId()))
                        continue;

                    break;

                case INCLUDE: // Send audio to other spectators and to members of the same group (if in any)
                    // Include group members
                    if (!serverPlayer.isSpectator())
                        if (group == null || connection.getGroup() == null || !group.getId().equals(connection.getGroup().getId()))
                            continue;

                    break;

                case EXCLUDE: // Only send audio to dead players (groups are ignored)
                    if (!serverPlayer.isSpectator())
                        continue;

                    break;
            }

            api.sendStaticSoundPacketTo(connection, event.getPacket().staticSoundPacketBuilder().build());
        }
    }
}
