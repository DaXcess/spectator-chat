package io.daxcess.spectatorchat;

import de.maxhenkel.voicechat.api.VoicechatConnection;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionLevel;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SpectatorChatFabric implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_ID);

    @Nullable
    public static MinecraftServer SERVER;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> SERVER = server);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> SERVER = null);

        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> SpectatorChatPlugin.onRegisterCommands(dispatcher)));

        SpectatorChatRegistry.register(new SpectatorChatService() {
            @Override
            public MinecraftServer getServer() {
                return SERVER;
            }

            @Override
            public List<ServerPlayer> getServerPlayers() {
                if (SERVER == null)
                    return List.of();

                return SERVER.getPlayerList().getPlayers();
            }

            @Override
            public @Nullable ServerPlayer getConnectionPlayer(VoicechatConnection connection) {
                if (connection.getPlayer().getPlayer() instanceof ServerPlayer player)
                    return player;

                return null;
            }

            @Override
            public boolean hasCommandAccess(CommandSourceStack ctx) {
                return Permissions.check(ctx, Constants.PERMISSION_COMMAND, PermissionLevel.GAMEMASTERS);
            }
        });

        LOGGER.info("Global Spectator Voicechat plugin loaded and registered");
    }
}