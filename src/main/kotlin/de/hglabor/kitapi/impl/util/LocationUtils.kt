package de.hglabor.kitapi.impl.util

import de.hglabor.plugins.kitapi.kit.kits.GladiatorKit
import org.bukkit.HeightMap
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import java.util.*


object LocationUtils {

    private val random: Random = Random()
    private val skipBlocks: List<Material> = listOf(
        Material.RED_STAINED_GLASS,
        Material.GREEN_STAINED_GLASS,
        Material.YELLOW_STAINED_GLASS
    )

    fun getHighestBlock(world: World, spread: Int, tryCounter: Int): Location {
        val seaLevel = world.seaLevel
        val randomX: Int = random.nextInt(spread + spread) - spread
        val randomZ: Int = random.nextInt(spread + spread) - spread
        if (tryCounter > 8) {
            val highestY = world.getHighestBlockYAt(randomX, randomZ, HeightMap.MOTION_BLOCKING_NO_LEAVES)
            return Location(world, randomX.toDouble(), highestY.toDouble(), randomZ.toDouble())
        }
        for (i in seaLevel + 20 downTo seaLevel - 10 + 1) {
            val block: Block = world.getBlockAt(randomX, i, randomZ)
            val blockLoc: Location = block.location
            val type: Material = block.type
            if (type == GladiatorKit.INSTANCE.material || skipBlocks.contains(type)) {
                continue
            }
            if (block.type.isSolid && block.getRelative(BlockFace.DOWN).type.isSolid) {
                if (!blockLoc.clone().add(0.0, 1.0, 0.0).block.type.isSolid && !blockLoc.clone().add(0.0, 2.0, 0.0).block.type.isSolid) {
                    return block.location
                }
            }
        }
        return getHighestBlock(world, spread, tryCounter + 1)
    }
}