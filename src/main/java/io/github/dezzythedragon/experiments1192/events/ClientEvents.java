package io.github.dezzythedragon.experiments1192.events;

import io.github.dezzythedragon.experiments1192.Experiments1192;
import io.github.dezzythedragon.experiments1192.screen.StoryTabletScreen;
import io.github.dezzythedragon.experiments1192.util.KeyBinds;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = Experiments1192.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents{
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event){
            if(KeyBinds.BESTIARY_KEY.consumeClick()){
                //Minecraft.getInstance().player.sendSystemMessage(Component.literal("RAWR!"));
                Minecraft.getInstance().setScreen(new StoryTabletScreen(Component.literal("Literal Text")));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = Experiments1192.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents{
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event){
            event.register(KeyBinds.BESTIARY_KEY);
        }
    }
}
