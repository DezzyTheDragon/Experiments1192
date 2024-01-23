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

public class ComplexAssemblerRecipies implements Recipe<SimpleContainer> {

    private final ResourceLocation ID;
    private final ItemStack OUTPUT;
    private final NonNullList<Ingredient> INPUT;

    public ComplexAssemblerRecipies(ResourceLocation id, ItemStack output, NonNullList<Ingredient> ingredients){
        ID = id;
        OUTPUT = output;
        INPUT = ingredients;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        //TODO: Look at how the normal crafting table matches its recipe and replicate that
        return false;
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
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }

    public static class ComplexAssembler_Type implements RecipeType<ComplexAssemblerRecipies>{
        public ComplexAssembler_Type(){}
        public static final ComplexAssembler_Type INSTANCE = new ComplexAssembler_Type();
        public static final String ID = "complex_assembling";
    }

    public static class ComplexAssembler_Serializer implements RecipeSerializer<ComplexAssemblerRecipies>{
        public static final ComplexAssembler_Serializer INSTANCE = new ComplexAssembler_Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Experiments1192.MODID, "complex_assembling");

        @Override
        public ComplexAssemblerRecipies fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++){
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new ComplexAssemblerRecipies(pRecipeId, output, inputs);
        }


        @Override
        public @Nullable ComplexAssemblerRecipies fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++){
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack output = pBuffer.readItem();

            return new ComplexAssemblerRecipies(pRecipeId, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ComplexAssemblerRecipies pRecipe) {
            pBuffer.writeInt(pRecipe.getIngredients().size());

            for(Ingredient ingredient : pRecipe.getIngredients()){
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(), false);

        }
    }
}
