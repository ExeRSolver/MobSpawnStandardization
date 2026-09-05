package exersolver.mobspawnstandardization;

import exersolver.mobspawnstandardization.mixin.access.BooleanRuleInvoker;
import exersolver.mobspawnstandardization.mixin.access.GameRulesInvoker;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

public class MobSpawnStandardization implements ModInitializer {
	public static final String MOD_ID = "mobspawnstandardization";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static GameRules.Key<GameRules.BooleanRule> STANDARD_MOB_SPAWNING;
	public static Function<MinecraftServer, Long> rngSeedGetter = server -> server.getSaveProperties().getGeneratorOptions().getSeed();

	@Override
	public void onInitialize() {
		STANDARD_MOB_SPAWNING = GameRulesInvoker.invokeRegister("standardMobSpawning", GameRules.Category.SPAWNING, BooleanRuleInvoker.invokeCreate(true));
	}

	public static boolean isStandardMobSpawning(World world) {
		return world != null && world.getGameRules() != null && world.getGameRules().getBoolean(STANDARD_MOB_SPAWNING);
	}
}