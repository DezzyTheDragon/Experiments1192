package io.github.dezzythedragon.experiments1192.common.blocks;

import io.github.dezzythedragon.experiments1192.Experiments1192;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Experiments1192.MODID);

    //Blocks
    //public static RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE)));
    public static RegistryObject<Block> ELECTRUM_ORE = BLOCKS.register("electrum_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1f).requiresCorrectToolForDrops()));

    //Tile Entities
    //TODO: Complex assembler allows for the crafting of certain modded items, requires that the player find the recipe first
    public static RegistryObject<Block> COMPLEX_ASSEMBLER = BLOCKS.register("complex_assembler",
            () -> new ComplexAssemblerBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL).strength(0.5f)));
    //TODO: Takes certain modded items and a blank blueprint and returns the blueprint for the given item
    public static RegistryObject<Block> ANALYZER = BLOCKS.register("analyzer", () -> new Block(BlockBehaviour.Properties.of(Material.HEAVY_METAL)));
    public static RegistryObject<Block> ELECTRUM_REFINERY = BLOCKS.register("electrum_refinery",
            () -> new ElectrumRefineryBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL).noOcclusion().strength(0.5f).requiresCorrectToolForDrops()));
}
