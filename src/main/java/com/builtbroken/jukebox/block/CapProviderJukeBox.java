package com.builtbroken.jukebox.block;

import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * Provider for {@link ItemHandlerJukeBox}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by StrikerRocker on 4/7/2018.
 */
public class CapProviderJukeBox implements ICapabilityProvider
{
    private LazyOptional inventoryHolder;

    public CapProviderJukeBox(JukeboxTileEntity jukebox)
    {
        this.inventoryHolder = LazyOptional.of(() -> new ItemHandlerJukeBox(jukebox));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
        return cap.orEmpty(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inventoryHolder);
    }
}
