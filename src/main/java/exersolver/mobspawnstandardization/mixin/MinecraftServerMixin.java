package exersolver.mobspawnstandardization.mixin;

import exersolver.mobspawnstandardization.IMinecraftServer;
import exersolver.mobspawnstandardization.rng.RNGManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.SaveProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements IMinecraftServer {
    @Shadow
    @Final
    protected SaveProperties saveProperties;

    @Unique
    private RNGManager rngManager;

    @Inject(
            method = "loadWorld()V",
            at = @At("HEAD")
    )
    private void onLoadWorld(CallbackInfo ci) {
        this.rngManager = new RNGManager(this.saveProperties.getGeneratorOptions().getSeed());
    }

    @Override
    public RNGManager mobspawn$getRNGManager() {
        return this.rngManager;
    }
}
