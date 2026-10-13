package com.manbear;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ManbearConfig {
    public static final ForgeConfigSpec SPEC;

    // spawning
    public static final ForgeConfigSpec.IntValue SPAWN_INTERVAL_TICKS;
    public static final ForgeConfigSpec.IntValue SPAWN_CHANCE_DENOMINATOR;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> DIMENSIONS;
    public static final ForgeConfigSpec.IntValue SPAWN_MIN_DIST;
    public static final ForgeConfigSpec.IntValue SPAWN_MAX_DIST;
    public static final ForgeConfigSpec.IntValue LIFETIME_MIN_SECONDS;
    public static final ForgeConfigSpec.IntValue LIFETIME_MAX_SECONDS;
    public static final ForgeConfigSpec.IntValue RESPAWN_COOLDOWN_MINUTES;
    // behaviour
    public static final ForgeConfigSpec.IntValue GRACE_SECONDS;
    public static final ForgeConfigSpec.IntValue CHASE_CHANCE_DENOMINATOR;
    public static final ForgeConfigSpec.DoubleValue CHASE_SPEED_BONUS_BASE;
    public static final ForgeConfigSpec.DoubleValue CHASE_SPEED_BONUS_MAX;
    public static final ForgeConfigSpec.IntValue CHASE_RAMP_SECONDS;
    public static final ForgeConfigSpec.DoubleValue DAMAGE;
    public static final ForgeConfigSpec.IntValue LUNGE_COOLDOWN_TICKS;
    public static final ForgeConfigSpec.BooleanValue ADAPTATION;
    public static final ForgeConfigSpec.IntValue STALK_MIN_DISTANCE;
    public static final ForgeConfigSpec.IntValue STALK_MAX_DISTANCE;
    // animals / ambience
    public static final ForgeConfigSpec.BooleanValue ANIMALS_FLEE;
    public static final ForgeConfigSpec.IntValue ANIMAL_FLEE_RANGE;
    public static final ForgeConfigSpec.BooleanValue MIMIC_ANIMAL_SOUNDS;
    // blocks / traps
    public static final ForgeConfigSpec.BooleanValue BREAK_BLOCKS;
    public static final ForgeConfigSpec.DoubleValue MAX_BREAK_HARDNESS;
    public static final ForgeConfigSpec.BooleanValue DROP_BROKEN_BLOCKS;
    public static final ForgeConfigSpec.IntValue MAX_TRAPS;
    public static final ForgeConfigSpec.DoubleValue TRAP_DAMAGE;
    public static final ForgeConfigSpec.IntValue TRAP_ROOT_TICKS;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.push("spawning");
        SPAWN_INTERVAL_TICKS = b.comment("Ticks between spawn rolls (1200 = 1 minute).").defineInRange("spawnIntervalTicks", 1200, 20, 1_000_000);
        SPAWN_CHANCE_DENOMINATOR = b.comment("Each roll succeeds with chance 1/N. Default 25 = 1/25 per minute.").defineInRange("spawnChanceDenominator", 25, 1, 100_000);
        DIMENSIONS = b.comment("Dimensions the Manbear may spawn in.").defineList("dimensions", List.of("minecraft:overworld"), o -> o instanceof String);
        SPAWN_MIN_DIST = b.defineInRange("spawnMinDistance", 30, 16, 128);
        SPAWN_MAX_DIST = b.defineInRange("spawnMaxDistance", 46, 20, 160);
        LIFETIME_MIN_SECONDS = b.comment("He vanishes after a random time between min and max (seconds), so sightings stay rare and scary.").defineInRange("lifetimeMinSeconds", 90, 10, 3600);
        LIFETIME_MAX_SECONDS = b.defineInRange("lifetimeMaxSeconds", 120, 10, 3600);
        RESPAWN_COOLDOWN_MINUTES = b.comment("Cool-down after he is killed or leaves before he can spawn again.").defineInRange("respawnCooldownMinutes", 3, 0, 6000);
        b.pop();

        b.push("behaviour");
        GRACE_SECONDS = b.comment("Seconds after spawning before he may start a REAL chase on his own.").defineInRange("graceSeconds", 20, 0, 3600);
        CHASE_CHANCE_DENOMINATOR = b.comment("Each time he spawns there is a 1/N chance he is a real hunter that will actually chase. Otherwise he only stalks, hides, peeks, fakes and lays traps.").defineInRange("chaseChanceDenominator", 10, 1, 10_000);
        CHASE_SPEED_BONUS_BASE = b.comment("How much faster than your sprint speed he is the moment a chase starts. 0.15 = 15% faster.").defineInRange("chaseSpeedBonusBase", 0.15, 0.0, 2.0);
        CHASE_SPEED_BONUS_MAX = b.comment("The extra speed he reaches after chaseRampSeconds of chasing.").defineInRange("chaseSpeedBonusMax", 0.35, 0.0, 2.0);
        CHASE_RAMP_SECONDS = b.defineInRange("chaseRampSeconds", 30, 5, 600);
        DAMAGE = b.comment("Base attack damage (hearts x2).").defineInRange("attackDamage", 6.0, 0.5, 100.0);
        LUNGE_COOLDOWN_TICKS = b.defineInRange("lungeCooldownTicks", 70, 20, 1200);
        ADAPTATION = b.comment("Learn from each player's habits and adapt.").define("adaptation", true);
        STALK_MIN_DISTANCE = b.comment("While stalking/hiding he keeps at least this far away. He only closes in when ambushing, chasing or fake chasing.").defineInRange("stalkMinDistance", 26, 10, 80);
        STALK_MAX_DISTANCE = b.defineInRange("stalkMaxDistance", 42, 12, 100);
        b.pop();

        b.push("animals_and_ambience");
        ANIMALS_FLEE = b.comment("Animals panic and run when he is near (and scatter when he first appears).").define("animalsFlee", true);
        ANIMAL_FLEE_RANGE = b.defineInRange("animalFleeRange", 40, 8, 128);
        MIMIC_ANIMAL_SOUNDS = b.comment("Occasional distorted pig / cow / sheep noises coming from him or from behind you.").define("mimicAnimalSounds", true);
        b.pop();

        b.push("blocks_and_traps");
        BREAK_BLOCKS = b.comment("Allow him to break blocks to reach you (also needs the mobGriefing gamerule).").define("breakBlocks", true);
        MAX_BREAK_HARDNESS = b.comment("Blocks harder than this are safe (obsidian = 50).").defineInRange("maxBreakHardness", 30.0, 0.0, 2000.0);
        DROP_BROKEN_BLOCKS = b.define("dropBrokenBlocks", true);
        MAX_TRAPS = b.defineInRange("maxTraps", 8, 0, 64);
        TRAP_DAMAGE = b.defineInRange("trapDamage", 4.0, 0.0, 100.0);
        TRAP_ROOT_TICKS = b.comment("How long a sprung trap holds you in place.").defineInRange("trapRootTicks", 100, 10, 2400);
        b.pop();

        SPEC = b.build();
    }
}
