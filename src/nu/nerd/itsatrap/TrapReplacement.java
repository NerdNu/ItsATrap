package nu.nerd.itsatrap;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Possible methods of replacing the skeleton trap.
 */
enum TrapReplacement {
    // ------------------------------------------------------------------------
    /**
     * This replacement type replaces the trap with witches riding guardians.
     */
    GUARDIAN(3, true) {
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
                mount.addPassenger(rider);

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
     * This replacement type replaces the trap with drowned riding dolphins.
     */
    DOLPHIN(3, true) {
        @Override
        protected void spawnCustomMobs() {
            if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
                ItsATrap.PLUGIN.getLogger().info("Dry run: would spawn a drowned on a dolphin at " + Util.formatLocation(_location));
            } else {
                if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                    ItsATrap.PLUGIN.getLogger().info("Spawning a drowned on a dolphin at " + Util.formatLocation(_location));
                }

                Drowned rider = _location.getWorld().spawn(_location, Drowned.class);
                Dolphin mount = _location.getWorld().spawn(_location, Dolphin.class);
                mount.addPassenger(rider);
                rider.getEquipment().setHelmet(new ItemStack(Material.CYAN_STAINED_GLASS));
                rider.getEquipment().setItemInMainHand(new ItemStack(Material.TRIDENT));
                rider.getEquipment().setItemInMainHandDropChance(ItsATrap.CONFIG.TRIDENT_DROP_CHANCE);

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
     * This replacement type replaces the trap with drowned riding zombie
     * horses.
     */
    TRIDENT(3, false) {
        @Override
        protected void spawnCustomMobs() {
            if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
                ItsATrap.PLUGIN.getLogger().info("Dry run: would spawn a drowned on a zombie horse at " + Util.formatLocation(_location));
            } else {
                if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                    ItsATrap.PLUGIN.getLogger().info("Spawning a drowned on a zombie horse at " + Util.formatLocation(_location));
                }

                Drowned rider = _location.getWorld().spawn(_location, Drowned.class);
                ZombieHorse mount = _location.getWorld().spawn(_location, ZombieHorse.class);
                mount.setAdult();
                mount.setTamed(true);
                mount.addPassenger(rider);
                rider.getEquipment().setHelmet(new ItemStack(Material.CYAN_STAINED_GLASS));
                rider.getEquipment().setItemInMainHand(new ItemStack(Material.TRIDENT));
                rider.getEquipment().setItemInMainHandDropChance(ItsATrap.CONFIG.TRIDENT_DROP_CHANCE);
                rider.setCanPickupItems(false);

                Player player = findNearestPlayer();
                if (player != null) {
                    rider.setTarget(player);
                }
            }
        }
    },

    // ------------------------------------------------------------------------
    /**
     * This replacement type retains Mojang's vanilla skeleton trap, unchanged.
     */
    SKELETON(4, false) {
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
    POISON(3, false) {
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
                mount.addPassenger(rider);

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
    ZOMBIE(4, false) {
        @Override
        protected void spawnCustomMobs() {
            if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
                ItsATrap.PLUGIN.getLogger().info("Dry run: would spawn a zombie riding a zombie horse at " + Util.formatLocation(_location));
            } else {
                if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                    ItsATrap.PLUGIN.getLogger().info("Spawning a zombie riding a zombie horse at " + Util.formatLocation(_location));
                }

                Zombie rider = _location.getWorld().spawn(_location, Zombie.class);
                ZombieHorse mount = _location.getWorld().spawn(_location, ZombieHorse.class);
                mount.setAdult();
                mount.setTamed(true);
                mount.addPassenger(rider);
                rider.getEquipment().setHelmet(new ItemStack(Material.CARVED_PUMPKIN));
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
     * This replacement type spawns ghasts.
     */
    GHAST(4, false) {
        @Override
        protected void spawnCustomMobs() {
            if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
                ItsATrap.PLUGIN.getLogger().info("Dry run: would spawn a ghast at " + Util.formatLocation(_location));
            } else {
                if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                    ItsATrap.PLUGIN.getLogger().info("Spawning a ghast at " + Util.formatLocation(_location));
                }

                Ghast ghast = _location.getWorld().spawn(_location, Ghast.class);
                Player player = findNearestPlayer();
                if (player != null) {
                    ghast.setTarget(player);
                }
            }
        }
    },

    // ------------------------------------------------------------------------
    /**
     * This replacement type spawns baby zombies riding chickens.
     */
    CHICKEN(4, false) {
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
                mount.addPassenger(rider);
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
    CREEPER(4, false) {
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
    },

    // ------------------------------------------------------------------------
    /**
     * This replacement type spawns three vindicators.
     */
    VINDICATOR(3, false) {
        @Override
        protected void spawnCustomMobs() {
            if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
                ItsATrap.PLUGIN.getLogger().info("Dry run: would spawn a vindicator at " + Util.formatLocation(_location));
            } else {
                if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                    ItsATrap.PLUGIN.getLogger().info("Spawning a vindicator at " + Util.formatLocation(_location));
                }

                Vindicator mob = _location.getWorld().spawn(_location, Vindicator.class);
                Player player = findNearestPlayer();
                if (player != null) {
                    mob.setTarget(player);
                }
            }
        }
    },

