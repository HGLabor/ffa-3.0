package de.hglabor.kitapi.impl.kit

import de.hglabor.kitapi.impl.arena.ArenaManager.teleportToArena
import de.hglabor.kitapi.impl.player.PlayerList.testPlayer
import de.hglabor.plugins.kitapi.KitApi
import de.hglabor.plugins.kitapi.kit.kits.NoneKit
import de.hglabor.plugins.kitapi.kit.selector.KitSelector
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class KitSelectorImpl : KitSelector(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Kit Selector") {

    @EventHandler
    public override fun onKitSelectorClick(event: PlayerInteractEvent) {
        if (event.item != null && isKitSelectorItem(event.item)) {
            val testPlayer = event.player.testPlayer
            if (event.player.openInventory.title.contains(KIT_SELECTOR_TITLE)) {
                return
            }
            if (testPlayer.isInKitSelection) {
                openFirstPage(event.player)
            }
        }
    }

    @EventHandler
    public override fun onInventoryClick(event: InventoryClickEvent) {
        val player: Player = event.whoClicked as Player
        val clickedItem: ItemStack? = event.currentItem
        val inventoryTitle = event.view.title
        if (clickedItem == null) {
            return
        }
        if (inventoryTitle.contains(KIT_SELECTOR_TITLE)) {
            event.isCancelled = true
            if (nextPage(inventoryTitle, clickedItem, player)) {
                return
            }
            if (lastPage(inventoryTitle, clickedItem, player)) {
                return
            }
            val kitSelector: ItemStack? = getKitSelectorInHand(player)
            val kit = KitApi.getInstance().byItem(clickedItem)
            if (kitSelector != null && isKitSelectorItem(kitSelector) && kit != null) {
                val index: Int = kitSelector.itemMeta.displayName
                    .substring(kitSelector.itemMeta.displayName.length - 1).toInt() - 1
                val testPlayer = player.testPlayer
                testPlayer.setKit(kit, index)
                player.playSound(player.location, Sound.ENTITY_VILLAGER_YES, 1f, 1f)
                player.closeInventory()
                if (testPlayer.kits.stream().noneMatch { kits -> kits.equals(NoneKit.INSTANCE) }) {
                    player.teleportToArena()
                }
            }
        }
    }

    private fun getKitSelectorInHand(player: Player): ItemStack? {
        for (kitSelectorItem in kitSelectorItems) {
            if (kitSelectorItem.isSimilar(player.inventory.itemInMainHand)) {
                return player.inventory.itemInMainHand
            } else if (kitSelectorItem.isSimilar(player.inventory.itemInOffHand)) {
                return player.inventory.itemInOffHand
            }
        }
        return null
    }
}