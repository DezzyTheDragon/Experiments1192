package io.github.dezzythedragon.experiments1192.common.blocks;

import io.github.dezzythedragon.experiments1192.Experiments1192;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TileEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Experiments1192.MODID);

    public static final RegistryObject<BlockEntityType<ElectrumRefineryTileEntity>> ELECTRUM_REFINERY =
            TILE_ENTITIES.register("electrum_refinery",
                    () -> BlockEntityType.Builder.of(ElectrumRefineryTileEntity::new, BlockRegistry.ELECTRUM_REFINERY.get()).build(null));
    public static final RegistryObject<BlockEntityType<ComplexAssemblerTileEntity>> COMPLEX_ASSEMBLER =
            TILE_ENTITIES.register("complex_assembler",
                    () -> BlockEntityType.Builder.of(ComplexAssemblerTileEntity::new, BlockRegistry.COMPLEX_ASSEMBLER.get()).build(null));
}
