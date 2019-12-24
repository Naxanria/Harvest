package com.naxanria.harvest;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/*
  @author: Naxanria
*/
@Mod(Harvest.MODID)
public class Harvest
{
  public static final String MODID = "harvest";
  public static final Logger LOGGER = LogManager.getLogger(MODID);
  
  public Harvest()
  {
    MinecraftForge.EVENT_BUS.addListener(this::onInteract);
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
  }
  
  private void onInteract(final PlayerInteractEvent.RightClickBlock event)
  {
    World world = event.getWorld();
    
    if (event.getHand() == Hand.OFF_HAND)
    {
      return;
    }
  
    BlockPos pos = event.getPos();
    BlockState state = world.getBlockState(pos);
    Block block = state.getBlock();
  
    Config config = Config.COMMON;
    List<String> blacklist = config.blacklist.get();
    boolean whitelist = config.whitelist.get();
    boolean consumeSeed = config.consumeSeed.get();
    
    if (block instanceof CropsBlock)
    {
      if (blacklist.contains(block.getRegistryName().toString()))
      {
        if (!whitelist)
        {
          return;
        }
      }
      else if (whitelist)
      {
        return;
      }
      
      CropsBlock crop = (CropsBlock) block;
      if (crop.isMaxAge(state))
      {
        if (!world.isRemote)
        {
          List<ItemStack> drops = Block.getDrops(state, (ServerWorld) world, pos, null);
          ItemStack seed = crop.getItem(world, pos, state);
  
          for (ItemStack drop : drops)
          {
            if (consumeSeed)
            {
              if (drop.getItem() == seed.getItem())
              {
                drop.shrink(1);
              }
            }
    
            if (!drop.isEmpty())
            {
              InventoryHelper.spawnItemStack(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, drop);
            }
          }
  
          world.setBlockState(pos, crop.withAge(0), 3);
        }
        
        event.setCanceled(true);
      }
    }
  }
}
