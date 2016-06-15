package nu.nerd.itsatrap;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;

// ----------------------------------------------------------------------------
/**
 * Customised trap horse handling.
 *
 * Possible traps:
 * <ul>
 * <li>In ocean and deep ocean biomes, spawn witches riding guardians. The
 * original trap horse is replaced with an elder guardian, ridden by a witch.</li>
 * <li>Witches on cave spiders.</li>
 * <li>Stone sword wielding zombies on zombie horses.</li>
 * <li>Blazes riding ghasts.</li>
 * <li>Baby zombies with stone swords riding chickens.</li>
 * </ul>
 */
public class ItsATrap extends JavaPlugin implements Listener {
    /**
     * Configuration wrapper instance.
     */
    public static Configuration CONFIG = new Configuration();

    /**
     * This plugin, accessible as, effectively, a singleton.
     */
    public static ItsATrap PLUGIN;

    // ------------------------------------------------------------------------
    /**
     * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
     */
    @Override
    public void onEnable() {
        PLUGIN = this;

        saveDefaultConfig();
        CONFIG.reload();

        getServer().getPluginManager().registerEvents(this, this);
    }

    // ------------------------------------------------------------------------
    /**
     * @see org.bukkit.plugin.java.JavaPlugin#onCommand(org.bukkit.command.CommandSender,
     *      org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(getName())) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                CONFIG.reload();
                sender.sendMessage(ChatColor.GOLD + getName() + " configuration reloaded.");
                return true;
            }
        }

        sender.sendMessage(ChatColor.RED + "Invalid command syntax.");
        return true;
    }

    // ------------------------------------------------------------------------
    /**
     * Handle spawns of the trap horses and their jockeys.
     *
     * Testing reveals that the trap horses and jockeys all spawn at the same
     * block x, y and z as the trigger horse (probably at the exact same
     * floating point coordinates).
     *
     * A jockey is spawned first (SpawnReason.JOCKEY), and presumably is set as
     * the rider of the original trigger horse. However, it apparently doesn't
     * have a vehicle set when it spawns, which makes it very difficult to
     * distinguish it from a spider jockey (thanks Mojang). If the jockey has no
     * mount, we will search for a skeletal horse at the jockey coordinates and
     * assume that it is the mount.
     *
     * The three subsequent jockeys are each proceeded by their trap horses; the
     * spawns are skeleton horse (SpawnReason.TRAP) then skeleton
     * (SpawnReason.Jockey), repeated three times. There seems to be no distance
     * between the spawn locations, nor any time delay.
     *
     * Further testing shows that the subsequent spawns don't happen if the
     * spawn of any reinforcement trap horses are cancelled or the mobs are
     * removed, so it seems that the reinforcement horses are themselves trigger
     * horses that trigger further spawns up to the limit for the trap.
     *
     * Therefore, our strategy for replacing the trap will be to cancel/remove
     * all subsequent spawns from Mojang code and simply spawn as many mobs as
     * we need in custom code.
     *
     * The initial trigger horse spawns with SpawnReason.LIGHTNING.
     */
    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        boolean isTestEgg = (CONFIG.DEBUG_ALLOW_SPAWN_EGGS && event.getSpawnReason() == SpawnReason.SPAWNER_EGG);

        if (event.getEntityType() == EntityType.SKELETON &&
            (event.getSpawnReason() == SpawnReason.JOCKEY || isTestEgg)) {
            // Note: SpawnReason.JOCKEY also covers behaviours like spider
            // jockeys and baby zombie chicken jockeys.
            Entity jockey = event.getEntity();
            Location loc = jockey.getLocation();
            Entity mount = jockey.getVehicle();
            if (mount == null) {
                mount = findTriggerHorse(loc);
            }
            if (isTestEgg || mount instanceof Horse) {
                if (isNewTrapLocation(loc)) {
                    _replacement = chooseReplacement(loc);
                }
                _replacement.onJockeySpawn(event);
            }

        } else if (event.getEntityType() == EntityType.HORSE) {
            if (event.getSpawnReason() == SpawnReason.LIGHTNING) {
                if (_random.nextDouble() > CONFIG.TRAP_CHANCE) {
                    event.setCancelled(true);
                }
            } else if (event.getSpawnReason() == SpawnReason.TRAP || isTestEgg) {
                // We assume that a jockey will always spawn first when the trap
                // is sprung and we don't need to check the trap location here.
                if (_replacement != null) {
                    _replacement.onTrapHorseSpawn(event);
                }
            }
        }
    }

    // ------------------------------------------------------------------------
    /**
     * Return true if the spawn location of a skeleton is different from the
     * last recorded jockey spawn location.
     *
     * In that situation, a {@link TrapReplacement} must be selected.
     *
     * @return true if the spawn location of a skeleton is different from the
     *         last recorded jockey spawn location.
     */
    protected boolean isNewTrapLocation(Location loc) {
        return _location == null || (!_location.getWorld().equals(loc.getWorld())) ||
               _location.distanceSquared(loc) > TRAP_IDENTITY_RADIUS * TRAP_IDENTITY_RADIUS;
    }

    // ------------------------------------------------------------------------
    /**
     * Randomly select a new {@link TrapReplacement} appropriate to the
     * specified location and update the stored current trap location.
     *
     * @param loc the new trap location.
     * @return a new {@link TrapReplacement}.
     */
    protected TrapReplacement chooseReplacement(Location loc) {
        _location = loc;
        Biome biome = loc.getBlock().getBiome();
        if (biome == Biome.OCEAN || biome == Biome.DEEP_OCEAN) {
            _replacement = TrapReplacement.GUARDIAN_TRAP;
        } else {
            _replacement = TrapReplacement.VALUES[1 + _random.nextInt(TrapReplacement.VALUES.length - 1)];
        }
        _replacement.trapChanged(_location);
        return _replacement;
    }

    // ------------------------------------------------------------------------
    /**
     * Find a trigger horse within the block of the specified location.
     *
     * @param loc the location.
     * @return the trigger horse, or null if not found.
     */
    protected static Horse findTriggerHorse(Location loc) {
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, 0.5, 0.5, 0.5)) {
            if (entity instanceof Horse && ((Horse) entity).getVariant() == Variant.SKELETON_HORSE) {
                return (Horse) entity;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------------
    /**
     * Maximum distance (in blocks) plane between spawns such that they are
     * considered to be part of the same trap.
     *
     * Testing reveals that the trap horses and jockeys spawn at the same block
     * x, y and z as the original trigger horse.
     */
    protected static final double TRAP_IDENTITY_RADIUS = 2.0;

    /**
     * Currently active replacement method;
     */
    protected TrapReplacement _replacement;

    /**
     * Location of the most recent trap jockey spawn.
     */
    protected Location _location;

    /**
     * Random number generator.
     */
    protected Random _random = new Random();

} // class ItsATrap