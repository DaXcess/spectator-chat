package io.daxcess.spectatorchat;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpectatorChatMod implements ModInitializer {

	public static final String MOD_ID = "simple-voice-chat-global-spectator";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Nullable
	public static MinecraftServer SERVER;

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> SERVER = server);
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> SERVER = null);
	}
}