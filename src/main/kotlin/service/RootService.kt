package service

import entity.UpAndDownGame
import gui.Refreshable

/**
 * This base service class provide access to all other services
 *
 * @property gameService The connected [GameService]
 * @property playerActionService The connected [PlayerActionService]
 * @property currentGame The currently active [entity.UpAndDownGame]
 */

class RootService {
    val gameService = GameService(this)
    val playerActionService = PlayerActionService(this)

    //The currently active game. Can be `null`, if no game has started yet.
    var currentGame: UpAndDownGame? = null

    /**
     * Adds the provided [newRefreshable] to all services connected to this root service
     * @param newRefreshable The [Refreshable] to be added
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        gameService.addRefreshable(newRefreshable)
        playerActionService.addRefreshable(newRefreshable)
    }

    /**
     * Adds each of the provided [newRefreshables] to all services
     * connected to this root service
     * @param newRefreshables The [Refreshable]s to be added
     */
    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }

}
