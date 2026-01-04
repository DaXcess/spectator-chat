package io.daxcess.spectatorchat;

import com.mojang.brigadier.CommandDispatcher;
import de.maxhenkel.voicechat.api.ForgeVoicechatPlugin;
import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import io.daxcess.spectatorchat.commands.SpectatorChatCommand;
import io.daxcess.spectatorchat.data.GroupMode;
import io.daxcess.spectatorchat.data.SpectatorChatData;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

@ForgeVoicechatPlugin
public class SpectatorChatPlugin implements VoicechatPlugin {
    @Override
    public String getPluginId() {
        return "global-spectator-vc";
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophonePacket);
    }

    private void onMicrophonePacket(MicrophonePacketEvent event) {
        var service = SpectatorChatRegistry.getService();
        if (service == null)
            return;

        if (!SpectatorChatData.get(service.getServer()).isEnabled())
            return;

        if (event.getSenderConnection() == null)
            return;

        var player = service.getConnectionPlayer(event.getSenderConnection());
        if (player == null)
            return;

        // Only broadcast if we're a spectator
        if (!player.isSpectator())
            return;

        event.cancel();

        var group = event.getSenderConnection().getGroup();
        var api = event.getVoicechat();

        for (var serverPlayer : service.getServerPlayers())
        {
            // Don't send to self
            if (serverPlayer.getUUID().equals(player.getUUID()))
                continue;

            // Ignore missing connections (e.g. mod not installed)
            var connection = api.getConnectionOf(serverPlayer.getUUID());
            if (connection == null)
                continue;

            // Send the audio if the group mode criteria is met
            if (shouldSendAudioPacket(service, serverPlayer, connection, group))
                api.sendStaticSoundPacketTo(connection, event.getPacket().staticSoundPacketBuilder().build());
        }
    }

    private boolean shouldSendAudioPacket(SpectatorChatService service, ServerPlayer targetPlayer, VoicechatConnection receiver, Group senderGroup) {
        var mode = SpectatorChatData.get(service.getServer()).getGroupMode();

        switch (mode) {
            // Only send audio to fellow spectating group members
            case GroupMode.GROUP_ONLY -> {
                // Target must also be a spectator
                if (!targetPlayer.isSpectator())
                    return false;

                // If we're both not in a group, we can send our audio
                if (senderGroup == null && receiver.getGroup() == null)
                    return true;

                // If we're both in the same group, we can send our audio, otherwise just return
                return senderGroup.getId().equals(receiver.getGroup().getId());
            }

            // Hear our group members and other spectators
            case GroupMode.GROUP_PLUS_SPECTATORS -> {
                // Allow non-spectator communication if groups equal (not being in a group does NOT count to being equal)
                if (!targetPlayer.isSpectator())
                    return senderGroup != null && receiver.getGroup() != null && senderGroup.getId().equals(receiver.getGroup().getId());

                return true;
            }

            // You can only hear other spectators, groups are ignored
            case GroupMode.SPECTATOR_ONLY -> {
                return targetPlayer.isSpectator();
            }
        }

        // You messed up the group mode, silence for you!
        return false;
    }

    public static void onRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        SpectatorChatCommand.register(dispatcher, ctx -> SpectatorChatRegistry.getService().hasCommandAccess(ctx));
    }
}