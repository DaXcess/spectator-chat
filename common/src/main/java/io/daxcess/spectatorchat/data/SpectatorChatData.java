package io.daxcess.spectatorchat.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class SpectatorChatData extends SavedData {
    private GroupMode groupMode = GroupMode.GROUP_ONLY;
    private boolean enabled = true;

    public static SpectatorChatData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(new Factory<>(SpectatorChatData::create, SpectatorChatData::load, DataFixTypes.OPTIONS), "global-spectator-vc");
    }

    public static SpectatorChatData create() {
        return new SpectatorChatData();
    }

    public static SpectatorChatData load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        var mode = tag.getInt("groupMode");
        var enabled = tag.getBoolean("enabled");

        return new SpectatorChatData(mode, enabled);
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        tag.putInt("groupMode", groupMode.ordinal());
        tag.putBoolean("enabled", enabled);

        return tag;
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