    // ------------------------------------------------------------------------
    /**
     * This replacement type spawns one evoker.
     */
    EVOKER(1, false) {
        @Override
        protected void spawnCustomMobs() {
            if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
                ItsATrap.PLUGIN.getLogger().info("Dry run: would spawn an evoker at " + Util.formatLocation(_location));
            } else {
                if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                    ItsATrap.PLUGIN.getLogger().info("Spawning an evoker at " + Util.formatLocation(_location));
                }

                Evoker mob = _location.getWorld().spawn(_location, Evoker.class);
                Player player = findNearestPlayer();
                if (player != null) {
                    mob.setTarget(player);
                }
            }
        }
    },

    // ------------------------------------------------------------------------
    /**
     * This replacement type spawns three illusioners.
     */
    ILLUSIONER(3, false) {
        @Override
        protected void spawnCustomMobs() {
            if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
                ItsATrap.PLUGIN.getLogger().info("Dry run: would spawn an illusioner at " + Util.formatLocation(_location));
            } else {
                if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                    ItsATrap.PLUGIN.getLogger().info("Spawning an illusioner at " + Util.formatLocation(_location));
                }

                Illusioner mob = _location.getWorld().spawn(_location, Illusioner.class);
                Player player = findNearestPlayer();
                if (player != null) {
                    mob.setTarget(player);
                }
            }
        }
    },

    // ------------------------------------------------------------------------
    /**
     * This replacement type spawns some bugs.
     */
    BUG(10, false) {
        @Override
        protected void spawnCustomMobs() {
            if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
                ItsATrap.PLUGIN.getLogger().info("Dry run: would spawn bug at " + Util.formatLocation(_location));
            } else {
                if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                    ItsATrap.PLUGIN.getLogger().info("Spawning bug at " + Util.formatLocation(_location));
                }

                EntityType entityType = (Math.random() < 0.5 ? EntityType.SILVERFISH : EntityType.ENDERMITE);
                Creature mob = (Creature) _location.getWorld().spawnEntity(_location, entityType);
                Player player = findNearestPlayer();
                if (player != null) {
                    mob.setTarget(player);
                }
            }
        }
    },

    // ------------------------------------------------------------------------
    /**
     * This replacement type spawns shulkers.
     */
    SHULKER(3, false) {
        @Override
        protected void spawnCustomMobs() {
            if (ItsATrap.CONFIG.DEBUG_DRY_RUN) {
                ItsATrap.PLUGIN.getLogger().info("Dry run: would spawn shulker at " + Util.formatLocation(_location));
            } else {
                if (ItsATrap.CONFIG.DEBUG_SPAWNS) {
                    ItsATrap.PLUGIN.getLogger().info("Spawning shulker at " + Util.formatLocation(_location));
                }

                // Try up to 3 times to find a spawning location.
                Location spawnLoc = null;
                for (int i = 0; i < 3; ++i) {
                    int dx = (int) Math.round(Math.random() * 10 - 5);
                    int dz = (int) Math.round(Math.random() * 10 - 5);
                    Location loc = _location.clone().add(dx, 0, dz);
                    Block block = loc.getBlock();
                    if (block.getType() == Material.AIR) {
                        spawnLoc = loc;
                        break;
                    }
                }

                if (spawnLoc != null) {
                    Shulker mob = spawnLoc.getWorld().spawn(spawnLoc, Shulker.class);
                    Player player = findNearestPlayer();
                    if (player != null) {
                        mob.setTarget(player);
                    }
                }
            }
        }
    };

    // ------------------------------------------------------------------------
    /**
     * TrapReplacements that spawn in aquatic biomes.
     */
    public static ArrayList<TrapReplacement> AQUATIC_REPLACEMENTS = new ArrayList<>();

    /**
     * TrapReplacements that spawn in dry land biomes.
     */
    public static ArrayList<TrapReplacement> LAND_REPLACEMENTS = new ArrayList<>();

    // ------------------------------------------------------------------------
    // Initialise AQUATIC_REPLACEMENTS and LAND_REPLACEMENTS.

    static {
        for (TrapReplacement replacement : values()) {
            if (replacement.isAquatic()) {
                AQUATIC_REPLACEMENTS.add(replacement);
            } else {
                LAND_REPLACEMENTS.add(replacement);
            }
        }
    }

    // ------------------------------------------------------------------------
    /**
     * Constructor.
     *
     * @param spawnCount the number of calls to
     * @param aquatic if true, the replacement only spawns in aquatic biomes.
     */
    TrapReplacement(int spawnCount, boolean aquatic) {
        _spawnCount = spawnCount;
        _aquatic = aquatic;
    }

    // ------------------------------------------------------------------------
    /**
     * Return true if the replacement only spawns in aquatic biomes.
     * 
     * @return true if the replacement only spawns in aquatic biomes.
     */
    public boolean isAquatic() {
        return _aquatic;
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
                AbstractHorse trapHorse = ItsATrap.findTriggerHorse(_location);
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

    /**
     * If true, the replacement only spawns in aquatic biomes.
     */
    protected boolean _aquatic;
} // class TrapReplacement