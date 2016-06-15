ItsATrap
========
Custom handling of skeleton traps.

Configuration
-------------

| Setting | Description |
| :--- | :--- |
| `debug.config` | If true, loaded configuration settings are logged. |
| `dry-run` | If true, the plugin logs what it would do about traps, rather than performing the actions. |
| `spawns` | If true, spawned mobs are logged. |
| `allow-spawn-eggs` | If true, skeleton and horse spawn eggs are treated as equivalent to JOCKEY and TRAP spawns, respectively and can then be used to test some aspects of the plugin's behavior. |

Testing ItsATrap is difficult because trap horse spawns are only common with a
lot of players online.  You basically need to test on a live server.


Commands
--------

 * `/itsatrap reload` - Reload the configuration.


Permissions
-----------

 * `itsatrap.admin` - Permission to run `/itsatrap reload`.

