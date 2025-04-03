package entity

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for the [UpAndDownGame] class
 */

class UpAndDownGameTest {
    val handCardsAlice = mutableListOf(
        Card(CardSuit.SPADES, CardValue.QUEEN),
        Card(CardSuit.DIAMONDS, CardValue.JACK),
        Card(CardSuit.SPADES, CardValue.ACE)
    )

    val drawCardsAlice = mutableListOf(
        Card(CardSuit.CLUBS, CardValue.KING),
        Card(CardSuit.HEARTS, CardValue.ACE),
        Card(CardSuit.DIAMONDS, CardValue.TEN)
    )

    val handCardsBob = mutableListOf(
        Card(CardSuit.HEARTS, CardValue.TEN),
        Card(CardSuit.SPADES, CardValue.JACK),
        Card(CardSuit.CLUBS, CardValue.QUEEN)
    )

    val drawCardsBob = mutableListOf(
        Card(CardSuit.SPADES, CardValue.KING),
        Card(CardSuit.HEARTS, CardValue.FOUR),
        Card(CardSuit.DIAMONDS, CardValue.NINE)
    )

    val player1 = Player("Alice", handCardsAlice, drawCardsAlice)
    val player2 = Player("Bob", handCardsBob, drawCardsBob)
    val players = listOf(player1, player2)

    val currentPlayer = players.indexOf(player1)
    val lastPass = false

    val centerDeck1 = mutableListOf(Card(CardSuit.CLUBS, CardValue.ACE))
    val centerDeck2 = mutableListOf(Card(CardSuit.HEARTS, CardValue.KING))

    val game = UpAndDownGame(players, currentPlayer)


    @Test
    fun testGame() {
        assertEquals(2, game.players.size)
        assertEquals(0, game.currentPlayer)
        assertEquals(false, game.lastPass)
//        assertEquals(centerDeck1, game.centerDeck1)
//        assertEquals(centerDeck2, game.centerDeck2)
    }
}

