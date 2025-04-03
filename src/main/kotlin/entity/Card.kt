package entity

/**
 * Represents a card in a deck of cards.
 * @property suit The [CardSuit] of the card
 * @property value The [CardValue] of the card
 */

data class Card(val suit: CardSuit, val value: CardValue) {

    //Return a string representation of the card
    override fun toString() = "$suit$value"

    /**
     * Compares this card with another card based on its value.
     * This function is primarily used for sorting purposes in entity tests.
     * @param other The card to compare with.
     */
    operator fun compareTo(other: Card) = this.value.ordinal - other.value.ordinal

    /**
     * Compares the suit of this card with another card's suit.
     * @param other The card whose suit is to be compared with this card's suit.
     * @return A negative integer if this card's suit is less than the other
     *         card's suit, zero if they are equal, and a positive integer if
     *         this card's suit is greater than the other card's suit.
     */
    fun compareSuit(other: Card): Int {
        return this.suit.ordinal - other.suit.ordinal
    }
}



