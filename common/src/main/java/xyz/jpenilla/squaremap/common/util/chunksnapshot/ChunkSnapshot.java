package xyz.jpenilla.squaremap.common.util.chunksnapshot;

import java.util.EnumMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public interface ChunkSnapshot extends BiomeManager.NoiseBiomeSource {
    BlockState getBlockState(BlockPos pos);

    FluidState getFluidState(BlockPos pos);

    int getHeight(Heightmap.Types type, int x, int z);

    DimensionType dimensionType();

    ChunkPos pos();

    boolean sectionEmpty(int sectionIndex);

    @SuppressWarnings({"unchecked", "rawtypes"})
    static ChunkSnapshot snapshot(final LevelChunk chunk, final boolean biomesOnly) {
        // AsyncCatcher.catchOp("Chunk Snapshot");
        final LevelChunkSection[] sections = chunk.getSections();
        final int sectionCount = sections.length;
        final PalettedContainer<BlockState>[] states = new PalettedContainer[sectionCount];
        final Biome[] biomes = new Biome[1024];

        var biomeContainer = chunk.getBiomes();

        for (int y = 0; y < 64; y++) {
            for (int z = 0; z < 4; z++) {
                for (int x = 0; x < 4; x++) {
                    int index = (y << 4) | (z << 2) | x;
                    biomes[index] = biomeContainer.getNoiseBiome(x, y, z);
                }
            }
        }

        final boolean[] empty = new boolean[sectionCount];
        final Heightmap heightmap = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE);
        if (!biomesOnly) {
            for (int i = 0; i < sectionCount; i++) {

                final LevelChunkSection section = sections[i];

                if (section == null) {
                    empty[i] = true;
                    states[i] = ChunkSnapshotImpl.EMPTY_SECTION_BLOCK_STATES;
                    continue;
                }

                final boolean sectionEmpty = section.isEmpty();
                empty[i] = sectionEmpty;

                if (sectionEmpty) {
                    states[i] = ChunkSnapshotImpl.EMPTY_SECTION_BLOCK_STATES;
                } else {
                    states[i] = section.getStates();
                }
            }
        }

        return new ChunkSnapshotImpl(
            states,
            biomes,
            Util.make(new EnumMap<>(Heightmap.Types.class), map -> map.put(Heightmap.Types.WORLD_SURFACE, heightmap)),
            empty,
            chunk.getLevel().dimensionType(),
            chunk.getPos()
        );
    }
}
