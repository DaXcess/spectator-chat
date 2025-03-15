package io.daxcess.spectatorchat;

import de.maxhenkel.configbuilder.ConfigBuilder;
import io.daxcess.spectatorchat.config.Configuration;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class SpectatorChatMod implements ModInitializer {

	public static final String MODID = "simple-voice-chat-global-spectator";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static Configuration CONFIG;

	@Nullable
	public static MinecraftServer SERVER;

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> SERVER = server);
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> SERVER = null);

		CONFIG = ConfigBuilder.builder(Configuration::new).path(getConfigFile()).build();
	}

	private static Path getConfigFile() {
		return getConfigFolder().resolve("spectator-chat.properties");
	}

	private static Path getConfigFolder() {
		return Path.of(".").resolve("config");
	}
}