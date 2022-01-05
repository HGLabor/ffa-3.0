package de.hglabor.kitapi.impl

import de.hglabor.kitapi.impl.kit.KitSelectorImpl
import de.hglabor.kitapi.impl.kit.PassiveSelectorImpl
import de.hglabor.kitapi.impl.player.PlayerList
import org.bukkit.plugin.java.JavaPlugin

class KitTestServer : JavaPlugin() {

    override fun onLoad() {
        INSTANCE = this
    }

    override fun onEnable() {
        if(!dataFolder.exists()) {
            dataFolder.mkdirs()
        }
        KitApi.register(PlayerList, KitSelectorImpl(), PassiveSelectorImpl(), this, dataFolder.toPath())
    }

    companion object {
        lateinit var INSTANCE: KitTestServer; private set
    }
}

val KitApi get() = de.hglabor.plugins.kitapi.KitApi.getInstance()