package exersolver.mobspawnstandardization.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import exersolver.mobspawnstandardization.IMinecraftServer;
import exersolver.mobspawnstandardization.mixin.access.EntityAccessor;
import exersolver.mobspawnstandardization.rng.RNGManager;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.structure.SwampHutGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(SwampHutGenerator.class)
public abstract class SwampHutGeneratorMixin {

    @Inject(
            method = "generate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/WitchEntity;initialize(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/entity/EntityData;"
            )
    )
    private void standardizeMobRNG(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) ServerWorldAccess world, @Local WitchEntity witchEntity) {
        BlockPos pos = witchEntity.getBlockPos();
        long rngSeed = ((IMinecraftServer) world.getWorld().getServer()).mobspawn$getRNGManager().getRngSeed();
        Random random = new Random(RNGManager.mixSeed(rngSeed, pos.getX(), pos.getY(), pos.getZ()));
        ((EntityAccessor) witchEntity).setRandom(random);
    }

    @Inject(
            method = "method_16181",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/passive/CatEntity;initialize(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/entity/EntityData;"
            )
    )
    private void standardizeMobRNG(CallbackInfo ci, @Local(argsOnly = true) WorldAccess world, @Local CatEntity catEntity) {
        BlockPos pos = catEntity.getBlockPos();
        long rngSeed = ((IMinecraftServer) world.getWorld().getServer()).mobspawn$getRNGManager().getRngSeed();
        Random random = new Random(RNGManager.mixSeed(rngSeed, pos.getX(), pos.getY(), pos.getZ()));
        ((EntityAccessor) catEntity).setRandom(random);
    }
}
