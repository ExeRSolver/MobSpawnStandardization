package exersolver.mobspawnstandardization.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import exersolver.mobspawnstandardization.IMinecraftServer;
import exersolver.mobspawnstandardization.mixin.access.EntityAccessor;
import exersolver.mobspawnstandardization.rng.RNGManager;
import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.structure.OceanMonumentGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(OceanMonumentGenerator.Piece.class)
public abstract class OceanMonumentPieceMixin {

    @Inject(
            method = "method_14772",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ElderGuardianEntity;initialize(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/entity/EntityData;"
            )
    )
    private void standardizeMobRNG(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) WorldAccess world, @Local ElderGuardianEntity elderGuardianEntity) {
        BlockPos pos = elderGuardianEntity.getBlockPos();
        long rngSeed = ((IMinecraftServer) world.getWorld().getServer()).mobspawn$getRNGManager().getRngSeed();
        Random random = new Random(RNGManager.mixSeed(rngSeed, pos.getX(), pos.getY(), pos.getZ()));
        ((EntityAccessor) elderGuardianEntity).setRandom(random);
    }
}
