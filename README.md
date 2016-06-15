ItsATrap
========
Custom handling of skeleton traps.


Features
--------

 * Only a configurable fraction of vanilla skeleton traps are allowed to spawn
   the initial triggering skeleton horse.
 * When that trap horse is triggered by the player approaching within 10 blocks
   of it, the trap springs and one of the following actions is taken:
   * In ocean and deep ocean biomes, the trap mobs are replaced by 3 witches
     riding guardians - a "Guardian Trap".
   * In other biomes, the fate of the trap mobs is selected randomly from the
     following options:
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


Configuration
-------------

| Setting | Description |
| :--- | :--- |
| `debug.config` | If true, loaded configuration settings are logged. |
| `debug.dry-run` | If true, the plugin logs what it would do about traps, rather than performing the actions. |
| `debug.spawns` | If true, spawned mobs are logged. |
| `debug.allow-spawn-eggs` | If true, skeleton and horse spawn eggs are treated as equivalent to JOCKEY and TRAP spawns, respectively and can then be used to test some aspects of the plugin's behavior. |
| `trap-chance` | The probability, in the range [0.0,1.0], that a vanilla skeleton trap will be allowed to spawn in the world. A value of 0.0 denies all trap spawns, whereas 1.0 allows all traps to spawn, and they are then eligible for replacement with custom mobs. |
| `creeper-charge-chance` | The probability, in the range [0.0,1.0] that a creeper in a Creeper Trap will be charged. |

Testing ItsATrap is difficult because trap horse spawns are only common with a
lot of players online.  You basically need to test on a live server.


Commands
--------

 * `/itsatrap reload` - Reload the configuration.


Permissions
-----------

 * `itsatrap.admin` - Permission to run `/itsatrap reload`.

