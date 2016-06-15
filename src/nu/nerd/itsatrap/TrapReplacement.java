package nu.nerd.itsatrap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Possible methods of replacing the skeleton trap.
 */
enum TrapReplacement {
    // ------------------------------------------------------------------------
    /**
     * This replacement type replaces the trap with witches riding guardians.
     *
     * It is only applicable in ocean and deep ocean biomes.
     */
    GUARDIAN_TRAP(3) {
        @Override
        protected void spawnCustomMobs() {
            if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
                ItsATrap.PLUGIN.getLogger().info("Dry run: would spawn a guardian and witch at " + Util.formatLocation(_location));
            } else {
                if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                    ItsATrap.PLUGIN.getLogger().info("Spawning a guardian and witch at " + Util.formatLocation(_location));
                }

                Witch rider = _location.getWorld().spawn(_location, Witch.class);
                Guardian mount = _location.getWorld().spawn(_location, Guardian.class);
                mount.setPassenger(rider);

                Player player = findNearestPlayer();
                if (player != null) {
                    rider.setTarget(player);
                    mount.setTarget(player);
                }
            }
        }
    },

    // ------------------------------------------------------------------------
    /**
     * This replacement type retains Mojang's vanilla skeleton trap, unchanged.
     */
    SKELETON_TRAP(4) {
        @Override
        protected void onTrapHorseSpawn(CreatureSpawnEvent event) {
            if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                ItsATrap.PLUGIN.getLogger().info("Vanilla code Spawning a skeleton trap horse at " + Util.formatLocation(_location));
            }
        }

        @Override
        protected void onJockeySpawn(CreatureSpawnEvent event) {
            if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                ItsATrap.PLUGIN.getLogger().info("Vanilla code Spawning a skeleton trap jockey at " + Util.formatLocation(_location));
            }
        }

        @Override
        protected void spawnCustomMobs() {
            // Nothing to do.
        }

        @Override
        protected void strikeLightning() {
            // No additional lightning needed.
        }
    },

    // ------------------------------------------------------------------------
    /**
     * This replacement type spawns witches riding cave spiders.
     */
    POISON_TRAP(3) {
        @Override
        protected void spawnCustomMobs() {
            if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
                ItsATrap.PLUGIN.getLogger().info("Dry run: would spawn a witch riding a cave spider at " + Util.formatLocation(_location));
            } else {
                if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                    ItsATrap.PLUGIN.getLogger().info("Spawning a witch riding a cave spider at " + Util.formatLocation(_location));
                }

                Witch rider = _location.getWorld().spawn(_location, Witch.class);
                CaveSpider mount = _location.getWorld().spawn(_location, CaveSpider.class);
                mount.setPassenger(rider);

                Player player = findNearestPlayer();
                if (player != null) {
                    rider.setTarget(player);
                    mount.setTarget(player);
                }
            }
        }
    },

    // ------------------------------------------------------------------------
    /**
     * This replacement type spawns zombies riding zombie horses.
     */
    ZOMBIE_TRAP(4) {
        @Override
        protected void spawnCustomMobs() {
            if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
                ItsATrap.PLUGIN.getLogger().info("Dry run: would spawn a zombie riding a zombie horse at " + Util.formatLocation(_location));
            } else {
                if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                    ItsATrap.PLUGIN.getLogger().info("Spawning a zombie riding a zombie horse at " + Util.formatLocation(_location));
                }

                Zombie rider = _location.getWorld().spawn(_location, Zombie.class);
                Horse mount = _location.getWorld().spawn(_location, Horse.class);
                mount.setVariant(Variant.UNDEAD_HORSE);
                mount.setAdult();
                mount.setTamed(true);
                mount.setPassenger(rider);
                rider.getEquipment().setHelmet(new ItemStack(Material.PUMPKIN));
                rider.getEquipment().setItemInMainHand(new ItemStack(Material.STONE_SWORD));

                Player player = findNearestPlayer();
                if (player != null) {
                    rider.setTarget(player);
                }
            }
        }
    },

    // ------------------------------------------------------------------------
    /**
     * This replacement type spawns blazes riding ghasts.
     */
    GHAST_TRAP(4) {
        @Override
        protected void spawnCustomMobs() {
            if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
                ItsATrap.PLUGIN.getLogger().info("Dry run: would spawn a blaze riding a ghast at " + Util.formatLocation(_location));
            } else {
                if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                    ItsATrap.PLUGIN.getLogger().info("Spawning a blaze riding a ghast at " + Util.formatLocation(_location));
                }

                Blaze rider = _location.getWorld().spawn(_location, Blaze.class);
                Ghast mount = _location.getWorld().spawn(_location, Ghast.class);
                mount.setPassenger(rider);

                Player player = findNearestPlayer();
                if (player != null) {
                    rider.setTarget(player);
                    // Undefined for ghast: mount.setTarget(player);
                }
            }
        }
    },

    // ------------------------------------------------------------------------
    /**
     * This replacement type spawns baby zombies riding chickens.
     */
    CHICKEN_TRAP(4) {
        @Override
        protected void spawnCustomMobs() {
            if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
                ItsATrap.PLUGIN.getLogger().info("Dry run: would spawn a baby zombie riding a chicken at " + Util.formatLocation(_location));
            } else {
                if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                    ItsATrap.PLUGIN.getLogger().info("Spawning a baby zombie riding a chicken at " + Util.formatLocation(_location));
                }

                Zombie rider = _location.getWorld().spawn(_location, Zombie.class);
                Chicken mount = _location.getWorld().spawn(_location, Chicken.class);
                mount.setPassenger(rider);
                rider.setBaby(true);
                rider.getEquipment().setItemInMainHand(new ItemStack(Material.STONE_SWORD));

                Player player = findNearestPlayer();
                if (player != null) {
                    rider.setTarget(player);
                }
            }
        }
    },

    // ------------------------------------------------------------------------
    /**
     * This replacement type spawns a mix of charged and uncharged creepers.
     */
    CREEPER_TRAP(4) {
        @Override
        protected void spawnCustomMobs() {
            if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
                ItsATrap.PLUGIN.getLogger().info("Dry run: would spawn a creeper at " + Util.formatLocation(_location));
            } else {
                boolean charged = (Math.random() < ItsATrap.CONFIG.CREEPER_CHARGE_CHANCE);
                if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                    ItsATrap.PLUGIN.getLogger().info("Spawning a " + (charged ? "charged" : "") + "creeper at " + Util.formatLocation(_location));
                }

                Creeper creeper = _location.getWorld().spawn(_location, Creeper.class);
                // Work around https://hub.spigotmc.org/jira/browse/SPIGOT-2335
                if (charged) {
                    creeper.setPowered(true);
                }

                Player player = findNearestPlayer();
                if (player != null) {
                    creeper.setTarget(player);
                }
            }
        }
    };

    // ------------------------------------------------------------------------
    /**
     * All values, computed only once.
     */
    public static TrapReplacement[] VALUES = values();

    // ------------------------------------------------------------------------
    /**
     * Constructor.
     *
     * @param spawnCount the number of calls to
     */
    TrapReplacement(int spawnCount) {
        _spawnCount = spawnCount;
    }

    // ------------------------------------------------------------------------
    /**
     * This method is called when a trap reinforcement horse is spawned.
     *
     * NOTE: this method is not called for the initial spawn of the trap trigger
     * horse.
     */
    protected void onTrapHorseSpawn(CreatureSpawnEvent event) {
        if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
            ItsATrap.PLUGIN.getLogger().info("Dry run: would cancel trap horse spawn at " + Util.formatLocation(_location));
        } else {
            event.setCancelled(true);
        }
    }

    // ------------------------------------------------------------------------
    /**
     * This method is called when a trap skeleton jockey spawns.
     *
     * The first jockey that spawns is intended to be set as the mount of the
     * original trigger horse.
     */
    protected void onJockeySpawn(CreatureSpawnEvent event) {
        if (_firstJockey) {
            if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
                ItsATrap.PLUGIN.getLogger().info("Dry run: would remove original trap horse at " + Util.formatLocation(_location));
            } else {
                Horse trapHorse = ItsATrap.findTriggerHorse(_location);
                if (trapHorse == null) {
                    ItsATrap.PLUGIN.getLogger().warning("Trap horse could not be found at " + Util.formatLocation(_location));
                } else {
                    trapHorse.remove();
                }
                for (int i = 0; i < _spawnCount; ++i) {
                    spawnCustomMobs();
                    strikeLightning();
                }
            }
            _firstJockey = false;
        }

        if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
            ItsATrap.PLUGIN.getLogger().info("Dry run: would cancel jockey spawn at " + Util.formatLocation(_location));
        } else {
            event.setCancelled(true);
        }
    }

    // ------------------------------------------------------------------------
    /**
     * Spawn the custom mount and rider at the trap location.
     *
     * This method is overridden by each TrapReplacement implementation to spawn
     * the custom mobs.
     */
    protected abstract void spawnCustomMobs();

    // ------------------------------------------------------------------------
    /**
     * Strike lighting at the trap location.
     *
     * This method is overridden in the case of the default trap to not strike
     * lightning, since vanilla code already does that.
     */
    protected void strikeLightning() {
        _location.getWorld().strikeLightningEffect(_location);
    }

    // ------------------------------------------------------------------------
    /**
     * This method is called when the current TrapReplacement instance must be
     * re-initialised because the trap location is changed.
     *
     * The new trap location is stored and the _firstJockey flag is set to
     * expect the first skeleton jockey to spawn.
     *
     * @param loc the new trap location.
     */
    protected void trapChanged(Location loc) {
        _location = loc;
        _firstJockey = true;
    }

    // ------------------------------------------------------------------------
    /**
     * Find a player within TRAP_RADIUS blocks of the current trap location.
     *
     * @return the player that presumably triggered the trap.
     */
    protected Player findNearestPlayer() {
        for (Entity entity : _location.getWorld().getNearbyEntities(_location, TRAP_RADIUS, TRAP_RADIUS, TRAP_RADIUS)) {
            if (entity instanceof Player) {
                return (Player) entity;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------------
    /**
     * According to the Minecraft wiki, skeleton traps are triggered when the
     * player ventures within 10 blocks.
     *
     * Add half a block for good measure.
     */
    private static final double TRAP_RADIUS = 10.5;

    // ------------------------------------------------------------------------
    /**
     * The current trap location.
     */
    protected Location _location;

    /**
     * If true, the first jockey of the trap has yet to spawn.
     */
    protected boolean _firstJockey;

    /**
     * Number of custom spawns to spawn, in total, for this trap type.
     */
    protected int _spawnCount;
} // class TrapReplacement