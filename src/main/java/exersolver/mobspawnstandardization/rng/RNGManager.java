package exersolver.mobspawnstandardization.rng;

import exersolver.mobspawnstandardization.IMinecraftServer;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.Random;

public class RNGManager {
    private final HashMap<SpawningRNGSection, Random> rngMap = new HashMap<>();
    private final long rngSeed;
    public Random baseRandom;
    public Random offsetRandom;
    public Random mobRandom;

    public RNGManager(long rngSeed) {
        this.rngSeed = rngSeed;
    }

    public long getRngSeed() {
        return this.rngSeed;
    }

    public void setRNGForSpawnCycle(ChunkPos cPos, DimensionType dimType, SpawnGroup category) {
        int dimensionId = getDimensionId(dimType);
        SpawningRNGSection section = new SpawningRNGSection(cPos.x, cPos.z, dimensionId, category.ordinal());
        Random rng = this.rngMap.get(section);
        if (rng == null) {
            rng = new Random(mixSeed(this.rngSeed, MixingType.SPAWN_CYCLE_RNG.ordinal(), cPos.x, cPos.z, dimensionId, section.categoryId));
            this.rngMap.put(section, rng);
        }
        this.baseRandom = new Random(rng.nextLong());
    }

    public static Random getMobRng(WorldAccess world, int x, int y, int z) {
        long rngSeed = ((IMinecraftServer) world.getWorld().getServer()).mobspawn$getRNGManager().getRngSeed();
        return new Random(mixSeed(rngSeed, MixingType.MOB_RNG.ordinal(), x, y, z));
    }

    public static long mixSeed(long rngSeed, int... salts) {
        for (int salt : salts) {
            rngSeed ^= salt;

            rngSeed = (rngSeed ^ (rngSeed >>> 30)) * 0xbf58476d1ce4e5b9L;
            rngSeed = (rngSeed ^ (rngSeed >>> 27)) * 0x94d049bb133111ebL;
            rngSeed ^= (rngSeed >>> 31);
        }
        return rngSeed;
    }

    public static int getDimensionId(DimensionType dimType) {
        return dimType.hasEnderDragonFight() ? 2 : dimType.hasCeiling() ? 1 : 0;
    }

    public enum MixingType {
        SPAWN_CYCLE_RNG, MOB_RNG, CHUNK_PRIORITY
    }
}
