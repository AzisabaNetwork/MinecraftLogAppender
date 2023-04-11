package net.azisaba.minecraftlogappender;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@SuppressWarnings("unused")
public class BukkitPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        String filePattern = Variable.replaceAll(getConfig().getString("file-pattern", "logs2/%d{yyyy-MM-dd}-%i.log.gz"));
        String fileName = Variable.replaceAll(getConfig().getString("file-name", "logs2/latest.log"));
        Log4jAppender.appendAppender(Objects.requireNonNull(Log4jAppender.createAppender(filePattern, fileName), "appender"));
    }
}
