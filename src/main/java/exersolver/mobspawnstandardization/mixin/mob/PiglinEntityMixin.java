package exersolver.mobspawnstandardization.mixin.mob;

import exersolver.mobspawnstandardization.mixin.access.EntityAccessor;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(PiglinEntity.class)
public abstract class PiglinEntityMixin {

    @Redirect(
            method = "initialize",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/WorldAccess;getRandom()Ljava/util/Random;"
            )
    )
    private Random standardizeBaby(WorldAccess world) {
        return ((EntityAccessor) this).getRandom();
    }

    @Redirect(
            method = "equipAtChance",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/World;random:Ljava/util/Random;",
                    opcode = org.objectweb.asm.Opcodes.GETFIELD
            )
    )
    private Random standardizeEquipment(World world) {
        return ((EntityAccessor) this).getRandom();
    }
}
