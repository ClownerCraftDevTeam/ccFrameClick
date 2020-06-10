package net.clownercraft.ccframeclick;

import org.bukkit.Location;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class ccFrameClickListener implements Listener {

    @EventHandler
    public void onFrameClick(PlayerInteractEntityEvent event) {
        //return for anything on the off-hand and anything that isn't an item frame
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (!(event.getRightClicked() instanceof ItemFrame)) return;

        //see if the clicked frame is one defined in our config
        ItemFrame frame = (ItemFrame) event.getRightClicked();
        Location loc = frame.getLocation();
        if (ccFrameCommand.waiting) {
            if (event.getPlayer().getUniqueId().equals(ccFrameCommand.waitingUUID)) {
                //command is waiting for a click from this player
                if (ccFrameCommand.waitingCommand.equalsIgnoreCase("")) {
                    //Remove the frame instead of adding it
                    Config.frames.remove(loc);
                    event.getPlayer().sendMessage(Config.frame_removed);
                } else {
                    //Add this frame to the config
                    Config.frames.put(loc, ccFrameCommand.waitingCommand);
                    event.getPlayer().sendMessage(Config.frame_added.replaceAll("\\{command}",ccFrameCommand.waitingCommand));
                }
                //Save the change, cancel the event and return
                Config.save();
                ccFrameCommand.waiting = false;
                event.setCancelled(true);
                return;
            }
        }

        if (Config.frames.containsKey(loc)) {

            if (event.getPlayer().hasPermission("ccframeclick.click")) {
                //Execute command for this frame if user has click permission
                event.getPlayer().performCommand(Config.frames.get(loc));
            }

            //Cancel the event so the frame doesn't rotate
            event.setCancelled(true);
        }
    }

    //prevent damage to clickable frames
    @EventHandler
    public void onFrameBreak(EntityDamageEvent e) {
        if (e.getEntity() instanceof ItemFrame) {
            if (Config.frames.containsKey(e.getEntity().getLocation())) {
                e.setCancelled(true);
            }
        }
    }
}
