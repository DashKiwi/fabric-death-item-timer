package com.dashkiwi.deathitemtimer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class DeathItemTimerMod implements ModInitializer {
    
    @Override
    public void onInitialize() {
        ServerTickEvents.END_WORLD_TICK.register(this::onWorldTick);
    }
    
    private void onWorldTick(ServerWorld world) {
        for (ItemEntity itemEntity : world.iterateEntities()) {
            if (itemEntity instanceof ItemEntity) {
                updateItemTimer((ItemEntity) itemEntity);
            }
        }
    }
    
    private void updateItemTimer(ItemEntity itemEntity) {
        if (itemEntity.getThrower() != null) {
            if (itemEntity.hasCustomName()) {
                itemEntity.setCustomNameVisible(false);
            }
            return;
        }
        
        int age = itemEntity.getItemAge();
        int maxAge = 6000; // 5 minutes (300 seconds) for death items
        int remaining = maxAge - age;
        
        if (remaining <= 0) {
            itemEntity.setCustomNameVisible(false);
            return;
        }
        
        int seconds = remaining / 20;
        int minutes = seconds / 60;
        int displaySeconds = seconds % 60;
        
        Formatting color;
        Formatting labelColor;
        if (seconds > 60) {
            labelColor = Formatting.RED;
            color = Formatting.YELLOW;
        } else if (seconds > 30) {
            labelColor = Formatting.GOLD;
            color = Formatting.YELLOW;
        } else {
            labelColor = Formatting.DARK_RED;
            color = Formatting.RED;
        }
        
        String timeString;
        if (minutes > 0) {
            timeString = String.format("%d:%02d", minutes, displaySeconds);
        } else {
            timeString = String.format("%ds", displaySeconds);
        }
        
        Text customName = Text.literal("Despawn: ")
            .formatted(labelColor)
            .append(Text.literal(timeString).formatted(color));
        
        itemEntity.setCustomName(customName);
        itemEntity.setCustomNameVisible(true);
    }
}