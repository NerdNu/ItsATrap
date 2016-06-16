package nu.nerd.itsatrap;

// ----------------------------------------------------------------------------
/**
 * Reads and exposes the plugin configuration.
 */
public class Configuration {
    /**
     * If true, log configuration loading.
     */
    public boolean DEBUG_CONFIG;

    /**
     * If true, log the actions that would be taken, but don't perform them.
     */
    public boolean DEBUG_DRY_RUN;

    /**
     * If true, log mob spawns.
     */
    public boolean DEBUG_SPAWNS;

    /**
     * If true, spawn eggs can be used to simulate trap-related events.
     */
    public boolean DEBUG_ALLOW_SPAWN_EGGS;

    /**
     * The chance (range [0.0,1.0]) that a trap will spawn when the vanilla
     * skeleton trap would happen.
     *
     * A TRAP_CHANCE of 1.0 will allow all vanilla skeleton trap trigger horses
     * to spawn, while a chance of 0.0 will prevent all of them from spawning.
     */
    public double TRAP_CHANCE;

    /**
     * The probability (range [0.0,1.0]) that a creeper spawned in a Creeper
     * Trap will be a charged creeper.
     */
    public double CREEPER_CHARGE_CHANCE;

    // ------------------------------------------------------------------------
    /**
     * Load the plugin configuration.
     */
    public void reload() {
        ItsATrap.PLUGIN.reloadConfig();

        DEBUG_CONFIG = ItsATrap.PLUGIN.getConfig().getBoolean("debug.config");
        DEBUG_DRY_RUN = ItsATrap.PLUGIN.getConfig().getBoolean("debug.dry-run");
        DEBUG_SPAWNS = ItsATrap.PLUGIN.getConfig().getBoolean("debug.spawns");
        DEBUG_ALLOW_SPAWN_EGGS = ItsATrap.PLUGIN.getConfig().getBoolean("debug.allow-spawn-eggs");
        TRAP_CHANCE = ItsATrap.PLUGIN.getConfig().getDouble("trap-chance");
        CREEPER_CHARGE_CHANCE = ItsATrap.PLUGIN.getConfig().getDouble("creeper-charge-chance");

        if (DEBUG_CONFIG) {
            ItsATrap.PLUGIN.getLogger().info("Configuration: ");
            ItsATrap.PLUGIN.getLogger().info("DEBUG_DRY_RUN: " + DEBUG_DRY_RUN);
            ItsATrap.PLUGIN.getLogger().info("DEBUG_SPAWNS: " + DEBUG_SPAWNS);
            ItsATrap.PLUGIN.getLogger().info("DEBUG_ALLOW_SPAWN_EGGS: " + DEBUG_ALLOW_SPAWN_EGGS);
            ItsATrap.PLUGIN.getLogger().info("TRAP_CHANCE: " + TRAP_CHANCE);
            ItsATrap.PLUGIN.getLogger().info("CREEPER_CHARGE_CHANCE: " + CREEPER_CHARGE_CHANCE);
        }
    } // reload
} // class Configuration