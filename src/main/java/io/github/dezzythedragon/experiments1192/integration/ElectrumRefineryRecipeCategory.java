package io.github.dezzythedragon.experiments1192.integration;

import io.github.dezzythedragon.experiments1192.Experiments1192;
import io.github.dezzythedragon.experiments1192.common.blocks.BlockRegistry;
import io.github.dezzythedragon.experiments1192.recipe.ElectrumRefineryRecipies;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ElectrumRefineryRecipeCategory implements IRecipeCategory<ElectrumRefineryRecipies> {
    public static final ResourceLocation UID = new ResourceLocation(Experiments1192.MODID, "electrum_refining");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Experiments1192.MODID, "textures/gui/electrum_refinery_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public ElectrumRefineryRecipeCategory(IGuiHelper helper){
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.ELECTRUM_REFINERY.get()));
    }

    @Override
    public RecipeType<ElectrumRefineryRecipies> getRecipeType() {
        return JEIExperiments1192Plugin.REFINERY_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.electrum_refinery");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ElectrumRefineryRecipies recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 86, 17).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 35).addItemStack(recipe.getResultItem());
    }
}
