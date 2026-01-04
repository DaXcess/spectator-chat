package io.daxcess.spectatorchat;

import de.maxhenkel.voicechat.api.VoicechatConnection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mod(Constants.MOD_ID)
public class SpectatorChatNeoForge  {
    public static final PermissionNode<Boolean> COMMAND_PERMISSION = new PermissionNode<>(
            Constants.MOD_ID,
            "permission.command",
            PermissionTypes.BOOLEAN,
            (player, uuid, contexts) -> player == null || player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)
    );

    @Nullable
    private static MinecraftServer SERVER;

    public SpectatorChatNeoForge() {
        NeoForge.EVENT_BUS.addListener(SpectatorChatNeoForge::onServerStarting);
        NeoForge.EVENT_BUS.addListener(SpectatorChatNeoForge::onServerStopped);
        NeoForge.EVENT_BUS.addListener(SpectatorChatNeoForge::onRegisterCommands);
        NeoForge.EVENT_BUS.addListener(SpectatorChatNeoForge::onPermissionGatherNodes);

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
                if (ctx.getEntity() instanceof ServerPlayer player)
                    return PermissionAPI.getPermission(player, COMMAND_PERMISSION);

                return ctx.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER);
            }
        });
    }

    private static void onServerStarting(ServerStartingEvent event) {
        SERVER = event.getServer();
    }

    private static void onServerStopped(ServerStoppedEvent event) {
        SERVER = null;
    }

    private static void onRegisterCommands(RegisterCommandsEvent event) {
        SpectatorChatPlugin.onRegisterCommands(event.getDispatcher());
    }

    private static void onPermissionGatherNodes(PermissionGatherEvent.Nodes event) {
        event.addNodes(COMMAND_PERMISSION);
    }
}