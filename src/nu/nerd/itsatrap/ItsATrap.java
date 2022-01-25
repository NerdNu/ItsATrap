package nu.nerd.itsatrap;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;

// ----------------------------------------------------------------------------
/**
 * Customised trap horse handling.
 *
 * See {@link TrapReplacement} for possible trap replacements.
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
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase(getName())) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    return false;
                } else if (args[0].equalsIgnoreCase("reload")) {
                    CONFIG.reload();
                    sender.sendMessage(ChatColor.GOLD + getName() + " configuration reloaded.");
                    return true;
                } else if (args[0].equalsIgnoreCase("types")) {
                    String aquaticReplacements = TrapReplacement.AQUATIC_REPLACEMENTS.stream()
                        .map(r -> ChatColor.YELLOW + r.toString()).collect(Collectors.joining(ChatColor.GRAY + ", "));
                    sender.sendMessage(ChatColor.GOLD + "Aquatic trap replacement types: " + aquaticReplacements);
                    String landReplacements = TrapReplacement.LAND_REPLACEMENTS.stream()
                        .map(r -> ChatColor.YELLOW + r.toString()).collect(Collectors.joining(ChatColor.GRAY + ", "));
                    sender.sendMessage(ChatColor.GOLD + "Dry land trap replacement types: " + landReplacements);
                    return true;
                } else if (args[0].equalsIgnoreCase("type")) {
                    _forcedReplacement = null;
                    sender.sendMessage(ChatColor.GOLD + "The trap replacement type will be randomly selected from now on.");
                    return true;
                }
            } else if (args.length == 2 && args[0].equalsIgnoreCase("type")) {
                String typeName = args[1].toUpperCase();
                try {
                    _forcedReplacement = TrapReplacement.valueOf(typeName);
                    sender.sendMessage(ChatColor.GOLD + "The trap replacement type will be " +
                                       _forcedReplacement.toString() + " until the next restart.");
                } catch (IllegalArgumentException ex) {
                    sender.sendMessage(ChatColor.RED + typeName + " is not a valid trap replacement type.");
                }
                return true;
            }
        }

        sender.sendMessage(ChatColor.RED + "Invalid command syntax. Use \"/" +
                           alias + " help\" for help.");
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
     * The sequence of spawn events (EntityType because SpawnReason) is:
     * <ul>
     * <li>SKELETON_HORSE because LIGHTNING (the trap horse)</li>
     * <li>Player triggers the trap horse.</li>
     * <li>SKELETON because TRAP
     * <li>
     * <li>Then three repetitions of:</li>
     * <ul>
     * <li>SKELETON_HORSE reason JOCKEY</li>
     * <li>SKELETON reason JOCKEY</li>
     * </ul>
     * </ul>
     *
     * Once the trap is triggered, the skeleton horses and their jockeys all
     * spawn at the same location and in the same tick.
     *
     * The first skeleton jockey spawns with reason SpawnReason.TRAP, which
     * should make it easy enough to distinguish from a spider jocky (thanks,
     * Mojang!). :) If the jockey has no mount, we will search for a skeletal
     * horse at the jockey coordinates and assume that it is the mount.
     *
     * Further testing shows that the subsequent spawns don't happen if the
     * spawn of any reinforcement trap horses are cancelled or the mobs are
     * removed, so it seems that the reinforcement horses are themselves trigger
     * horses that trigger further spawns up to the limit for the trap.
     *
     * Therefore, our strategy for replacing the trap will be to cancel/remove
     * all subsequent spawns from Mojang code and simply spawn as many mobs as
     * we need in custom code.
     */
    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        boolean isTestEgg = (CONFIG.DEBUG_ALLOW_SPAWN_EGGS && event.getSpawnReason() == SpawnReason.SPAWNER_EGG);

        Location loc = event.getLocation();
        EntityType entityType = event.getEntityType();
        SpawnReason reason = event.getSpawnReason();

        // For debugging when Mojang changes the spawn algorithm again.
        // if (entityType == EntityType.SKELETON_HORSE || (loc.getY() > 63 &&
        // entityType == EntityType.SKELETON)) {
        // getLogger().info(entityType + " because " + reason + " at " +
        // Util.formatLocation(loc));
        // }

        if (entityType == EntityType.SKELETON &&
            (reason == SpawnReason.TRAP || isTestEgg)) {
            Entity jockey = event.getEntity();
            Entity mount = jockey.getVehicle();
            if (mount == null) {
                mount = findTriggerHorse(loc);
            }

            if (CONFIG.DEBUG_VANILLA_SPAWNS) {
                if (mount != null) {
                    getLogger().info("Skeleton jockey spawned at " +
                                     Util.formatLocation(event.getEntity().getLocation()));
                }
            }

            if (isTestEgg || mount instanceof SkeletonHorse) {
                if (isNewTrapLocation(loc)) {
                    _replacement = chooseReplacement(loc);
                }
                _replacement.onJockeySpawn(event);
            }

        } else if (entityType == EntityType.SKELETON_HORSE) {
            if (reason == SpawnReason.LIGHTNING) {
                if (_random.nextDouble() < CONFIG.TRAP_CHANCE) {
                    if (CONFIG.DEBUG_SPAWNS) {
                        getLogger().info("Trap trigger horse spawned at " +
                                         Util.formatLocation(event.getEntity().getLocation()));
                    }
                } else {
                    event.setCancelled(true);
                    if (CONFIG.DEBUG_SPAWNS) {
                        getLogger().info("Cancelling trigger horse spawn at " +
                                         Util.formatLocation(event.getEntity().getLocation()));
                    }
                }
            } else if (reason == SpawnReason.JOCKEY) {
                if (CONFIG.DEBUG_VANILLA_SPAWNS) {
                    getLogger().info("Trap horse spawned at " +
                                     Util.formatLocation(event.getEntity().getLocation()));

                }
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
     * If a specific type has been selected with /itsatrap type <type>, then
     * that type will always be returned, until the next restart.
     *
     * @param loc the new trap location.
     * @return a new {@link TrapReplacement}.
     */
    protected TrapReplacement chooseReplacement(Location loc) {
        _location = loc;

        TrapReplacement replacement;
        if (_forcedReplacement != null) {
            replacement = _forcedReplacement;
        } else {
            Biome biome = loc.getBlock().getBiome();
            ArrayList<TrapReplacement> replacementOptions = AQUATIC_BIOMES.contains(biome) ? TrapReplacement.AQUATIC_REPLACEMENTS
                                                                                           : TrapReplacement.LAND_REPLACEMENTS;
            replacement = replacementOptions.get(_random.nextInt(replacementOptions.size()));
        }
        replacement.trapChanged(_location);
        return replacement;
    }

    // ------------------------------------------------------------------------
    /**
     * Find a trigger horse within the block of the specified location.
     *
     * @param loc the location.
     * @return the trigger horse, or null if not found.
     */
    protected static AbstractHorse findTriggerHorse(Location loc) {
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, 0.5, 0.5, 0.5)) {
            if (entity.getType() == EntityType.SKELETON_HORSE) {
                return (SkeletonHorse) entity;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------------
    /**
     * The set of all aquatic biomes, where aquatic trap replacements occur.
     */
    protected static final EnumSet<Biome> AQUATIC_BIOMES = EnumSet.noneOf(Biome.class);
    static {
        for (Biome biome : Biome.values()) {
            if ((biome.name().contains("RIVER") || biome.name().contains("OCEAN")) &&
                !biome.name().contains("LEGACY")) {
                AQUATIC_BIOMES.add(biome);
            }
        }
    };
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
     * If not null, all trap replacements will be of this type.
     */
    protected TrapReplacement _forcedReplacement;

    /**
     * Location of the most recent trap jockey spawn.
     */
    protected Location _location;

    /**
     * Random number generator.
     */
    protected Random _random = new Random();

} // class ItsATrap