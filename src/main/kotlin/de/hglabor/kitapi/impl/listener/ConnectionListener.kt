package de.hglabor.kitapi.impl.listener

import de.hglabor.kitapi.impl.arena.ArenaManager.prepareKitSelection
import de.hglabor.kitapi.impl.player.PlayerList.removeFromList
import de.hglabor.kitapi.impl.player.PlayerList.testPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class ConnectionListener : Listener {

    @EventHandler
    fun onPlayerJoin(it: PlayerJoinEvent) {
        it.joinMessage = null
        it.player.prepareKitSelection()
    }

    @EventHandler
    fun onPlayerQuit(it: PlayerQuitEvent) {
        it.quitMessage = null
        val ffaPlayer = it.player.testPlayer
        ffaPlayer.kits.forEach { kit -> kit.onDisable(ffaPlayer) }
        if (ffaPlayer.isInCombat) it.player.damage(187000.0) //haha wo ist .kill() hin
        it.player.removeFromList()
    }

}