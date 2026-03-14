package xyz.jpenilla.squaremap.common.util;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.server.level.ChunkHolder;

public interface ChunkMapAccess {
    ChunkHolder squaremap$getVisibleChunkIfPresent(long pos);

    Long2ObjectLinkedOpenHashMap<ChunkHolder> squaremap$pendingUnloads();
}
