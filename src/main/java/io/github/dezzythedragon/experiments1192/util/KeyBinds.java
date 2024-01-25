package io.github.dezzythedragon.experiments1192.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {
    public static final String EXPERIMENTS1192_CATEGORY_KEY = "key.category.experiments1192";
    public static final String KEY_BESTIARY = "key.experiments1192.bestiary";

    public static final KeyMapping BESTIARY_KEY = new KeyMapping(KEY_BESTIARY, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O, EXPERIMENTS1192_CATEGORY_KEY);
}
