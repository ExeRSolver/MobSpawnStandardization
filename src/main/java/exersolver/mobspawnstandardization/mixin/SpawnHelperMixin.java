package exersolver.mobspawnstandardization.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import exersolver.mobspawnstandardization.IMinecraftServer;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
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
        Random rng = ((IMinecraftServer) world.getServer()).mobspawn$getRNGManager().getRNG();
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
        Random rng = ((IMinecraftServer) world.getServer()).mobspawn$getRNGManager().getRNG();
        return rng.nextFloat();
    }

    @Redirect(
            method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Random;nextInt(I)I"
            )
    )
    private static int standardizeOffsetsAndSize(Random original, int i, @Local(argsOnly = true) ServerWorld world) {
        Random rng = ((IMinecraftServer) world.getServer()).mobspawn$getRNGManager().getRNG();
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
        return ((IMinecraftServer) world.getServer()).mobspawn$getRNGManager().getRNG();
    }
}
