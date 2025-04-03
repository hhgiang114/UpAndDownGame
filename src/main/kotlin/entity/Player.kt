package entity

/**
 * Represents a player in game Up And Down.
 *
 * @property namePlayer The name of the player
 * @property handCards The hand of [Card]s of the player
 * @property drawDeck The draw pile [Card]s of the player
 */

data class Player(val namePlayer: String, var handCards: MutableList<Card>, var drawDeck: MutableList<Card>)