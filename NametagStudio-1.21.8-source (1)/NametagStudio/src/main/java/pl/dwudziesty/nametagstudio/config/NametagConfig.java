package pl.dwudziesty.nametagstudio.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/** User-facing settings. Values are clamped by the GUI before being changed. */
public final class NametagConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("nametagstudio.json");
    public static Settings settings = new Settings();

    private NametagConfig() {}

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                try (Reader reader = Files.newBufferedReader(FILE)) {
                    Settings loaded = GSON.fromJson(reader, Settings.class);
                    if (loaded != null) settings = loaded;
                }
            }
        } catch (Exception e) {
            System.err.println("[Nametag Studio] Could not load config: " + e.getMessage());
        }
        normalize();
    }

    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            try (Writer writer = Files.newBufferedWriter(FILE)) { GSON.toJson(settings, writer); }
        } catch (Exception e) {
            System.err.println("[Nametag Studio] Could not save config: " + e.getMessage());
        }
    }

    private static void normalize() {
        settings.textScale = clamp(settings.textScale, 0.5f, 3.0f);
        settings.widthScale = clamp(settings.widthScale, 0.5f, 2.5f);
        settings.heightScale = clamp(settings.heightScale, 0.5f, 2.5f);
        settings.verticalOffset = clamp(settings.verticalOffset, -1.0f, 2.0f);
        settings.textColor = settings.textColor == null ? "#FFFFFF" : settings.textColor;
        settings.backgroundColor = settings.backgroundColor == null ? "#20242D" : settings.backgroundColor;
        settings.language = "en".equals(settings.language) ? "en" : "pl";
        settings.theme = settings.theme == null ? "dark" : settings.theme;
    }
    private static float clamp(float v, float min, float max) { return Math.max(min, Math.min(max, v)); }

    public static int parseColor(String hex, int fallback) {
        try {
            String s = hex.startsWith("#") ? hex.substring(1) : hex;
            long parsed = Long.parseLong(s, 16);
            if (s.length() == 6) return 0xFF000000 | (int) parsed;
            if (s.length() == 8) return (int) parsed;
        } catch (Exception ignored) { }
        return fallback;
    }

    public static final class Settings {
        public boolean enabled = true;
        public boolean showText = true;
        public boolean showBackground = true;
        public boolean shadow = true;
        public float textScale = 1.0f;
        public float widthScale = 1.0f;
        public float heightScale = 1.0f;
        public float verticalOffset = 0.0f;
        public String textColor = "#FFFFFF";
        public String backgroundColor = "#20242D";
        /** Alpha in the range 0..255. */
        public int backgroundOpacity = 150;
        public String language = "pl";
        public String theme = "dark";
        public boolean roundedUi = true;
    }
}
