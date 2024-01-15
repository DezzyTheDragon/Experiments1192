package io.github.dezzythedragon.experiments1192.common.blocks;

import io.github.dezzythedragon.experiments1192.screen.ComplexAssemblerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ComplexAssemblerTileEntity extends BlockEntity implements MenuProvider {
    private ItemStackHandler itemHandler = new ItemStackHandler(17){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    public LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private static int OUTPUT_SLOT = 16;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;

    public ComplexAssemblerTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityRegistry.COMPLEX_ASSEMBLER.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0 -> ComplexAssemblerTileEntity.this.progress;
                    case 1 -> ComplexAssemblerTileEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0 -> ComplexAssemblerTileEntity.this.progress = value;
                    case 1 -> ComplexAssemblerTileEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER)
        {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("assembler.progress", this.progress);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("assembler.progress");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("experiments1192.tile_entity.complex_assembler");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ComplexAssemblerMenu(id, inventory, this, this.data);
    }

    public void drops()
    {
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++)
        {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, ComplexAssemblerTileEntity entity){
        //TODO: Assembler logic
    }
}
