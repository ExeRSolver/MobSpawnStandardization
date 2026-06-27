package exersolver.mobspawnstandardization.mixin.mob;

import exersolver.mobspawnstandardization.mixin.access.EntityAccessor;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(SpiderEntity.class)
public abstract class SpiderEntityMixin {

    @Redirect(
            method = "initialize",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/WorldAccess;getRandom()Ljava/util/Random;"
            )
    )
    private Random standardizeJockeyAndEffect(WorldAccess world) {
        return ((EntityAccessor) this).getRandom();
    }
}
