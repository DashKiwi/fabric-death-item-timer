scoreboard players operation #temp death_dropped = @s death_dropped

scoreboard players operation #temp death_dropped /= #20 death_dropped

execute store result storage despawntimer:timer seconds int 1 run scoreboard players get #temp death_dropped

function despawntimer:set_timer_name with storage despawntimer:timer