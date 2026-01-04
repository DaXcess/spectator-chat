package io.daxcess.spectatorchat;

import de.maxhenkel.voicechat.api.VoicechatConnection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SpectatorChatService {
    MinecraftServer getServer();

    List<ServerPlayer> getServerPlayers();

    @Nullable ServerPlayer getConnectionPlayer(VoicechatConnection connection);

    boolean hasCommandAccess(CommandSourceStack ctx);
}
