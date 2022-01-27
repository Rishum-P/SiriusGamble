package sirius.gambling;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import sirius.gambling.util.SQLManager;

import java.util.List;
import java.util.Random;


public class Slots
{
  Player player;
  double bet = 0.0D;
  
  public static Inventory inv;

  ItemStack purple = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);

  ItemStack blue = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
  
  ItemStack yellow = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
  
  ItemStack green = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
  
  ItemStack pink = new ItemStack(Material.PINK_STAINED_GLASS_PANE);
  
  ItemStack gray = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
  
  ItemStack empty = new ItemStack(Material.GLASS_PANE);
  

  Random random = new Random(System.currentTimeMillis());

  @Deprecated
  public Slots(Player player, double bet) {
    this.player = player;
    this.bet = bet;
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
    rollTask.runTaskTimerAsynchronously(SiriusGambling.plugin, 5L, 2L);
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

  @Deprecated
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
      SiriusGambling.econ.depositPlayer(this.player, this.bet * 3.0D);
      Bukkit.broadcastMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.GREEN + this.player.getPlayerListName() + " just bet " + this.bet + " and won " + (this.bet * 3.0D) + "!");
      updateStats(this.bet * 3.0D);

    } else if (ni1 == ni2 || ni2 == ni3 || ni1 == ni3) {
      ItemMeta iM = i1.getItemMeta();
      iM.setDisplayName(ChatColor.YELLOW + "YOU WON");
      i1.setItemMeta(iM);
      i2.setItemMeta(iM);
      i3.setItemMeta(iM);
      this.player.sendMessage(ChatColor.GREEN + "You won! " + (this.bet * 2.0D) + " added to your balance!");
      SiriusGambling.econ.depositPlayer(this.player, this.bet * 2.0D);
      Bukkit.broadcastMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.GREEN + this.player.getPlayerListName() + " just bet " + this.bet + " and won " + (this.bet * 2.0D) + "!");
      updateStats(this.bet * 2.0D);

    } else {
      ItemMeta iM = i1.getItemMeta();
      iM.setDisplayName(ChatColor.GRAY + "BAD LUCK");
      i1.setItemMeta(iM);
      i2.setItemMeta(iM);
      i3.setItemMeta(iM);
      Bukkit.broadcastMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + "SiriusGamble" + ChatColor.YELLOW + "] " + ChatColor.RED + this.player.getPlayerListName() + " just bet " + this.bet + " and lost it all!");
      updateStats(0.0D);

    }
  }


  public void updateStats(double amount_won) {

    int Won = 0;
    int amount_lost = 0;

    if (amount_won != 0.0D) {
      Won = 1;
    }

    if (amount_won == 0.0D) {
      amount_lost = (int) this.bet;
    }


    if (SiriusGambling.siriusGambling.getSQLManager().PlayerExists(player.getUniqueId().toString())) {

      List<Number> test = SiriusGambling.siriusGambling.getSQLManager().GetData(player.getUniqueId().toString());
      int Games_Played = (int) test.get(1);
      int Games_Won = (int) test.get(2);
      int Amount_Won = (int) test.get(3);
      int Amount_Lost = (int) test.get(4);
      int Total_Bet = (int) test.get(5);

      Games_Played++;
      Games_Won = Games_Won + Won;
      Amount_Won = Amount_Won + (int) amount_won;
      Amount_Lost = Amount_Lost + amount_lost;
      Total_Bet = Total_Bet + (int) this.bet;

      SiriusGambling.siriusGambling.getSQLManager().UpdateData(player.getUniqueId().toString(),player.getName(),Games_Played,Games_Won,Amount_Won,Amount_Lost,Total_Bet);

    }
    else {

      SiriusGambling.siriusGambling.getSQLManager().InsertFirstTime(player.getUniqueId().toString(), player.getName(), Won, (int) amount_won , amount_lost, (int) this.bet);
    }
  }

}

