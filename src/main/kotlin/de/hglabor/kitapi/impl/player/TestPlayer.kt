package de.hglabor.kitapi.impl.player

import de.hglabor.plugins.kitapi.player.KitPlayerImpl
import org.bukkit.Bukkit
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard
import java.util.*

open class TestPlayer(uuid: UUID?) : KitPlayerImpl(uuid) {
    private val name: String
    var kills = 0
    private var status: Status
    private var scoreboard: Scoreboard? = null
    private var objective: Objective? = null
    fun increaseKills() {
        kills++
    }

    override fun getUUID(): UUID = uuid
    override fun getName(): String = name
    override fun isValid(): Boolean = isInArena
    val isInKitSelection get() = status == Status.KITSELECTION
    val isInArena get() = status == Status.ARENA

    fun setStatus(status: Status) {
        this.status = status
    }

    enum class Status {
        KITSELECTION, ARENA, SPECTATOR
    }

    init {
        status = Status.KITSELECTION
        name = Bukkit.getOfflinePlayer(uuid!!).name!!
    }
}