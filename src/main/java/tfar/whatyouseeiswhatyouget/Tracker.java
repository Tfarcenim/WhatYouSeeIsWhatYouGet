package tfar.whatyouseeiswhatyouget;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeMod;

import java.util.List;

public class Tracker {

    private int ticks;
    private BlockPos last;

    void update(PlayerEntity player) {
        BlockRayTraceResult result = (BlockRayTraceResult) player.pick(player.getAttributeValue(ForgeMod.REACH_DISTANCE.get()),0,true);
        BlockPos current = result.getPos();
        if (result.getType() == RayTraceResult.Type.MISS) {
            ticks = 0;
            last = null;
        } else {
            if (!current.equals(last)) {
                ticks = 0;
            } else {
                ticks++;
            }

            if (ticks >= WhatYouSeeIsWhatYouGet.TIME) {
                World world = player.world;

                BlockState state = world.getBlockState(current);
                LootContext.Builder lootcontext$builder = new LootContext.Builder((ServerWorld)world)
                        .withRandom(world.rand)
                        .withParameter(LootParameters.field_237457_g_, Vector3d.copyCentered(current))
                        .withParameter(LootParameters.TOOL, player.getHeldItemMainhand())
                        .withNullableParameter(LootParameters.BLOCK_ENTITY, world.getTileEntity(current))
                        .withNullableParameter(LootParameters.THIS_ENTITY, player);
                List<ItemStack> drops = state.getDrops(lootcontext$builder);

                drops.forEach(itemStack -> {
                    if (!player.addItemStackToInventory(itemStack)) {
                        InventoryHelper.spawnItemStack(world,player.getPosX(),player.getPosY(),player.getPosZ(),itemStack);
                    }
                });
                ticks = 0;
            }
            last = current.toImmutable();
        }
    }
}
