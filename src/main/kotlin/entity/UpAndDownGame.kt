package entity

/**
 * Represents an UpAndDown game
 * @property players The players of the game
 * @property currentPlayer The index of the current player
 * @property lastPass Whether the last Action is Pass or not
 * @property centerDeck1 First center stack of cards that are played
 * @property centerDeck2 Second center stack of cards that are played
 */

class UpAndDownGame(val players: List<Player>, var currentPlayer: Int) {
    var lastPass: Boolean = false
    var centerDeck1: MutableList<Card> = mutableListOf()
    var centerDeck2: MutableList<Card> = mutableListOf()
}



