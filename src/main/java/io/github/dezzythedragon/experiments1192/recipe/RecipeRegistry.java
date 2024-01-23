package io.github.dezzythedragon.experiments1192.recipe;

import io.github.dezzythedragon.experiments1192.Experiments1192;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Experiments1192.MODID);

    public static final RegistryObject<RecipeSerializer<ElectrumRefineryRecipies>> ELECTRUM_REFINERY_RECIPE = RECIPE.register("electrum_refining",
            () -> ElectrumRefineryRecipies.ElectrumRefinery_Serializer.INSTANCE);
}
