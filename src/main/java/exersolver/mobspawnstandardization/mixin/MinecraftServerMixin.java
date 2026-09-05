package exersolver.mobspawnstandardization.mixin;

import exersolver.mobspawnstandardization.IMinecraftServer;
import exersolver.mobspawnstandardization.MobSpawnStandardization;
import exersolver.mobspawnstandardization.rng.RNGManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements IMinecraftServer {
    @Unique
    private RNGManager rngManager;

    @Inject(
            method = "loadWorld()V",
            at = @At("HEAD")
    )
    private void onLoadWorld(CallbackInfo ci) {
        this.rngManager = new RNGManager(MobSpawnStandardization.rngSeedGetter.apply((MinecraftServer) (Object) this));
    }

    @Override
    public RNGManager mobspawn$getRNGManager() {
        return this.rngManager;
    }
}
