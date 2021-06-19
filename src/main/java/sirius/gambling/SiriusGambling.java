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

public class SiriusGambling extends JavaPlugin {
    public SiriusGambling instance = this;
    public static Economy econ = null;
    public static Permission perms = null;
    public static boolean vaultPresent = false;
    public static Plugin plugin;


    Logger log;

    public void onEnable() {
        plugin = this;

        this.log = getLogger();

        log.info("SiriusGambling is starting...");
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

}
