scoreboard players operation #temp death_dropped = @s death_dropped
scoreboard players operation #temp death_dropped /= #20 death_dropped
execute store result storage despawntimer:timer seconds int 1 run scoreboard players get #temp death_dropped

execute if score #temp death_dropped matches 200.. run function despawntimer:set_timer_name_green with storage despawntimer:timer
execute if score #temp death_dropped matches 60..199 run function despawntimer:set_timer_name_yellow with storage despawntimer:timer
execute if score #temp death_dropped matches ..59 run function despawntimer:set_timer_name_red with storage despawntimer:timer