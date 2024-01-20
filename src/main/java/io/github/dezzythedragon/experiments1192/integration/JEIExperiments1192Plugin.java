package io.github.dezzythedragon.experiments1192.integration;

import io.github.dezzythedragon.experiments1192.Experiments1192;
import io.github.dezzythedragon.experiments1192.recipe.ElectrumRefineryRecipies;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIExperiments1192Plugin implements IModPlugin {
    public static RecipeType<ElectrumRefineryRecipies> REFINERY_TYPE =
            new RecipeType<>(ElectrumRefineryRecipeCategory.UID, ElectrumRefineryRecipies.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Experiments1192.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ElectrumRefineryRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        //Register the recipe types
        List<ElectrumRefineryRecipies> refineryRecipesList = rm.getAllRecipesFor(ElectrumRefineryRecipies.Type.INSTANCE);
        registration.addRecipes(REFINERY_TYPE, refineryRecipesList);
        //Add additional recipe types
    }
}
