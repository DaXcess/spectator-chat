package io.daxcess.spectatorchat;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import io.papermc.paper.command.brigadier.ApiMirrorRootNode;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpectatorChatPaper extends JavaPlugin implements Listener {

    public static final Logger LOGGER = LogManager.getLogger(Constants.MOD_ID);

    @Nullable
    private SpectatorChatPlugin voicechatPlugin;

    @Override
    public void onEnable() {
        var service = getServer().getServicesManager().load(BukkitVoicechatService.class);

        if (service == null)
        {
            LOGGER.error("Failed to register global spectator plugin");
            return;
        }

        voicechatPlugin = new SpectatorChatPlugin();
        service.registerPlugin(voicechatPlugin);

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            var registrar = event.registrar();
            var root = registrar.getDispatcher().getRoot();
            if (root instanceof ApiMirrorRootNode apiMirrorRootNode) {
                var dispatcher = apiMirrorRootNode.getDispatcher();

                SpectatorChatPlugin.onRegisterCommands(dispatcher);
            } else {
                SpectatorChatPaper.LOGGER.error("Failed to register commands");
            }
        });

        SpectatorChatRegistry.register(new SpectatorChatService() {
            @Override
            public MinecraftServer getServer() {
                return ((CraftServer)Bukkit.getServer()).getServer();
            }

            @Override
            public List<ServerPlayer> getServerPlayers() {
                return getServer().getPlayerList().getPlayers();
            }

            @Override
            public @Nullable ServerPlayer getConnectionPlayer(VoicechatConnection connection) {
                if (connection.getPlayer().getPlayer() instanceof CraftPlayer player)
                    return player.getHandle();

                return null;
            }

            @Override
            public boolean hasCommandAccess(CommandSourceStack ctx) {
                return ctx.getSender().hasPermission(Constants.PERMISSION_COMMAND);
            }
        });

        LOGGER.info("Successfully registered global spectator plugin");
    }

    @Override
    public void onDisable() {
        if (voicechatPlugin == null)
            return;

        getServer().getServicesManager().unregister(voicechatPlugin);
    }
}