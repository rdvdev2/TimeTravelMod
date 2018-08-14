package com.rdvdev2.TimeTravelMod;

import net.minecraftforge.common.config.Config;

@Config(modid = "timetravelmod", name = "TTM")
public class ModConfigs {
    @Config.Name("Enable unimplemented blocks")
    @Config.Comment("If changed to true, all blocks in development will be added, even if they have no recipe or function.")
    @Config.RequiresMcRestart
    public static boolean unimplementedBlocks = false;
}
