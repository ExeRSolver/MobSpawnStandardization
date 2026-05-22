package exersolver.mobspawnstandardization.rng;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.Random;

public class RNGManager {
    private final HashMap<SpawningRNGSection, Random> rngMap = new HashMap<>();
    private final long rngSeed;
    private Random currentRNG;

    public RNGManager(long rngSeed) {
        this.rngSeed = rngSeed;
    }

    public void setRNGForSpawnCycle(ChunkPos cPos, DimensionType dimType, SpawnGroup category) {
        int dimensionId = dimType.hasEnderDragonFight() ? 2 : dimType.hasCeiling() ? 1 : 0;
        SpawningRNGSection section = new SpawningRNGSection(cPos.x, cPos.z, dimensionId, category.ordinal());
        Random rng = this.rngMap.get(section);
        if (rng == null) {
            rng = new Random(mixSeed(this.rngSeed, section.chunkX, section.chunkZ, section.dimensionId, section.categoryId));
            this.rngMap.put(section, rng);
        }
        this.currentRNG = new Random(rng.nextLong());
    }

    public Random getRNG() {
        return this.currentRNG;
    }

    private static long mixSeed(long rngSeed, int... salts) {
        for (int salt : salts) {
            rngSeed ^= salt;

            rngSeed = (rngSeed ^ (rngSeed >>> 30)) * 0xbf58476d1ce4e5b9L;
            rngSeed = (rngSeed ^ (rngSeed >>> 27)) * 0x94d049bb133111ebL;
            rngSeed ^= (rngSeed >>> 31);
        }
        return rngSeed;
    }
}
