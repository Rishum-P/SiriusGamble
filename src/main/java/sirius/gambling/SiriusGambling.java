package sirius.gambling;

import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import sirius.gambling.commands.CommandRegisterer;
import sirius.gambling.events.EventRegisterer;
import sirius.gambling.events.InventoryEventHandler;
import sirius.gambling.util.Config;
import sirius.gambling.util.SQLManager;

public class SiriusGambling extends JavaPlugin {
    public SiriusGambling instance = this;
    public static Economy econ = null;
    public static Permission perms = null;
    public static boolean vaultPresent = false;
    public static Plugin plugin;
    public static SiriusGambling siriusGambling;
    private SQLManager sql;



    Logger log;

    public void onEnable() {
        plugin = this;
        siriusGambling = this;

        this.log = getLogger();

        log.info("SiriusGambling is starting...");

        Config.setup();
        Config.get().addDefault("sql.hostname","localhost");
        Config.get().addDefault("sql.port",3306);
        Config.get().addDefault("sql.username","root");
        Config.get().addDefault("sql.password","changeme");
        Config.get().addDefault("sql.database","Database-1");
        Config.get().options().copyDefaults(true);
        Config.save();

        log.info("[SiriusUtils] Connecting To Database...");
        initDatabase();


        CommandRegisterer.registerCommands(this);
        EventRegisterer.RegisterEvents();

        if (checkVault()) {
            if (setupEconomy()) {
                this.log.info("Economy set up.");
            } else {
                this.log.warning("No economy plugin detected.");
                getPluginLoader().disablePlugin((Plugin) this);
                return;
            }
            if (setupPermissions()) {
                this.log.info("Permissions set up.");
            } else {
                this.log.warning("No permission plugin detected");
                getPluginLoader().disablePlugin((Plugin) this);
                return;
            }
        }

    }

    public void onDisable() {
        sql.onDisable();
        log.info("SiriusGambling is stopping");
    }


    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = (Economy) rsp.getProvider();
        return (econ != null);
    }


    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = (Permission) rsp.getProvider();
        return (perms != null);
    }

    private boolean checkVault() {
        vaultPresent = (getServer().getPluginManager().getPlugin("Vault") != null);
        return vaultPresent;
    }

    private void initDatabase() {
        sql = new SQLManager(this);
    }

    public SQLManager getSQLManager() {
        return sql;
    }

}

