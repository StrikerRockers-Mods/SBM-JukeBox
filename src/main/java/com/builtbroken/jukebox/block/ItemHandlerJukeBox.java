package com.builtbroken.jukebox.block;

import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

/**
 * Wrapper for {@link BlockJukebox.TileEntityJukebox} to give access to record as an inventory
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by StrikerRocker on 4/7/2018.
 */
public class ItemHandlerJukeBox implements IItemHandlerModifiable
{
    private final BlockJukebox.TileEntityJukebox jukebox;

    public ItemHandlerJukeBox(BlockJukebox.TileEntityJukebox tileEntityJukebox)
    {
        this.jukebox = tileEntityJukebox;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack)
    {
        if (slot != 0)
        {
            throw new IndexOutOfBoundsException();
        }
        if (jukebox != null && jukebox.getRecord().isEmpty())
        {
            jukebox.setRecord(stack);
        }
    }

    @Override
    public int getSlots()
    {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if (slot != 0)
        {
            throw new IndexOutOfBoundsException();
        }
        return jukebox.getRecord();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        if (slot != 0)
        {
            throw new IndexOutOfBoundsException();
        }
        if (stack.getItem() instanceof ItemRecord && getStackInSlot(slot).isEmpty())
        {
            if (!simulate)
            {
                //Set record
                jukebox.setRecord(stack);

                //Set state
                setPlayState(jukebox, true);

                //Trigger record to play
                jukebox.getWorld().playEvent(null, 1010, jukebox.getPos(), Item.getIdFromItem(stack.getItem()));
            }
            return ItemStack.EMPTY;
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (slot != 0)
        {
            throw new IndexOutOfBoundsException();
        }
        ItemStack stack = jukebox.getRecord();
        if (!simulate)
        {
            //Set to empty
            jukebox.setRecord(ItemStack.EMPTY);

            //Set state
            setPlayState(jukebox, false);

            //Update audio
            jukebox.getWorld().playEvent(1010, jukebox.getPos(), 0);
            jukebox.getWorld().playRecord(jukebox.getPos(), null);
        }
        return stack;
    }

    protected void setPlayState(BlockJukebox.TileEntityJukebox jukebox, boolean b)
    {
        //Set state
        IBlockState state = jukebox.getWorld().getBlockState(jukebox.getPos());
        if (state.getBlock() instanceof BlockJukebox)
        {
            jukebox.getWorld().setBlockState(jukebox.getPos(), state.withProperty(BlockJukebox.HAS_RECORD, Boolean.valueOf(b)), 2);
        }
    }

    @Override
    public int getSlotLimit(int slot)
    {
        if (slot != 0)
        {
            throw new IndexOutOfBoundsException();
        }
        return 1;
    }
}