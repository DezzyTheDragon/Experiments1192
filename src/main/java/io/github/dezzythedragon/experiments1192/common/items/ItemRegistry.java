package io.github.dezzythedragon.experiments1192.common.items;

import io.github.dezzythedragon.experiments1192.Experiments1192;
import io.github.dezzythedragon.experiments1192.common.blocks.BlockRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Experiments1192.MODID);

    public static final CreativeModeTab MOD_TAB = new CreativeModeTab("mod_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RAW_ELECTRUM.get());
        }
    };

    //Block items
    //public static RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new BlockItem(BlockRegistry.EXAMPLE_BLOCK.get(), new Item.Properties()));
    public static RegistryObject<Item> ELECTRUM_ORE = ITEMS.register("electrum_ore", () -> new BlockItem(BlockRegistry.ELECTRUM_ORE.get(), new Item.Properties().tab(MOD_TAB)));
    public static RegistryObject<Item> COMPLEX_ASSEMBLER = ITEMS.register("complex_assembler", () -> new BlockItem(BlockRegistry.COMPLEX_ASSEMBLER.get(), new Item.Properties().tab(MOD_TAB)));
    public static RegistryObject<Item> ANALYZER = ITEMS.register("analyzer", () -> new BlockItem(BlockRegistry.ANALYZER.get(), new Item.Properties().tab(MOD_TAB)));
    public static RegistryObject<Item> ELECTRUM_REFINERY = ITEMS.register("electrum_refinery", () -> new BlockItem(BlockRegistry.ELECTRUM_REFINERY.get(), new Item.Properties().tab(MOD_TAB)));
    //Items
    public static RegistryObject<Item> RAW_ELECTRUM = ITEMS.register("raw_electrum", () -> new Item(new Item.Properties().tab(MOD_TAB)));
    public static RegistryObject<Item> ELECTRUM_INGOT = ITEMS.register("electrum_ingot", () -> new Item(new Item.Properties().tab(MOD_TAB)));
    //TODO: Blueprints allow the player to learn advanced mod recipes. Blank allow the player to reverse engineer one item.
    public static RegistryObject<Item> BLANK_BLUEPRINT = ITEMS.register("blank_blueprint", () -> new Item(new Item.Properties().tab(MOD_TAB)));

}
