package io.daxcess.spectatorchat;

import de.maxhenkel.voicechat.api.VoicechatConnection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mod(Constants.MOD_ID)
public class SpectatorChatForge {
    public static final PermissionNode<Boolean> COMMAND_PERMISSION = new PermissionNode<>(
            Constants.MOD_ID,
            "permission.command",
            PermissionTypes.BOOLEAN,
            (player, uuid, contexts) -> player == null || player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)
    );

    @Nullable
    private static MinecraftServer SERVER;

    public SpectatorChatForge() {
        MinecraftForge.EVENT_BUS.register(this);

        SpectatorChatRegistry.register(new SpectatorChatService() {
            @Override
            public MinecraftServer getServer() {
                return SERVER;
            }

            @Override
            public List<ServerPlayer> getServerPlayers() {
                return SERVER == null ? List.of() : SERVER.getPlayerList().getPlayers();
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

    @SubscribeEvent
    private static void onServerStarting(ServerStartingEvent event) {
        SERVER = event.getServer();
    }

    @SubscribeEvent
    private static void onServerStopped(ServerStoppedEvent event) {
        SERVER = null;
    }

    @SubscribeEvent
    private static void onRegisterCommands(RegisterCommandsEvent event) {
        SpectatorChatPlugin.onRegisterCommands(event.getDispatcher());
    }

    @SubscribeEvent
    private static void onPermissionGatherNodes(PermissionGatherEvent.Nodes event) {
        event.addNodes(COMMAND_PERMISSION);
    }
}