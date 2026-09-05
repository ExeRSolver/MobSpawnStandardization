package exersolver.mobspawnstandardization.mixin.mob;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import exersolver.mobspawnstandardization.MobSpawnStandardization;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

    @Unique
    private Random despawnRandom;

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initialize", at = @At("HEAD"))
    public void initDespawnTimer(WorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag, CallbackInfoReturnable<EntityData> cir) {
        if (MobSpawnStandardization.isStandardMobSpawning(world.getWorld())) {
            this.despawnRandom = new Random(this.getRandom().nextLong());
        }
    }

    @WrapOperation(method = "checkDespawn", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"))
    public int redirectDespawnRNG(Random instance, int bound, Operation<Integer> original) {
        if (this.despawnRandom == null || !MobSpawnStandardization.isStandardMobSpawning(this.world)) {
            return original.call(instance, bound);
        }
        return original.call(this.despawnRandom, bound);
    }
}
