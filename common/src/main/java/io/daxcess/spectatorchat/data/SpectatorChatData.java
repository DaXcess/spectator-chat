package io.daxcess.spectatorchat.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class SpectatorChatData extends SavedData {
    private GroupMode groupMode = GroupMode.GROUP_ONLY;
    private boolean enabled = true;

    public static final SavedDataType<SpectatorChatData> ID = new SavedDataType<>(
            "global-spectator-vc",
            SpectatorChatData::new,
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT.fieldOf("groupMode").forGetter(sd -> sd.groupMode.ordinal()),
                    Codec.BOOL.fieldOf("enabled").forGetter(sd -> sd.enabled)
            ).apply(instance, SpectatorChatData::new)),
            DataFixTypes.LEVEL
    );

    public static SpectatorChatData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(ID);
    }

    private SpectatorChatData() {}

    private SpectatorChatData(int groupMode, boolean enabled) {
        this.groupMode = GroupMode.values()[groupMode];
        this.enabled = enabled;
    }

    public GroupMode getGroupMode() {
        return groupMode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setGroupMode(GroupMode mode) {
        groupMode = mode;
        setDirty();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        setDirty();
    }
}
