package net.azisaba.minecraftlogappender;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variable {
    // %{env:NAME}
    // %{env:NAME=DEFAULT}
    private static final Handler ENV = new Handler(Pattern.compile("%\\{env:(.+?)(?:=(.+?))?}"), m -> System.getenv().getOrDefault(m.group(1), m.group(2)));
    // %{property:NAME}
    // %{property:NAME=DEFAULT}
    private static final Handler PROPERTY = new Handler(Pattern.compile("%\\{property:(.+?)(?:=(.+?))?}"), m -> System.getProperty(m.group(1), m.group(2)));
    private static final List<Handler> HANDLERS = Arrays.asList(ENV, PROPERTY);

    public static @NotNull String replaceAll(@NotNull String original) {
        String replaced = original;
        for (Handler handler : HANDLERS) {
            StringBuffer sb = new StringBuffer();
            Matcher matcher = handler.pattern.matcher(replaced);
            while (matcher.find()) {
                matcher.appendReplacement(sb, handler.handler.apply(matcher));
            }
            matcher.appendTail(sb);
            replaced = sb.toString();
        }
        return replaced;
    }

    private static class Handler {
        private final Pattern pattern;
        private final Function<Matcher, String> handler;

        public Handler(@NotNull Pattern pattern, @NotNull Function<Matcher, String> handler) {
            this.pattern = pattern;
            this.handler = handler;
        }
    }
}
