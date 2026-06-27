package exersolver.mobspawnstandardization.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import exersolver.mobspawnstandardization.IMinecraftServer;
import exersolver.mobspawnstandardization.mixin.access.EntityAccessor;
import exersolver.mobspawnstandardization.rng.RNGManager;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin {

    @Inject(
            method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
            at = @At("HEAD")
    )
    private static void setRNG(SpawnGroup group, ServerWorld world, WorldChunk chunk, SpawnHelper.Checker checker, SpawnHelper.Runner runner, CallbackInfo ci) {
        ((IMinecraftServer) world.getServer()).mobspawn$getRNGManager().setRNGForSpawnCycle(chunk.getPos(), world.getDimension(), group);
    }

    @Redirect(
            method = "getSpawnPos",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Random;nextInt(I)I"
            )
    )
    private static int standardizeSpawnPos(Random original, int i, @Local(argsOnly = true) World world) {
        Random rng = ((IMinecraftServer) world.getServer()).mobspawn$getRNGManager().baseRandom;
        return rng.nextInt(i);
    }

    @Redirect(
            method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Random;nextFloat()F",
                    ordinal = 0
            )
    )
    private static float standardizeAttemptCount(Random original, @Local(argsOnly = true) ServerWorld world) {
        RNGManager rngManager = ((IMinecraftServer) world.getServer()).mobspawn$getRNGManager();
        Random rng = rngManager.baseRandom;
        rngManager.offsetRandom = new Random(rng.nextLong());
        rngManager.mobRandom = new Random(rng.nextLong());
        return rng.nextFloat();
    }

    @Redirect(
            method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Random;nextInt(I)I"
            ),
            slice = @Slice(
                    from = @At(
                            value = "HEAD"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Ljava/util/Random;nextInt(I)I",
                            ordinal = 3
                    )
            )
    )
    private static int standardizeOffset(Random original, int i, @Local(argsOnly = true) ServerWorld world) {
        Random rng = ((IMinecraftServer) world.getServer()).mobspawn$getRNGManager().offsetRandom;
        return rng.nextInt(i);
    }

    @ModifyArg(
            method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/SpawnHelper;pickRandomSpawnEntry(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/entity/SpawnGroup;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome$SpawnEntry;"
            ),
            index = 4
    )
    private static Random standardizeSpawnEntry(Random random, @Local(argsOnly = true) ServerWorld world) {
        return ((IMinecraftServer) world.getServer()).mobspawn$getRNGManager().mobRandom;
    }

    @Redirect(
            method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Random;nextInt(I)I",
                    ordinal = 4
            )
    )
    private static int standardizePackSize(Random original, int i, @Local(argsOnly = true) ServerWorld world) {
        Random rng = ((IMinecraftServer) world.getServer()).mobspawn$getRNGManager().mobRandom;
        return rng.nextInt(i);
    }

    @ModifyArg(
            method = "canSpawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/biome/Biome$SpawnEntry;Lnet/minecraft/util/math/BlockPos$Mutable;D)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/SpawnRestriction;canSpawn(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)Z"
            ),
            index = 4
    )
    private static Random standardizeSpawnChecks(Random random, @Local(argsOnly = true) ServerWorld world) {
        return ((IMinecraftServer) world.getServer()).mobspawn$getRNGManager().mobRandom;
    }

    @Inject(
            method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;initialize(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/entity/EntityData;"
            )
    )
    private static void standardizeMobRNG(CallbackInfo ci, @Local(argsOnly = true) ServerWorld world, @Local MobEntity mobEntity) {
        BlockPos pos = mobEntity.getBlockPos();
        long rngSeed = ((IMinecraftServer) world.getServer()).mobspawn$getRNGManager().getRngSeed();
        Random random = new Random(RNGManager.mixSeed(rngSeed, pos.getX(), pos.getY(), pos.getZ()));
        ((EntityAccessor) mobEntity).setRandom(random);
    }

    @Inject(
            method = "populateEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;initialize(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/entity/EntityData;"
            )
    )
    private static void standardizeMobRNG(CallbackInfo ci, @Local(argsOnly = true) WorldAccess world, @Local MobEntity mobEntity) {
        BlockPos pos = mobEntity.getBlockPos();
        long rngSeed = ((IMinecraftServer) world.getWorld().getServer()).mobspawn$getRNGManager().getRngSeed();
        Random random = new Random(RNGManager.mixSeed(rngSeed, pos.getX(), pos.getY(), pos.getZ()));
        ((EntityAccessor) mobEntity).setRandom(random);
    }
}
