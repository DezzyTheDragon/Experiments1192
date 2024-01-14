package io.github.dezzythedragon.experiments1192.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.dezzythedragon.experiments1192.Experiments1192;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ElectrumRefineryScreen extends AbstractContainerScreen<ElectrumRefineryMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Experiments1192.MODID, "textures/gui/electrum_refinery_gui.png");

    public ElectrumRefineryScreen(ElectrumRefineryMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(stack, x, y, 0, 0, imageWidth, imageHeight);
        renderProgressArrow(stack, x, y);
    }

    private void renderProgressArrow(PoseStack stack, int x, int y) {
        if(menu.isCrafting()){
            blit(stack, x + 105, y + 33, 177, 14, menu.getScaledProgress(), 16);
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, delta);
        renderTooltip(stack, mouseX, mouseY);
    }
}
