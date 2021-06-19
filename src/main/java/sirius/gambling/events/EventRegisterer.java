package sirius.gambling.events;
import sirius.gambling.SiriusGambling;

public class EventRegisterer {
    public static void RegisterEvents() {
        SiriusGambling.plugin.getServer().getPluginManager().registerEvents(new InventoryEventHandler(), SiriusGambling.plugin);
    }

}
