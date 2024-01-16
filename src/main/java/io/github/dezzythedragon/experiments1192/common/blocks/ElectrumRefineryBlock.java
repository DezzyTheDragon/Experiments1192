package io.github.dezzythedragon.experiments1192.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

//public class ElectrumRefineryBlock extends HorizontalDirectionalBlock {
public class ElectrumRefineryBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 16, 16, 16);
    public ElectrumRefineryBlock(Properties properties) {
        super(properties);
    }

    //Changes the hitbox of the block
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        double back = 2;
        double left = 4;
        double right = 4;
        double front = 6;
        VoxelShape shape = null;
        //This block is asymmetric so each direction needs to be handled
        switch (blockState.getValue(FACING)) {
            case NORTH ->
                shape = Block.box(right, 0, front, 16 - left, 16, 16 - back);
            case SOUTH ->
                shape = Block.box(left, 0, back, 16 - right, 16, 16 - front);
            case EAST ->
                shape = Block.box(back, 0, right, 16 - front, 16, 16 - left);
            case WEST ->
                shape = Block.box(front, 0, left, 16 - back, 16, 16 - right);
            default ->
                shape = Block.box(0, 0, 0, 16, 16, 16);
        }
        return shape;
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random) {
        //super.animateTick(blockState, level, blockPos, random);
        double xOffset = 0;
        double yOffset = 0.2D;
        double zOffset = 0;
        switch (blockState.getValue(FACING)) {
            case NORTH:
                xOffset = 0.5D;
                zOffset = 0.625D;
                break;
            case SOUTH:
                xOffset = 0.5D;
                zOffset = 0.375D;
                break;
            case EAST:
                xOffset = 0.375D;
                zOffset = 0.5D;
                break;
            case WEST:
                xOffset = 0.625D;
                zOffset = 0.5D;
                break;
            default:
                xOffset = 0.5D;
                zOffset = 0.5D;
        }
            level.addParticle(ParticleTypes.FLAME, blockPos.getX() + xOffset, blockPos.getY() + yOffset, blockPos.getZ() + zOffset, 0.0D, 0.0D, 0.0D);

    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext)
    {
        //Get the direction the player is facing and give the opposite so the model faces the player when placed
        return this.defaultBlockState().setValue(FACING,pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation)
    {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror)
    {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    //Tile Entity methods


    //Allows the tile entity to be visible
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(oldState.getBlock() != newState.getBlock())
        {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof ElectrumRefineryTileEntity)
            {
                ((ElectrumRefineryTileEntity) blockEntity).drops();
            }
        }
        super.onRemove(oldState, level, pos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!level.isClientSide)
        {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof ElectrumRefineryTileEntity)
            {
                NetworkHooks.openScreen(((ServerPlayer)player), (ElectrumRefineryTileEntity)blockEntity, pos);
            }else{
                throw new IllegalStateException("Our container provider is missing!");
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ElectrumRefineryTileEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, TileEntityRegistry.ELECTRUM_REFINERY.get(), ElectrumRefineryTileEntity::tick);
    }
}
