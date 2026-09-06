package exersolver.mobspawnstandardization.mixin;

import exersolver.mobspawnstandardization.IMinecraftServer;
import exersolver.mobspawnstandardization.MobSpawnStandardization;
import exersolver.mobspawnstandardization.rng.RNGManager;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Mixin(ServerChunkManager.class)
public abstract class ServerChunkManagerMixin {
    @Shadow
    @Final
    private ServerWorld world;

    @Redirect(
            method = "tickChunks",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Collections;shuffle(Ljava/util/List;)V"
            )
    )
    private void standardizeChunkIteration(List<ChunkHolder> list) {
        if (!MobSpawnStandardization.isStandardMobSpawning(this.world)) {
            Collections.shuffle(list);
            return;
        }

        RNGManager rngManager = ((IMinecraftServer) this.world.getServer()).mobspawn$getRNGManager();
        int dimensionId = RNGManager.getDimensionId(this.world.getDimension());

        PrioritizedChunkHolder[] prioritizedList = new PrioritizedChunkHolder[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ChunkHolder holder = list.get(i);
            WorldChunk chunk = holder.getWorldChunk();
            long inhabitedTime = chunk != null ? chunk.getInhabitedTime() : 0L;
            ChunkPos pos = holder.getPos();
            long priority = RNGManager.mixSeed(rngManager.getRngSeed(), pos.x, pos.z, dimensionId, (int) inhabitedTime);
            prioritizedList[i] = new PrioritizedChunkHolder(holder, priority);
        }

        Arrays.sort(prioritizedList);

        for (int i = 0; i < prioritizedList.length; i++) {
            list.set(i, prioritizedList[i].holder);
        }
    }

    private static class PrioritizedChunkHolder implements Comparable<PrioritizedChunkHolder> {
        final ChunkHolder holder;
        final long priority;

        PrioritizedChunkHolder(ChunkHolder holder, long priority) {
            this.holder = holder;
            this.priority = priority;
        }

        @Override
        public int compareTo(PrioritizedChunkHolder o) {
            return Long.compare(o.priority, this.priority);
        }
    }
}
