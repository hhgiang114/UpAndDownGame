package service

import entity.Card
import gui.Refreshable

/**
 * [Refreshable] implementation that refreshes nothing, but remembers
 * if a refresh method has been called (since last [reset])
 *
 * @param rootService The root service to which this service belongs
 */
class RefreshableTest(val rootService: RootService) : Refreshable {

    var refreshAfterStartNewGameCalled: Boolean = false
        private set

    var refreshAfterTurnStartCalled: Boolean = false
        private set

    var refreshAfterChangePlayerCalled: Boolean = false
        private set

    var refreshAfterDrawCardCalled: Boolean = false
        private set

    var refreshAfterPlayCardCalled: Boolean = false
        private set

    var refreshAfterRedrawHandCalled: Boolean = false
        private set

    var refreshAfterShowWinnerCalled: Boolean = false
        private set

    var refreshAfterRestartNewGameCalled: Boolean = false
        private set

    /**
     * Reset all *Called properties to false
     */
    fun reset() {
        refreshAfterStartNewGameCalled = false
        refreshAfterTurnStartCalled = false
        refreshAfterChangePlayerCalled = false
        refreshAfterDrawCardCalled = false
        refreshAfterPlayCardCalled = false
        refreshAfterRedrawHandCalled = false
        refreshAfterShowWinnerCalled = false
        refreshAfterRestartNewGameCalled = false
    }

    override fun refreshAfterStartNewGame() {
        refreshAfterStartNewGameCalled = true
    }

    override fun refreshAfterTurnStart() {
        refreshAfterTurnStartCalled = true
    }

    override fun refreshAfterChangePlayer() {
        refreshAfterChangePlayerCalled = true
    }

    override fun refreshAfterDrawCard(card: Card) {
        refreshAfterDrawCardCalled = true
    }

    override fun refreshAfterPlayCard(card: Card) {
        refreshAfterPlayCardCalled = true
    }

    override fun refreshAfterRedrawHand() {
        refreshAfterRedrawHandCalled = true
    }

    override fun refreshAfterShowWinner() {
        refreshAfterShowWinnerCalled = true
    }

    override fun refreshAfterRestartNewGame() {
        refreshAfterRestartNewGameCalled = true
    }

}
