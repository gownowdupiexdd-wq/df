package pl.dwudziesty.nametagstudio.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import pl.dwudziesty.nametagstudio.config.NametagConfig;

/** A compact, two-column settings studio with live nametag preview. */
public final class NametagStudioScreen extends Screen {
    private int panelX, panelY, panelW, panelH;
    private TextFieldWidget textColorField, backgroundColorField;
    private final NametagConfig.Settings s = NametagConfig.settings;

    public NametagStudioScreen() { super(Text.literal("Nametag Studio")); }

    private String tr(String pl, String en) { return "en".equals(s.language) ? en : pl; }

    @Override public void init() {
        panelW = Math.min(620, width - 36);
        panelH = Math.min(390, height - 36);
        panelX = (width - panelW) / 2;
        panelY = (height - panelH) / 2;
        int leftX = panelX + 18, leftW = (panelW - 54) / 2;
        int rightX = panelX + panelW / 2 + 8, rightW = panelW / 2 - 26;
        int y = panelY + 92;

        addDrawableChild(ButtonWidget.builder(label(tr("Widoczność: ", "Nametags: ") + onOff(s.enabled)), b -> {
            s.enabled = !s.enabled; b.setMessage(Text.literal(label(tr("Widoczność: ", "Nametags: ") + onOff(s.enabled))));
            NametagConfig.save();
        }).dimensions(leftX, y, leftW, 20).build());
        y += 27;
        addDrawableChild(new ValueSlider(leftX, y, leftW, tr("Rozmiar tekstu", "Text size"), s.textScale, 0.5, 3.0, v -> s.textScale = (float)v));
        y += 27;
        addDrawableChild(new ValueSlider(leftX, y, leftW, tr("Rozciągnięcie poziome", "Horizontal stretch"), s.widthScale, 0.5, 2.5, v -> s.widthScale = (float)v));
        y += 27;
        addDrawableChild(new ValueSlider(leftX, y, leftW, tr("Rozciągnięcie pionowe", "Vertical stretch"), s.heightScale, 0.5, 2.5, v -> s.heightScale = (float)v));
        y += 27;
        addDrawableChild(new ValueSlider(leftX, y, leftW, tr("Pozycja góra/dół", "Vertical offset"), s.verticalOffset, -1, 2, v -> s.verticalOffset = (float)v));
        y += 27;
        addDrawableChild(ButtonWidget.builder(label(tr("Cień: ", "Text shadow: ") + onOff(s.shadow)), b -> {
            s.shadow = !s.shadow; b.setMessage(Text.literal(label(tr("Cień: ", "Text shadow: ") + onOff(s.shadow)))); NametagConfig.save();
        }).dimensions(leftX, y, leftW, 20).build());

        int ry = panelY + 92;
        addDrawableChild(ButtonWidget.builder(label(tr("Tło nametaga: ", "Nametag background: ") + onOff(s.showBackground)), b -> {
            s.showBackground = !s.showBackground; b.setMessage(Text.literal(label(tr("Tło nametaga: ", "Nametag background: ") + onOff(s.showBackground)))); NametagConfig.save();
        }).dimensions(rightX, ry, rightW, 20).build());
        ry += 27;
        addDrawableChild(new ValueSlider(rightX, ry, rightW, tr("Przezroczystość tła", "Background opacity"), s.backgroundOpacity, 0, 255, v -> s.backgroundOpacity = (int)v));
        ry += 33;
        textColorField = new TextFieldWidget(textRenderer, rightX, ry + 13, rightW, 20, Text.literal("#FFFFFF"));
        textColorField.setMaxLength(9); textColorField.setText(s.textColor);
        textColorField.setChangedListener(v -> { s.textColor = v; NametagConfig.save(); }); addDrawableChild(textColorField);
        ry += 45;
        backgroundColorField = new TextFieldWidget(textRenderer, rightX, ry + 13, rightW, 20, Text.literal("#20242D"));
        backgroundColorField.setMaxLength(9); backgroundColorField.setText(s.backgroundColor);
        backgroundColorField.setChangedListener(v -> { s.backgroundColor = v; NametagConfig.save(); }); addDrawableChild(backgroundColorField);
        ry += 46;
        addDrawableChild(ButtonWidget.builder(label(tr("Motyw: ", "UI theme: ") + themeName()), b -> {
            s.theme = "dark".equals(s.theme) ? "light" : "dark"; b.setMessage(Text.literal(label(tr("Motyw: ", "UI theme: ") + themeName()))); NametagConfig.save();
        }).dimensions(rightX, ry, rightW, 20).build());
        ry += 27;
        addDrawableChild(ButtonWidget.builder(label(tr("Język: ", "Language: ") + ("pl".equals(s.language) ? "Polski" : "English")), b -> {
            s.language = "pl".equals(s.language) ? "en" : "pl"; NametagConfig.save(); clearAndInit();
        }).dimensions(rightX, ry, rightW, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal(tr("Zapisz i zamknij", "Save & close")), b -> { NametagConfig.save(); close(); })
                .dimensions(panelX + panelW - 146, panelY + panelH - 32, 128, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal(tr("Reset", "Reset")), b -> {
            NametagConfig.settings = new NametagConfig.Settings(); NametagConfig.save(); clearAndInit();
        }).dimensions(panelX + 18, panelY + panelH - 32, 80, 20).build());
    }

