package sirius.gambling;

import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SiriusGambling extends JavaPlugin {
	public SiriusGambling instance = this;
    public static Economy econ = null;
    public static Permission perms = null;    
    public static boolean vaultPresent = false;
  
    Logger log;
    
    public void onEnable() { 
    	this.log = getLogger();
    	if (checkVault()) {
    		if (setupEconomy()) { 
    			this.log.info("Economy set up."); 
    		} else { 
    			this.log.warning("No economy plugin detected.");
    			getPluginLoader().disablePlugin((Plugin)this);
    			return; 
    		} 
    if (setupPermissions()) {
        this.log.info("Permissions set up.");
      } else {
        this.log.warning("No permission plugin detected");
        getPluginLoader().disablePlugin((Plugin)this);
        return;
      } 
    } 
    InventoryEventHandler inventoryEventHandler = new InventoryEventHandler();
    getServer().getPluginManager().registerEvents(inventoryEventHandler, (Plugin)this);
  }
  
  public void onDisable() {


  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("gamble") && sender instanceof Player) {
      
      Player player = (Player)sender;
      if (args.length > 0) {
        if (args.length == 1) {
          double bet = 0.0D;
          try {
            bet = Double.parseDouble(args[0]);
          } catch (Exception e) {
                    player.sendMessage(ChatColor.YELLOW + "---------------[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "]---------------");
				    player.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.GREEN + "/gamble (bet)");
					player.sendMessage(ChatColor.RED + "How it works:");
					player.sendMessage(ChatColor.GREEN + "Match 3 Colors = Win 3x Bet");
					player.sendMessage(ChatColor.GREEN + "Match 2 Colors = Win 2x Bet");
					player.sendMessage(ChatColor.GREEN + "Match No Colors = No Winnings");
				    player.sendMessage(ChatColor.GOLD + "Minimum Bet is: " + ChatColor.GREEN + "$50");
				    player.sendMessage(ChatColor.GOLD + "Maximum Bet is: " + ChatColor.GREEN + "$5000");
	                player.sendMessage(ChatColor.GOLD + "Usage Cooldown: " + ChatColor.GREEN + "10 Minutes");
					player.sendMessage(ChatColor.YELLOW + "------------------------------------------");
            return false;
          } 
          if (econ.has((OfflinePlayer)player, bet)) {
            if (bet > 5000.0D) {
              player.sendMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.GREEN + "Maximum bet is $5000!");
              return true;
            } 
            if (bet < 50.0D) {
              player.sendMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.GREEN + "Minimum bet is $50!");
              return true;
            } 
            econ.withdrawPlayer((OfflinePlayer)player, bet);
            Slots slot = new Slots(player, bet, this.instance);
            slot.createInv();
          } else {
            player.sendMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.GREEN + "You do not have enough money!");
          } 
        } else {
          return false;
        } 
      } else {
        player.sendMessage(ChatColor.RED + "Usage: /gamble slots (bet)");
        return true;
      } 
      return true;
    } 
    return false;
  }
  
  private boolean setupEconomy() {
    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null)
      return false; 
    econ = (Economy)rsp.getProvider();
    return (econ != null);
  }

  
  private boolean setupPermissions() {
    RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
    perms = (Permission)rsp.getProvider();
    return (perms != null);
  }
  
  private boolean checkVault() {
    vaultPresent = (getServer().getPluginManager().getPlugin("Vault") != null);
    return vaultPresent;
	}
}
