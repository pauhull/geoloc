package de.pauhull.geoloc;

import de.pauhull.geoloc.config.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main extends JavaPlugin {

    public static final String PLUGIN_NAME = "GeoLoc";

    private int threadID;
    private GeoLoc geoLoc;
    private Configuration configuration;

    @Override
    public void onEnable() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(this::createThread);
        ExecutorService executorService = Executors.newCachedThreadPool(this::createThread);

        this.copyDatabaseFile();

        configuration = new Configuration(new File("plugins/GeoLoc/config.yml"));

        geoLoc = new GeoLoc(this, scheduledExecutorService, executorService, configuration);

        try {
            geoLoc.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        geoLoc.stop();
    }

    private Thread createThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(PLUGIN_NAME + " Task # " + (++threadID));
        return thread;
    }

    private void copyDatabaseFile() {
        File databaseFile = new File("plugins/GeoLoc/database/GeoLite2-City.mmdb");
        databaseFile.getParentFile().mkdirs();

        if (!databaseFile.exists()) {
            try {
                Files.copy(Configuration.class.getClassLoader().getResourceAsStream(databaseFile.getName()), databaseFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
