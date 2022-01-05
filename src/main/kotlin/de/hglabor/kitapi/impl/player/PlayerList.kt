package de.hglabor.kitapi.impl.player

import de.hglabor.plugins.kitapi.player.KitPlayer
import de.hglabor.plugins.kitapi.supplier.IPlayerList
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.*

object PlayerList : IPlayerList {
    private val players = mutableMapOf<UUID, TestPlayer>()

    val Player.testPlayer: TestPlayer
        get() = players.computeIfAbsent(uniqueId) { TestPlayer(uniqueId) }

    fun Player.removeFromList() {
        players.remove(uniqueId)
    }

    override fun getKitPlayer(player: Player): KitPlayer = player.testPlayer

    override fun getKitPlayer(uuid: UUID): KitPlayer = players.computeIfAbsent(uuid) { TestPlayer(uuid) }

    override fun getRandomAlivePlayer(): KitPlayer = players.values.filter { it.isValid }.random()

    override fun getTrackingTargets(): MutableList<Entity> =
        Bukkit.getOnlinePlayers().filter { it.testPlayer.isInArena }.toMutableList()
}