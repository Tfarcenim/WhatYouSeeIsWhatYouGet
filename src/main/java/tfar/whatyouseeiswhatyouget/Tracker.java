package tfar.whatyouseeiswhatyouget;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

public class Tracker {

    private int ticks;
    private BlockPos last;

    public Tracker() {
    }


    void update(PlayerEntity player) {
        BlockRayTraceResult result = (BlockRayTraceResult) player.pick(player.getAttributeValue(ForgeMod.REACH_DISTANCE.get()),0,true);
        if (result.getType() == RayTraceResult.Type.MISS) {
            ticks = 0;
            last = null;
        } else {
            if (!result.getPos().equals(last)) {
                ticks = 0;
            } else {
                ticks++;
            }

            if (ticks >= WhatYouSeeIsWhatYouGet.TIME) {
                World world = player.world;
                world.destroyBlock(result.getPos(),true,player);
                ticks = 0;
            }

            last = result.getPos().toImmutable();
        }
    }
}
