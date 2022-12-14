package net.azisaba.minecraftlogappender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.OnStartupTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("unused")
public class MinecraftLogAppender extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        String filePattern = Variable.replaceAll(getConfig().getString("file-pattern", "logs2/%d{yyyy-MM-dd}-%i.log.gz"));
        String fileName = Variable.replaceAll(getConfig().getString("file-name", "logs2/latest.log"));
        appendAppender(Objects.requireNonNull(createAppender(filePattern, fileName), "appender"));
    }

    public static @Nullable Appender createAppender(@NotNull String filePattern, @NotNull String fileName) {
        PatternLayout layout =
                PatternLayout.newBuilder()
                        .withPattern("[%d{HH:mm:ss}] [%t/%level]: [%logger] %msg%n%xEx{full}")
                        .build();
        return RollingRandomAccessFileAppender
                .newBuilder()
                .withFilePattern(filePattern)
                .withFileName(fileName)
                .setName("MinecraftLogAppender")
                .setLayout(layout)
                .withPolicy(
                        CompositeTriggeringPolicy.createPolicy(
                                TimeBasedTriggeringPolicy.newBuilder().withMaxRandomDelay(1000).build(),
                                OnStartupTriggeringPolicy.createPolicy(1)
                        )
                )
                .build();
    }

    public static void appendAppender(@NotNull Appender appender) {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        if (!appender.isStarted()) {
            appender.start();
        }
        config.addAppender(appender);
        for (LoggerConfig loggerConfig : config.getLoggers().values()) {
            loggerConfig.addAppender(appender, null, null);
        }
        config.getRootLogger().addAppender(appender, null, null);
    }
}
