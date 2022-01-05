package de.hglabor.kitapi.impl.arena

import de.hglabor.kitapi.impl.KitTestServer
import de.hglabor.kitapi.impl.listener.ConnectionListener
import de.hglabor.kitapi.impl.player.PlayerList.testPlayer
import de.hglabor.kitapi.impl.player.TestPlayer
import de.hglabor.kitapi.impl.util.LocationUtils
import de.hglabor.plugins.kitapi.KitApi
import de.hglabor.plugins.kitapi.kit.AbstractKit
import de.hglabor.plugins.kitapi.kit.config.KitMetaData
import de.hglabor.plugins.kitapi.kit.passives.NonePassive
import de.hglabor.utils.noriskutils.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer
import java.util.stream.IntStream

object ArenaManager {

    val world = Bukkit.getWorld("world")

    fun Player.prepareKitSelection() {
        testPlayer.setStatus(TestPlayer.Status.KITSELECTION)
        testPlayer.kits.forEach(Consumer { kit: AbstractKit -> kit.onDisable(testPlayer) })
        testPlayer.kills = 0
        testPlayer.lastHitInformation.clear()
        testPlayer.kits = KitApi.getInstance().emptyKitList()
        testPlayer.passive = NonePassive.INSTANCE
        testPlayer.resetKitAttributes()
        maxHealth = 20.0
        health = 20.0
        foodLevel = 20
        level = 0
        fireTicks = 0
        isGliding = false
        isGlowing = false
        totalExperience = 0
        gameMode = GameMode.ADVENTURE
        allowFlight = false
        isFlying = false
        isInvisible = false
        Arrays.stream(KitMetaData.values()).forEach { metaData: KitMetaData -> removeMetadata(metaData.key, KitTestServer.INSTANCE) }
        closeInventory()
        inventory.clear()
        KitApi.getInstance().kitSelector.kitSelectorItems.forEach(Consumer { kitSelector: ItemStack ->
            inventory.addItem(kitSelector)
        })
        inventory.addItem(KitApi.getInstance().passiveSelector.passiveSelectorItem)
    }

    fun Player.teleportToArena() {
        testPlayer.setStatus(TestPlayer.Status.ARENA)
        gameMode = GameMode.SURVIVAL
        allowFlight = false
        isFlying = false
        fireTicks = 0
        teleport(LocationUtils.getHighestBlock(world, (world.worldBorder.size / 2).toInt(), 0).clone().add(0.0, 1.0, 0.0))
        giveArenaEquip()
    }

    private fun Player.giveArenaEquip() {
        inventory.clear()
        inventory.setItem(1, ItemBuilder(Material.COMPASS).setName("§cTracker").build())
        inventory.setItem(13, ItemStack(Material.BOWL, 32))
        inventory.setItem(14, ItemStack(Material.RED_MUSHROOM, 32))
        inventory.setItem(15, ItemStack(Material.BROWN_MUSHROOM, 32))
        val itemCount = AtomicInteger()
        testPlayer.kits.forEach {
            if (!it.isUsingOffHand) {
                it.kitItems.forEach { kitItem -> inventory.setItem(1 + itemCount.incrementAndGet(), kitItem) }
            } else {
                inventory.setItemInOffHand(it.mainKitItem)
            }
            it.onEnable(testPlayer)
        }
        testPlayer.passive.onEnable(testPlayer)
        IntStream.range(0, 31 - itemCount.get()).mapToObj { ItemStack(Material.MUSHROOM_STEW) }.forEach { itemStacks -> inventory.addItem(itemStacks) }
    }

    init {
        Bukkit.getPluginManager().registerEvents(ConnectionListener(), KitTestServer.INSTANCE)
    }

}