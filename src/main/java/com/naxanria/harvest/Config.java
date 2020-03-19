package com.naxanria.harvest;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/*
  @author: Naxanria
*/
public class Config
{
  public static final Config COMMON;
  public static final ForgeConfigSpec SPEC;
  
  static
  {
    final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
    SPEC = specPair.getRight();
    COMMON = specPair.getLeft();
  }
  
  public ForgeConfigSpec.ConfigValue<List<String>> blacklist;
  public ForgeConfigSpec.BooleanValue whitelist;
  public ForgeConfigSpec.BooleanValue consumeSeed;
  public ForgeConfigSpec.BooleanValue harvestNetherWart;
  
  public Config(ForgeConfigSpec.Builder builder)
  {
    builder.comment("Harvest config");
    builder.push("common");
    blacklist = builder
      .comment("The blacklisted crops, these can not be harvested")
      .define("blacklist", new ArrayList<>());
    
    whitelist = builder
      .comment("Use the blacklist as a whitelist instead")
      .define("whitelist", false);
    
    consumeSeed = builder
      .comment("Consumes a seed (from the drops) to replant")
      .define("consumeSeed", true);
    
    harvestNetherWart = builder
      .comment("Harvest works on netherwart")
      .define("netherwart", true);
  }
  
}
