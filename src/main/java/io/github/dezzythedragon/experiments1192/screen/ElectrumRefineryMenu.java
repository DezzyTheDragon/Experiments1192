package io.github.dezzythedragon.experiments1192.screen;

import io.github.dezzythedragon.experiments1192.common.blocks.BlockRegistry;
import io.github.dezzythedragon.experiments1192.common.blocks.ElectrumRefineryTileEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class ElectrumRefineryMenu extends AbstractContainerMenu {
    public final ElectrumRefineryTileEntity tileEntity;
    public final Level level;
    public final ContainerData data;

    public ElectrumRefineryMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public ElectrumRefineryMenu(int id, Inventory inv, BlockEntity entity, ContainerData data)
    {
        super(MenuRegistry.ELECTRUM_REFINERY.get(), id);
        checkContainerSize(inv, 3);
        tileEntity = (ElectrumRefineryTileEntity)entity;
        this.level = inv.player.level;
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
            this.addSlot(new SlotItemHandler(itemHandler, 0, 56, 53));  //Fuel Slot?
            this.addSlot(new SlotItemHandler(itemHandler, 1, 56, 17));  //Ingredient Slot?
            this.addSlot(new SlotItemHandler(itemHandler, 2, 116, 35)); //Output Slot
        });

        addDataSlots(data);
    }

    public boolean isCrafting(){
        return data.get(0) > 0;
    }

    public int getScaledProgress(){
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 26; //height of arrow in pixels

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getFuelAmount(){
        int fuelLevel = this.data.get(2);
        int fuelMax = this.data.get(3);
        int fuelLevelSize = 18;
        return fuelMax != 0 && fuelLevel != 0 ? fuelLevel * fuelLevelSize / fuelMax : 0;
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 3;

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        //Credit for this section goes to diesieben07 | https://github.com/diesieben07/SevenCommons
        Slot sourceSlot = slots.get(index);
        if(sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfStack = sourceStack.copy();

        if(index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT){
            if(!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)){
                return ItemStack.EMPTY;
            }
        } else if(index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT){
            if(!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)){
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slot index: " + index);
            return ItemStack.EMPTY;
        }

        if(sourceStack.getCount() == 0){
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfStack;
    }

    @Override
    public boolean stillValid(Player player) {
        //Checks to make sure we are still able to access the block at the given block position
        return stillValid(ContainerLevelAccess.create(level, tileEntity.getBlockPos()), player, BlockRegistry.ELECTRUM_REFINERY.get());
    }

    private void addPlayerInventory(Inventory inventory)
    {
        for(int row = 0; row < 3; ++row){
            for(int col = 0; col < 9; ++col){
                this.addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory inventory){
        for (int i = 0; i < 9; ++i){
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }
}
