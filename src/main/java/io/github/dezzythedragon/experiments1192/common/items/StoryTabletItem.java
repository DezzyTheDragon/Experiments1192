package io.github.dezzythedragon.experiments1192.common.items;

import com.mojang.logging.LogUtils;
import io.github.dezzythedragon.experiments1192.Experiments1192;
import io.github.dezzythedragon.experiments1192.screen.StoryTabletMenu;
import io.github.dezzythedragon.experiments1192.screen.StoryTabletScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

//NOTE: This exists for possible reference, will be fully removed
@Deprecated
public class StoryTabletItem extends Item {
    public StoryTabletItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pLevel.isClientSide){
            LogUtils.getLogger().info("========RIGHT CLICKED========");
            Minecraft.getInstance().setScreen(new StoryTabletScreen(Component.literal("LiteralText")));
        }
        pPlayer.openItemGui(pPlayer.getItemInHand(pUsedHand), pUsedHand);
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
