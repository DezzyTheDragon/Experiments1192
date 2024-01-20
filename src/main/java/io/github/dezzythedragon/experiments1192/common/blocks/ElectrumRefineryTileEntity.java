package io.github.dezzythedragon.experiments1192.common.blocks;

import io.github.dezzythedragon.experiments1192.common.items.ItemRegistry;
import io.github.dezzythedragon.experiments1192.recipe.ElectrumRefineryRecipies;
import io.github.dezzythedragon.experiments1192.screen.ElectrumRefineryMenu;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ElectrumRefineryTileEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(3)
    {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private LazyOptional<IItemHandler> lazyOptionalItemHandler = LazyOptional.empty();

    //Inventory slot constants
    //NOTE: Turns out slots are 0 indexed
    private static final int FUEL_SLOT = 0;
    private static final int INGREDIENT_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    protected final ContainerData data;
    private int currentProgress = 0;
    private int maxProgress = 200;
    private int fuelLevel = 0;
    private int fuelMax = 1000;

    public ElectrumRefineryTileEntity(BlockPos pos, BlockState blockState) {
        super(TileEntityRegistry.ELECTRUM_REFINERY.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ElectrumRefineryTileEntity.this.currentProgress;
                    case 1 -> ElectrumRefineryTileEntity.this.maxProgress;
                    case 2 -> ElectrumRefineryTileEntity.this.fuelLevel;
                    case 3 -> ElectrumRefineryTileEntity.this.fuelMax;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index)
                {
                    case 0 -> ElectrumRefineryTileEntity.this.currentProgress = value;
                    case 1 -> ElectrumRefineryTileEntity.this.maxProgress = value;
                    case 2 -> ElectrumRefineryTileEntity.this.fuelLevel = value;
                    case 3 -> ElectrumRefineryTileEntity.this.fuelMax = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("experiments1192.tile_entity.electrum_refinery");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ElectrumRefineryMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER)
        {
            return lazyOptionalItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyOptionalItemHandler = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyOptionalItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemStackHandler.serializeNBT());
        nbt.putInt("refinery.progress", this.currentProgress);
        nbt.putInt("refinery.fuel", this.fuelLevel);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        currentProgress = nbt.getInt("refinery.progress");
        fuelLevel = nbt.getInt("refinery.fuel");
    }

    public void drops()
    {
        SimpleContainer inv = new SimpleContainer(itemStackHandler.getSlots());
        for(int i = 0; i < itemStackHandler.getSlots(); i++)
        {
            inv.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, ElectrumRefineryTileEntity entity) {
        //Tick logic
        if(level.isClientSide)
        {
            return;
        }

        if(hasRecipe(entity))
        {
            if(entity.fuelLevel <= 0){
                refuel(entity);
            }
            entity.fuelLevel--;
            entity.currentProgress++;
            setChanged(level, blockPos, blockState);
            if(entity.currentProgress >= entity.maxProgress)
            {
                craftItem(entity);
            }
        }else
        {
            entity.resetProgress();
            setChanged(level, blockPos, blockState);
        }

    }

    private static void refuel(ElectrumRefineryTileEntity entity) {
        if(entity.itemStackHandler.getStackInSlot(FUEL_SLOT).getItem() == Items.BLAZE_POWDER){
            entity.itemStackHandler.extractItem(FUEL_SLOT, 1, false);
            entity.fuelLevel = entity.fuelMax;
        }
    }

    private void resetProgress() {
        this.currentProgress = 0;
    }

    private static void craftItem(ElectrumRefineryTileEntity entity) {
        Level level = entity.getLevel();
        SimpleContainer inventory = new SimpleContainer(entity.itemStackHandler.getSlots());
        for(int i = 0; i < entity.itemStackHandler.getSlots(); i++)
        {
            inventory.setItem(i, entity.itemStackHandler.getStackInSlot(i));
        }

        Optional<ElectrumRefineryRecipies> recipe =
                level.getRecipeManager().getRecipeFor(ElectrumRefineryRecipies.Type.INSTANCE, inventory, level);

        if(hasRecipe(entity))
        {
            entity.itemStackHandler.extractItem(INGREDIENT_SLOT, 1, false);
            entity.itemStackHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(recipe.get().getResultItem().getItem(),
                    entity.itemStackHandler.getStackInSlot(OUTPUT_SLOT).getCount() +
                            recipe.get().getResultItem().getCount()));

            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(ElectrumRefineryTileEntity entity) {
        Level level = entity.getLevel();
        SimpleContainer inventory = new SimpleContainer(entity.itemStackHandler.getSlots());
        for(int i = 0; i < entity.itemStackHandler.getSlots(); i++)
        {
            inventory.setItem(i, entity.itemStackHandler.getStackInSlot(i));
        }

        Optional<ElectrumRefineryRecipies> recipe =
                level.getRecipeManager().getRecipeFor(ElectrumRefineryRecipies.Type.INSTANCE, inventory, level);

        return recipe.isPresent() && canInsertAmount(inventory)
                && canInsertItem(inventory, recipe.get().getResultItem())
                && hasValidPower(entity, inventory);
    }

    private static boolean hasValidPower(ElectrumRefineryTileEntity entity, SimpleContainer inventory){
        boolean validPower = false;
        if(entity.fuelLevel > 0 || inventory.getItem(FUEL_SLOT).getItem() == Items.BLAZE_POWDER){
            validPower = true;
        }
        return validPower;
    }

    private static boolean canInsertItem(SimpleContainer inventory, ItemStack itemStack) {
        return inventory.getItem(OUTPUT_SLOT).getItem() == itemStack.getItem() || inventory.getItem(OUTPUT_SLOT).isEmpty();
    }

    private static boolean canInsertAmount(SimpleContainer inventory) {
        return inventory.getItem(OUTPUT_SLOT).getMaxStackSize() > inventory.getItem(OUTPUT_SLOT).getCount();
    }
}
