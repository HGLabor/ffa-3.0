package de.hglabor.kitapi.impl.kit

import de.hglabor.kitapi.impl.player.PlayerList.testPlayer
import de.hglabor.plugins.kitapi.KitApi
import de.hglabor.plugins.kitapi.kit.Passive
import de.hglabor.plugins.kitapi.kit.selector.PassiveSelector
import de.hglabor.utils.localization.Localization
import de.hglabor.utils.noriskutils.ChatUtils
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class PassiveSelectorImpl : PassiveSelector(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Passive Selector") {

    @EventHandler
    override fun onKitSelectorClick(event: PlayerInteractEvent) {
        if (event.item != null && isKitSelectorItem(event.item)) {
            val testPlayer = event.player.testPlayer
            if (event.player.openInventory.title.contains(PASSIVE_SELECTOR_TITLE)) {
                return
            }
            if (testPlayer.isInKitSelection) {
                openFirstPage(event.player)
            }
        }
    }

    @EventHandler
    override fun onInventoryClick(event: InventoryClickEvent) {
        val player: Player = event.whoClicked as Player
        val clickedItem: ItemStack? = event.currentItem
        val inventoryTitle = event.view.title
        if (clickedItem == null) {
            return
        }
        if (inventoryTitle.contains(PASSIVE_SELECTOR_TITLE)) {
            event.isCancelled = true
            if (nextPage(inventoryTitle, clickedItem, player)) {
                return
            }
            if (lastPage(inventoryTitle, clickedItem, player)) {
                return
            }
            val kitSelector: ItemStack? = getKitSelectorInHand(player)
            val kit = KitApi.getInstance().byItem(clickedItem)
            if (kit is Passive) {
                if (kitSelector != null && isKitSelectorItem(kitSelector)) {
                    val testPlayer = player.testPlayer
                    testPlayer.passive = kit
                    player.playSound(player.location, Sound.ENTITY_VILLAGER_YES, 1f, 1f)
                    player.closeInventory()
                }
            }
        }
    }

    private fun getKitSelectorInHand(player: Player): ItemStack? {
        if (getPassiveSelectorItem().isSimilar(player.inventory.itemInMainHand)) {
            return player.inventory.itemInMainHand
        } else if (getPassiveSelectorItem().isSimilar(player.inventory.itemInOffHand)) {
            return player.inventory.itemInOffHand
        }
        return null
    }

}