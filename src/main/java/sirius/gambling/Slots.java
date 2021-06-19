package sirius.gambling;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Slots
{
  Player player;
  double bet = 0.0D;
  
  public static Inventory inv;
  
  ItemStack purple = new ItemStack(160);
  
  ItemStack blue = new ItemStack(160);
  
  ItemStack yellow = new ItemStack(160);
  
  ItemStack green = new ItemStack(160);
  
  ItemStack pink = new ItemStack(160);
  
  ItemStack gray = new ItemStack(160);
  
  ItemStack empty = new ItemStack(Material.THIN_GLASS);
  
  SiriusGambling plugin;
  
  Random random = new Random(System.currentTimeMillis());
  
  public Slots(Player player, double bet, SiriusGambling plugin) {
    this.player = player;
    this.bet = bet;
    this.plugin = plugin;
    this.purple.setDurability((short)2);
    this.blue.setDurability((short)3);
    this.yellow.setDurability((short)4);
    this.green.setDurability((short)5);
    this.pink.setDurability((short)6);
    this.gray.setDurability((short)7);
  }
  
  public void createInv() {
    inv = Bukkit.createInventory(null, 9, ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "Sirius" + ChatColor.YELLOW + "] " + ChatColor.BLUE + "SLOTS");
    inv.setItem(0, this.empty);
    inv.setItem(1, this.empty);
    inv.setItem(2, this.empty);
    inv.setItem(6, this.empty);
    inv.setItem(7, this.empty);
    inv.setItem(8, this.empty);
    this.player.openInventory(inv);
    BukkitRunnable rollTask = new BukkitRunnable() {
        private int i = 0;
        
        public void run() {
          if (this.i < 30) {
            Slots.this.roll();
            this.i++;
          } else {
            Slots.this.checkWin();
            cancel();
          } 
        }
      };
    rollTask.runTaskTimerAsynchronously((Plugin)this.plugin, 5L, 2L);
  }
  
  public void roll() {
    for (int i = 0; i < 3; i++) {
      int rnum = this.random.nextInt(6);
      switch (rnum) {
        case 0:
          inv.setItem(i + 3, this.purple);
          break;
        case 1:
          inv.setItem(i + 3, this.blue);
          break;
        case 2:
          inv.setItem(i + 3, this.yellow);
          break;
        case 3:
          inv.setItem(i + 3, this.green);
          break;
        case 4:
          inv.setItem(i + 3, this.pink);
          break;
        case 5:
          inv.setItem(i + 3, this.gray);
          break;
      } 
    } 
  }
  
  public void checkWin() {
    ItemStack i1 = inv.getItem(3);
    ItemStack i2 = inv.getItem(4);
    ItemStack i3 = inv.getItem(5);
    int ni1 = i1.getDurability();
    int ni2 = i2.getDurability();
    int ni3 = i3.getDurability();
    if (ni1 == ni2 && ni1 == ni3) {
      ItemMeta iM = i1.getItemMeta();
      iM.setDisplayName(ChatColor.RED + "YOU WON");
      i1.setItemMeta(iM);
      i2.setItemMeta(iM);
      i3.setItemMeta(iM);
      this.player.sendMessage(ChatColor.GREEN + "You won! " + (this.bet * 3.0D) + " added to your balance!");
      SiriusGambling.econ.depositPlayer((OfflinePlayer)this.player, this.bet * 3.0D);
      Bukkit.broadcastMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.GREEN + this.player
          .getPlayerListName() + " just bet " + this.bet + " and won " + (this.bet * 3.0D) + "!");
    } else if (ni1 == ni2 || ni2 == ni3 || ni1 == ni3) {
      ItemMeta iM = i1.getItemMeta();
      iM.setDisplayName(ChatColor.YELLOW + "YOU WON");
      i1.setItemMeta(iM);
      i2.setItemMeta(iM);
      i3.setItemMeta(iM);
      this.player.sendMessage(ChatColor.GREEN + "You won! " + (this.bet * 2.0D) + " added to your balance!");
      SiriusGambling.econ.depositPlayer((OfflinePlayer)this.player, this.bet * 2.0D);
      Bukkit.broadcastMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.GREEN + this.player
          .getPlayerListName() + " just bet " + this.bet + " and won " + (this.bet * 2.0D) + "!");
    } else {
      ItemMeta iM = i1.getItemMeta();
      iM.setDisplayName(ChatColor.GRAY + "BAD LUCK");
      i1.setItemMeta(iM);
      i2.setItemMeta(iM);
      i3.setItemMeta(iM);
      Bukkit.broadcastMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.RED + this.player
          .getPlayerListName() + " just bet " + this.bet + " and lost it all!");
   }
}
 }

