package de.pauhull.geoloc.listener;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import de.pauhull.geoloc.GeoLoc;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.net.InetAddress;

public class PlayerJoinListener implements Listener {

    private GeoLoc geoLoc;

    public PlayerJoinListener(GeoLoc geoLoc) {
        this.geoLoc = geoLoc;

        Bukkit.getPluginManager().registerEvents(this, geoLoc.getPlugin());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();
        InetAddress address = geoLoc.getIPs().get(player.getUniqueId());
        String playerName, displayName, ip, city, region, regionCode, country, countryCode, continent, continentCode;

        if (!player.hasPlayedBefore() || !geoLoc.isOnlyFirstJoin()) {

            CityResponse response;
            try {
                response = geoLoc.getDatabaseReader().city(address);
            } catch (GeoIp2Exception e) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', geoLoc.getCouldntResolve())
                        .replaceAll("%USER%", player.getName())
                        .replaceAll("%DISPLAY%", player.getDisplayName()));
                return;
            }

            playerName = player.getName();
            displayName = player.getDisplayName();
            ip = address.getHostAddress();
            city = response.getCity().getName();
            region = response.getMostSpecificSubdivision().getName();
            regionCode = response.getMostSpecificSubdivision().getIsoCode();
            country = response.getCountry().getName();
            countryCode = response.getCountry().getIsoCode();
            continent = response.getContinent().getName();
            continentCode = response.getContinent().getCode();

            String message = ChatColor.translateAlternateColorCodes('&', geoLoc.getMessage())
                    .replaceAll("%USER%", playerName)
                    .replaceAll("%DISPLAY%", displayName)
                    .replaceAll("%CITY%", city)
                    .replaceAll("%REGION%", region)
                    .replaceAll("%REGIONCODE%", regionCode)
                    .replaceAll("%COUNTRY%", country)
                    .replaceAll("%COUNTRYCODE%", countryCode)
                    .replaceAll("%CONTINENT%", continent)
                    .replaceAll("%CONTINENTCODE%", continentCode)
                    .replaceAll("%IP%", ip);

            Bukkit.broadcastMessage(message);
        }
    }

}
