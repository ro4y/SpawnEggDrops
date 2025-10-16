package com.ro4ymods.SpawnEggDrops;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = SpawnEggDrops.MODID, name = SpawnEggDrops.NAME, version = SpawnEggDrops.VERSION)
public class SpawnEggDrops {
    public static final String MODID = "spawneggdrops";
    public static final String NAME = "Spawn Egg Drops";
    public static final String VERSION = "1.0";

    private static final double DROP_CHANCE = 0.05;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if (!(event.getSource().getTrueSource() instanceof EntityPlayer)) {
            return;
        }

        EntityLivingBase entity = event.getEntityLiving();

        if (entity.world.rand.nextDouble() > DROP_CHANCE) {
            return;
        }

        ResourceLocation entityId = EntityList.getKey(entity);

        if (entityId != null) {
            ItemStack eggStack = new ItemStack(Item.getItemFromBlock(net.minecraft.init.Blocks.AIR));

            for (Item item : Item.REGISTRY) {
                if (item instanceof ItemMonsterPlacer) {
                    for (ResourceLocation name : EntityList.getEntityNameList()) {
                        ItemStack testStack = new ItemStack(item);
                        ItemMonsterPlacer.applyEntityIdToItemStack(testStack, name);
                        ResourceLocation stackEntity = ItemMonsterPlacer.getNamedIdFrom(testStack);

                        if (entityId.equals(stackEntity)) {
                            eggStack = testStack.copy();
                            break;
                        }
                    }
                    if (!eggStack.isEmpty()) {
                        break;
                    }
                }
            }

            if (!eggStack.isEmpty() && eggStack.getItem() instanceof ItemMonsterPlacer) {
                EntityItem entityItem = new EntityItem(
                        entity.world,
                        entity.posX,
                        entity.posY,
                        entity.posZ,
                        eggStack
                );
                entityItem.setDefaultPickupDelay();
                event.getDrops().add(entityItem);
            }
        }
    }
}