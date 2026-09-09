package exersolver.mobspawnstandardization.chunk;

import net.minecraft.server.world.ChunkHolder;

public class PrioritizedChunkHolder implements Comparable<PrioritizedChunkHolder> {
    public final ChunkHolder holder;
    public final long priority;

    public PrioritizedChunkHolder(ChunkHolder holder, long priority) {
        this.holder = holder;
        this.priority = priority;
    }

    @Override
    public int compareTo(PrioritizedChunkHolder o) {
        return Long.compare(o.priority, this.priority);
    }
}