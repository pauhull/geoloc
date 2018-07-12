package de.pauhull.geoloc;

import com.maxmind.geoip2.DatabaseReader;
import de.pauhull.geoloc.config.Configuration;
import de.pauhull.geoloc.listener.PlayerJoinListener;
import de.pauhull.geoloc.listener.PlayerLoginListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public class GeoLoc {

    @Getter
    private static GeoLoc instance;

    @Getter
    private JavaPlugin plugin;

    @Getter
    private ScheduledExecutorService scheduledExecutorService;

    @Getter
    private ExecutorService executorService;

    @Getter
    private Configuration configuration;

    @Getter
    private boolean onlyFirstJoin;

    @Getter
    private String message;

    @Getter
    private String couldntResolve;

    @Getter
    private File databaseFile;

    @Getter
    private DatabaseReader databaseReader;

    @Getter
    private Map<UUID, InetAddress> IPs;

    public GeoLoc(JavaPlugin plugin, ScheduledExecutorService scheduledExecutorService, ExecutorService executorService, Configuration configuration) {
        instance = this;

        this.plugin = plugin;
        this.scheduledExecutorService = scheduledExecutorService;
        this.executorService = executorService;
        this.configuration = configuration;
    }

    public void start() throws IOException {
        onlyFirstJoin = configuration.getConfig().getBoolean("OnlyFirstJoin");
        message = configuration.getConfig().getString("Message");
        couldntResolve = configuration.getConfig().getString("CouldNotResolveLocation");
        databaseFile = new File("plugins/GeoLoc/database/GeoLite2-City.mmdb");
        databaseReader = new DatabaseReader.Builder(databaseFile).build();
        IPs = new HashMap<>();

        new PlayerJoinListener(this);
        new PlayerLoginListener(this);
    }

    public void stop() {

    }

}
