package sirius.gambling.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryEventHandler implements Listener {
  @EventHandler(priority = EventPriority.LOWEST)
  public void onInventoryClickEvent(InventoryClickEvent event) {
    if (event.getWhoClicked() instanceof org.bukkit.entity.Player) {
      Inventory inv = event.getInventory();
      if (inv.contains(Material.valueOf("Sirius")))
        event.setCancelled(true); 
    } 
  }
}
