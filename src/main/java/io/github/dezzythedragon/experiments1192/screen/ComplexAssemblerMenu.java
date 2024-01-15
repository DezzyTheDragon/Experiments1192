package io.github.dezzythedragon.experiments1192.screen;

import io.github.dezzythedragon.experiments1192.common.blocks.BlockRegistry;
import io.github.dezzythedragon.experiments1192.common.blocks.ComplexAssemblerTileEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class ComplexAssemblerMenu extends AbstractContainerMenu {
    public final ComplexAssemblerTileEntity blockEntity;
    public final Level level;
    public final ContainerData data;


    public ComplexAssemblerMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, inventory.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public ComplexAssemblerMenu(int id, Inventory inventory, BlockEntity entity, ContainerData data){
        super(MenuRegistry.COMPLEX_ASSEMBLER.get(), id);
        checkContainerSize(inventory, 17);
        blockEntity = (ComplexAssemblerTileEntity)entity;
        this.level = inventory.player.level;
        this.data = data;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);


        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
            int index = 0;
            for(int row = 0; row < 4; ++row){
                for(int col = 0; col < 4; ++col){
                    this.addSlot(new SlotItemHandler(itemHandler, index, col * 18, row * 18));
                    index++;
                }
            }
            this.addSlot(new SlotItemHandler(itemHandler, 16, 100, 100));
        });


        addDataSlots(data);

    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 0;

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
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, BlockRegistry.COMPLEX_ASSEMBLER.get());
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
