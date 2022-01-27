package sirius.gambling.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryEventHandler implements Listener {
  @EventHandler(priority = EventPriority.LOWEST)
  public void onInventoryClickEvent(InventoryClickEvent event) {
    if (event.getWhoClicked() instanceof org.bukkit.entity.Player) {
      Player player = (Player) event.getWhoClicked();

      if (player.getOpenInventory().getTitle().contains("Sirius"))
        event.setCancelled(true);
    }

  }
}
