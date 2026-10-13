# Manbear – Forge 1.20.1 horror mod

Requires: Minecraft 1.20.1, Forge 47.x, **GeckoLib 4.4.9+** (4.x).

## Build
1. Install JDK 17.
2. Get a Gradle wrapper: copy `gradlew`, `gradlew.bat` and the `gradle/` folder from the official
   Forge 1.20.1 MDK into this folder (or run `gradle wrapper --gradle-version 8.1.1` if you have Gradle).
3. `./gradlew build` -> jar in `build/libs/`.  `./gradlew runClient` to test.
4. Drop the jar + GeckoLib into `mods/`.

## Your assets
`geo/manbear.geo.json`, `textures/entity/manbear.png` and `manbear_eyes.png` are byte-for-byte your files.
`animations/manbear.animation.json` is your file plus two derived clips: `crouch_walk` (walk cycle in the
hunt posture) and `crawl` (slowed `run2` gait), and `walk` is set to loop. Nothing in the textures was touched.
Model scale is `ManbearRenderer.MODEL_SCALE` (0.7 -> ~3 blocks tall, matching the 5-block hitbox).

## Testing quickly (OP)
* Spawn egg in the creative tab, or `/manbear spawn`
* `/manbear mode stalk|hide|peek|fake_chase|chase|lay_trap|ambush|retreat`
* `/manbear spawn chaser` forces a spawn that WILL chase (normal spawns only do 1 in 10)
* `/manbear info`, `/manbear profile` (what he has learned about you), `/manbear remove`, `/manbear reset`
* Config: `config/manbear-common.toml` (spawn odds, speeds, damage, griefing, ...)

## How the requested features are done
| Request | Where |
|---|---|
| Spawn 1/25 per minute, only one per world | `ServerEvents.spawnRoll`, `ManbearData.claim` (a second one discards itself) |
| Stalking, circling while facing you | `ManbearBrain.tickStalk` + `ManbearMoveControl` (strafe input relative to a body that always faces you) |
| Peek behind trees, hide behind bushes | `CoverFinder` (logs/leaves/terrain that break line of sight, plus a side spot to lean out), `tickHide`, `tickPeek` |
| Random mode switching (stalk -> peek -> fake -> real chase) | `ManbearBrain.spontaneous` / `pickNextMode` – weighted, checked every second |
| Fake chase | `tickFake` (charges, stops short, stares; ~10% turn out real) |
| Real chase, lunge, speed = player then slightly faster | `tickChase`, `tickLunge`, `refSpeedMod` (matches your actual sprint speed, up to +12% over 45 s) |
| Break blocks to reach you | `BlockBreaker` (respects mobGriefing + config) |
| Climb / crawl | `WallClimberNavigation` + climb flag; stance system (stand 5.0 / crouch 3.3 / crawl 0.9) picks a smaller body when a route needs it |
| Crouch to be stealthy | stance + `hunt` / `crouch_walk` animations |
| Bear traps, ambush when you step in one | `BearTrapBlock` (hunter_set) -> `ManbearEntity.onTrapSprung` -> `AMBUSH` mode |
| Directional bear noises | positional sound events (`ModSounds`) played from his body |
| Glowing eyes | `ManbearEyesLayer` (your eyes texture, full-bright). The texture only has 2 pixels, so the glow is small. |
| Adapts to the player | `PlayerProfile` (vigilance, panic, aggression, camping, sneak, sprint, base location) saved in the world |

### What adaptation does
Looks at him a lot -> hides/peeks/fakes more and freezes when stared at. Ignores him -> comes closer, chases sooner.
Runs when chased -> more real chases and lunges. Fights back -> more traps, hit-and-run. Camps/holes up -> more chases
with block breaking, traps near your base.

### Sounds
They alias vanilla polar-bear/ravager sounds pitched down (`assets/manbear/sounds.json`). To use your own, put `.ogg`
files in `assets/manbear/sounds/` and point the `sounds.json` entries at them.

## If it does not compile first time
This was written without access to the Forge/GeckoLib jars. The spots most likely to need a tweak if your GeckoLib
version differs slightly: `withScale` and `reRender` (renderer/eyes layer), `AnimationState.isMoving`. The brain, entity and
block code only use vanilla 1.20.1 (official mappings) APIs.

## Update 2: stealth, short appearances, peeking, animals, ambience
* **Stalking is farther away** (default 26-42 blocks, `stalkMinDistance` / `stalkMaxDistance`); hiding/peeking cover is searched at that range too. He only closes in when ambushing, chasing, fake chasing (or if you walk up to him).
* **He vanishes after 90-120 s** (`lifetimeMinSeconds` / `lifetimeMaxSeconds`). If you are not looking at him he disappears immediately; if you are, he backs away and fades. If a chase is running he finishes it first (max +40 s). Bear traps he laid stay in the world (capped by `maxTraps`).
  Because his life is short, the real-chase grace period is now 20 s and he switches modes more often.
* **Peeking** now stays behind the tree and *leans out* around the trunk with the new `peek_left` / `peek_right` animations (built from your `hunt` pose; legs planted, hips/torso/head swing out, one arm grips the tree).
  If he leans into the tree instead of out of it, set `SWAP_PEEK_SIDES = true` in `ManbearEntity.java`.
* **Animals** panic and run when he is near, and scatter in random directions for the first ~12 s after he appears (`animalsFlee`, `animalFleeRange`).
* **Distorted pig / cow / sheep noises** (`mimicAnimalSounds`) come from him, or sometimes from behind you.

## Update 3: scarier chases
* **Only 1 in 10 spawns are chasers** (`chaseChanceDenominator`). The other 9 never start a real chase on their own: they stalk, hide, peek, fake-charge and lay traps (fake charges become more common). A chaser can still be provoked into attacking if you hit him, and a bear trap you step in still triggers an ambush.
* **Faster chases:** he starts 15% faster than your sprint (`chaseSpeedBonusBase`) and builds to 35% faster after 30 s (`chaseSpeedBonusMax`, `chaseRampSeconds`). Fake charges run 15% faster than you so they look real.
* Delete `config/manbear-common.toml` (or edit the values) so the new defaults apply.
