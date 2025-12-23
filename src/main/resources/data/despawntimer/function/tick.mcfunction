scoreboard players set #-1 death_dropped -1

execute as @e[type=item,tag=death_item] store result score @s death_dropped run data get entity @s Age
execute as @e[type=item,tag=death_item] run scoreboard players operation @s death_dropped *= #-1 death_dropped
execute as @e[type=item,tag=death_item] run scoreboard players add @s death_dropped 6000

execute as @e[type=item,tag=death_item,scores={death_dropped=1..}] run data modify entity @s CustomNameVisible set value 1b
execute as @e[type=item,tag=death_item,scores={death_dropped=1..}] run function despawntimer:display_timer

execute as @e[type=item,tag=death_item,scores={death_dropped=..0}] run tag @s remove death_item