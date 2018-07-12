package de.pauhull.geoloc.listener;

import de.pauhull.geoloc.GeoLoc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.net.InetAddress;

public class PlayerLoginListener implements Listener {

    private GeoLoc geoLoc;

    public PlayerLoginListener(GeoLoc geoLoc) {
        this.geoLoc = geoLoc;

        Bukkit.getServer().getPluginManager().registerEvents(this, geoLoc.getPlugin());
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        InetAddress address = event.getAddress();
        geoLoc.getIPs().put(player.getUniqueId(), address);
    }

}
