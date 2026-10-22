package pl.dwudziesty.nametagstudio;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import pl.dwudziesty.nametagstudio.config.NametagConfig;
import pl.dwudziesty.nametagstudio.gui.NametagStudioScreen;

public final class NametagStudioClient implements ClientModInitializer {
    private static KeyBinding openMenu;

    @Override
    public void onInitializeClient() {
        NametagConfig.load();
        openMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nametagstudio.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT,
                "key.categories.misc"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenu.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new NametagStudioScreen());
            }
        });
    }
}
