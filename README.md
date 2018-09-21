ItsATrap
========
Custom handling of skeleton traps.


Features
--------

 * Only a configurable fraction of vanilla skeleton traps are allowed to spawn
   the initial triggering skeleton horse.
 * When that trap horse is triggered by the player approaching within 10 blocks
   of it, the trap springs and one of the following actions is taken:
   * In aquatic biomes (ocean and river variants), the trap mobs are replaced 
     by 3 witches riding guardians - a "Guardian Trap" - or 3 drowned riding
     dolphins - a "Dolphin Trap".
   * In other biomes, the fate of the trap mobs is selected randomly from the
     following options:
     * The trap mobs are replaced by three drowned riding zombie horses - a
       "Trident Trap".
     * The original skeleton trap (four skeletons riding skeletal horses) spawns
       unchanged.
     * The trap mobs are replaced by three witches riding cave spiders - a "Poison
       Trap".
     * The trap mobs are replaced by four zombies riding zombie horses - a "Zombie
       Trap". They wear pumpkin helmets and carry stone swords.
     * The trap mobs are replaced by four blazes riding ghasts - a "Ghast Trap".
     * The trap mobs are replaced by four baby zombies (with stone swords) riding
       chickens - a "Chicken Trap".
     * The trap mobs are replaced by four creepers, each with a small, configurable
       chance of being charged - a "Creeper Trap".
     * The trap mobs are replaced by three vindicators.
     * The trap mobs are replaced by one evoker.
     * The trap mobs are replaced by three illusioners.
     * The trap mobs are replaced by ten bugs - a mixture of silverfish and
       endermites.
     * The trap mobs are replaced by three shulkers.


Commands
--------

 * `/itsatrap help` - Show usage help.
 * `/itsatrap reload` - Reload the configuration.
 * `/itsatrap types` - List all possible trap replacement types.
 * `/itsatrap type [<type>]` - Force all traps to be of the specified
   type until the next restart (for testing purposes). If the
   type is omitted, normal random type selection resumes.


Configuration
-------------

| Setting | Description |
| :--- | :--- |
| `debug.config` | If true, loaded configuration settings are logged. |
| `debug.dry-run` | If true, the plugin logs what it would do about traps, rather than performing the actions. |
| `debug.spawns` | If true, log spawns of the trigger horse and trap replacements. |
| `debug.vanilla-spawns` | If true, log spawns of the trigger horse jockey and the reinforcement skeleton horses. This setting gives more insight into the vanilla trap behaviour than the debug.spawns setting. |
| `debug.allow-spawn-eggs` | If true, skeleton spawn eggs spawn a randomly selected trap replacement, or the specific replacement specified by the `/itsatrap type <type>` command. |
| `trap-chance` | The probability, in the range [0.0,1.0], that a vanilla skeleton trap will be allowed to spawn in the world. A value of 0.0 denies all trap spawns, whereas 1.0 allows all traps to spawn, and they are then eligible for replacement with custom mobs. |
| `creeper-charge-chance` | The probability, in the range [0.0,1.0] that a creeper in a Creeper Trap will be charged. |
| `trident-drop-chance` | The probability, in the range [0.0,1.0] that a drowned will drop its trident. |

Permissions
-----------

 * `itsatrap.admin` - Permission to run `/itsatrap` and its subcommands.

