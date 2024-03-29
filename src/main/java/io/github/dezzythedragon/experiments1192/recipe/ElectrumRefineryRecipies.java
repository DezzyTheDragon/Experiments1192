package io.github.dezzythedragon.experiments1192.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.dezzythedragon.experiments1192.Experiments1192;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ElectrumRefineryRecipies implements Recipe<SimpleContainer> {

    private final ResourceLocation ID;
    private final ItemStack OUTPUT;
    private final NonNullList<Ingredient> RECIPE_ITEMS;

    public ElectrumRefineryRecipies(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems){
        this.ID = id;
        this.OUTPUT = output;
        this.RECIPE_ITEMS = recipeItems;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()){
            return false;
        }

        return RECIPE_ITEMS.get(0).test(pContainer.getItem(1));
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return OUTPUT;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return OUTPUT.copy();
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ElectrumRefinery_Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return ElectrumRefinery_Type.INSTANCE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return RECIPE_ITEMS;
    }

    public static class ElectrumRefinery_Type implements RecipeType<ElectrumRefineryRecipies>{
        public ElectrumRefinery_Type() {}
        public static final ElectrumRefinery_Type INSTANCE = new ElectrumRefinery_Type();
        public static final String ID = "electrum_refining";
    }

    public static class ElectrumRefinery_Serializer implements RecipeSerializer<ElectrumRefineryRecipies>{
        public static final ElectrumRefinery_Serializer INSTANCE = new ElectrumRefinery_Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Experiments1192.MODID, "electrum_refining");

        @Override
        public ElectrumRefineryRecipies fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++){
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new ElectrumRefineryRecipies(pRecipeId, output, inputs);
        }

        //Take buffered data from network and read it out into usable information
        @Override
        public @Nullable ElectrumRefineryRecipies fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++){
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack output = pBuffer.readItem();

            return new ElectrumRefineryRecipies(pRecipeId, output, inputs);
        }

        //Take the data and write it into a buffer so it can be sent through the network
        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ElectrumRefineryRecipies pRecipe) {
            pBuffer.writeInt(pRecipe.getIngredients().size());

            for(Ingredient ing : pRecipe.getIngredients()){
                ing.toNetwork(pBuffer);
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(), false);
        }
    }
}
