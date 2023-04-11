package net.azisaba.minecraftlogappender;

import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

@Plugin(id = "minecraft-log-appender", name = "MinecraftLogAppender")
public class VelocityPlugin {
    @Inject
    public VelocityPlugin(@DataDirectory Path dataDirectory) throws IOException {
        String filePattern = Variable.replaceAll(getString(dataDirectory.resolve("config.yml"), "logs2/%d{yyyy-MM-dd}-%i.log.gz", "file-pattern"));
        String fileName = Variable.replaceAll(getString(dataDirectory.resolve("config.yml"), "logs2/latest.log", "file-name"));
        Log4jAppender.appendAppender(Objects.requireNonNull(Log4jAppender.createAppender(filePattern, fileName), "appender"));
    }

    public String getString(@NotNull Path path, String def, @NotNull String... name) throws IOException {
        return YAMLConfigurationLoader.builder().setPath(path).build().load().getNode((Object[]) name).getString(def);
    }
}
