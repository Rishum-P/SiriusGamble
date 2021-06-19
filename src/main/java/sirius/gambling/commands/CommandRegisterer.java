package sirius.gambling.commands;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandRegisterer {
    public static void registerCommands(JavaPlugin pluginInstance) {
        pluginInstance.getCommand("gamble").setExecutor(new CMDGamble());
    }
}