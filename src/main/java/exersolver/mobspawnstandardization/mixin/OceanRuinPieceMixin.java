package exersolver.mobspawnstandardization.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import exersolver.mobspawnstandardization.IMinecraftServer;
import exersolver.mobspawnstandardization.mixin.access.EntityAccessor;
import exersolver.mobspawnstandardization.rng.RNGManager;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.structure.OceanRuinGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(OceanRuinGenerator.Piece.class)
public abstract class OceanRuinPieceMixin {

    @Inject(
            method = "handleMetadata",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/DrownedEntity;initialize(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/entity/EntityData;"
            )
    )
    private void standardizeMobRNG(CallbackInfo ci, @Local(argsOnly = true) WorldAccess world, @Local DrownedEntity drownedEntity) {
        BlockPos pos = drownedEntity.getBlockPos();
        long rngSeed = ((IMinecraftServer) world.getWorld().getServer()).mobspawn$getRNGManager().getRngSeed();
        Random random = new Random(RNGManager.mixSeed(rngSeed, pos.getX(), pos.getY(), pos.getZ()));
        ((EntityAccessor) drownedEntity).setRandom(random);
    }
}