    private String label(String s) { return s; }
    private String onOff(boolean value) { return value ? tr("WŁ. / ON", "ON") : tr("WYŁ. / OFF", "OFF"); }
    private String themeName() { return "dark".equals(s.theme) ? tr("Ciemny", "Dark") : tr("Jasny", "Light"); }

    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        int bg = "light".equals(s.theme) ? 0xF2EDE7 : 0xF0181C25;
        int panel = "light".equals(s.theme) ? 0xFFF8F5F0 : 0xFF222833;
        int line = "light".equals(s.theme) ? 0xFFD6CEC4 : 0xFF3A4352;
        int ink = "light".equals(s.theme) ? 0xFF242832 : 0xFFF2F5FA;
        context.fill(0, 0, width, height, bg);
        context.fill(panelX, panelY, panelX + panelW, panelY + panelH, panel);
        context.fill(panelX, panelY, panelX + panelW, panelY + 3, 0xFF7C6CFF);
        context.drawBorder(panelX, panelY, panelW, panelH, line);
        context.drawText(textRenderer, Text.literal("NAMETAG STUDIO"), panelX + 18, panelY + 16, ink, false);
        context.drawText(textRenderer, Text.literal(tr("Personalizacja nicków • Fabric 1.21.8", "Player nametag customization • Fabric 1.21.8")), panelX + 18, panelY + 32, line, false);
        context.drawText(textRenderer, Text.literal(tr("USTAWIENIA TEKSTU", "TEXT SETTINGS")), panelX + 18, panelY + 68, ink, false);
        context.drawText(textRenderer, Text.literal(tr("TŁO I WYGLĄD", "BACKGROUND & STYLE")), panelX + panelW / 2 + 8, panelY + 68, ink, false);
        // Preview card
        int px = panelX + panelW / 2 - 62, py = panelY + 44;
        int previewBg = s.showBackground ? ((s.backgroundOpacity & 255) << 24) | (NametagConfig.parseColor(s.backgroundColor, 0x0020242D) & 0x00FFFFFF) : 0x00000000;
        if (s.showBackground) context.fill(px, py, px + 124, py + 23, previewBg);
        String preview = "dwudziesty";
        int color = NametagConfig.parseColor(s.textColor, 0xFFFFFFFF);
        if (s.showText) context.drawText(textRenderer, Text.literal(preview), px + 62 - textRenderer.getWidth(preview) / 2, py + 7, color, s.shadow);
        // Tiny creator avatar tile; actual player skin remains supplied by Minecraft.
        int hx = panelX + panelW - 42, hy = panelY + 14;
        context.fill(hx, hy, hx + 24, hy + 24, 0xFF394355);
        context.fill(hx + 5, hy + 4, hx + 19, hy + 13, 0xFFE5B18B);
        context.fill(hx + 4, hy + 3, hx + 20, hy + 8, 0xFF47352F);
        context.fill(hx + 4, hy + 14, hx + 20, hy + 22, 0xFF4B79A8);
        context.drawText(textRenderer, Text.literal("by dwudziesty"), panelX + panelW - 120, panelY + 43, ink, false);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override public boolean shouldPause() { return false; }
    @Override public void close() { NametagConfig.save(); if (client != null) client.setScreen(null); }

    private final class ValueSlider extends SliderWidget {
        private final String title; private final double min, max; private final java.util.function.DoubleConsumer setter;
        ValueSlider(int x, int y, int w, String title, double value, double min, double max, java.util.function.DoubleConsumer setter) {
            super(x, y, w, 20, Text.empty(), (value - min) / (max - min)); this.title = title; this.min = min; this.max = max; this.setter = setter; updateMessage();
        }
        @Override protected void updateMessage() { setMessage(Text.literal(title + ": " + String.format(java.util.Locale.ROOT, "%.2f", min + value * (max - min)))); }
        @Override protected void applyValue() { setter.accept(min + value * (max - min)); NametagConfig.save(); }
    }
}
