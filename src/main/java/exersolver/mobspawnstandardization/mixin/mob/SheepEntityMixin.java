package exersolver.mobspawnstandardization.mixin.mob;

import exersolver.mobspawnstandardization.mixin.access.EntityAccessor;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin {

    @Redirect(
            method = "initialize",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/WorldAccess;getRandom()Ljava/util/Random;"
            )
    )
    private Random standardizeColor(WorldAccess world) {
        return ((EntityAccessor) this).getRandom();
    }
}
