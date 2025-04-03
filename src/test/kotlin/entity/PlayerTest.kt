package entity

import kotlin.test.Test
import kotlin.test.assertEquals


/**
 * Tests for class [Player]
 */
class PlayerTest {
    /**
     * Check if Player right initialized
     */
    @Test
    fun testPlayer() {
        val playerName = "Alice"

        val handCard = mutableListOf(
            Card(CardSuit.SPADES, CardValue.SEVEN),
            Card(CardSuit.HEARTS, CardValue.FOUR)
        )

        val drawDeck = mutableListOf(
            Card(CardSuit.CLUBS, CardValue.TWO),
            Card(CardSuit.SPADES, CardValue.FOUR),
            Card(CardSuit.HEARTS, CardValue.KING)
        )
        val player = Player(playerName, handCard, drawDeck)

        assertEquals(playerName, player.namePlayer)
        assertEquals(handCard, player.handCards)
        assertEquals(drawDeck, player.drawDeck)
    }

}

