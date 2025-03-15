package io.daxcess.spectatorchat.mixin;

import io.daxcess.spectatorchat.SpectatorChatPlugin;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

	@Inject(at = @At("RETURN"), method = "changeGameMode")
	private void onGameModeChange(GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue())
			return;

		var player = (ServerPlayerEntity) (Object) this;

		if (SpectatorChatPlugin.SERVER_API == null)
			return;

		SpectatorChatPlugin.changePlayerGlobalGroup(player.getUuid(), gameMode == GameMode.SPECTATOR);
	}
}