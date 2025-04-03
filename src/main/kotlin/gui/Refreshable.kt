package gui

import entity.Card

/**
 * This interface provides a mechanism by which service layer classes tell to view classes
 * that some changes have been made to the object layer so that the user´s interface can be updated
 */
interface Refreshable {

    /** Perform refreshes after a new game started */
    fun refreshAfterStartNewGame() {}

    /** Perform refreshes after a turn started */
    fun refreshAfterTurnStart() {}

    /** Perform refreshes after we changed turn to another player */
    fun refreshAfterChangePlayer() {}

    /** Perform refreshes after player decided to draw a card
     * @param card The [Card] that has been drawn
     */
    fun refreshAfterDrawCard(card: Card) {}

    /** Perform refreshes after player decided to play a card
     * @param card The [Card] that has valid to play
     */
    fun refreshAfterPlayCard(card: Card) {}

    /** Perform refreshes after player decided to change hand cards */
    fun refreshAfterRedrawHand() {}

    /** Perform refreshes after show winner */
    fun refreshAfterShowWinner() {}

    /** Perform refreshes after player decided to restart the game */
    fun refreshAfterRestartNewGame() {}

}