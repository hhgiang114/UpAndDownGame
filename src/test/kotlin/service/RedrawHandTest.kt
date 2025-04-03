package service

import entity.*
import kotlin.test.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

/**
 * Check if the action redraw hand card happens correctly
 * Check [PlayerActionService.redrawHand]
 */
class RedrawHandTest {
    private var rootService = RootService()

    /** Set up for test */
    @BeforeTest
    fun setUp() {
        rootService = RootService()
        rootService.gameService.startNewGame(listOf("Alice", "Bob"))
    }

    /** Test drawing card when actual game does not exist */
    @Test
    fun `redraw card without current game`() {
        val game = RootService()
        // Set the current game of the root service to null
        game.currentGame = null

        // Test: No game is currently active
        assertThrows<IllegalStateException> { game.playerActionService.redrawHand() }
    }

    /** Test redraw hand card with fulfilled requirements */
    @Test
    fun `test redraw hand card`() {
        val game = rootService.currentGame
        assertNotNull(game)

        // Set up game
        game.currentPlayer = 0
        game.players[0].drawDeck = mutableListOf(
            Card(CardSuit.SPADES, CardValue.JACK),
            Card(CardSuit.HEARTS, CardValue.FIVE),
            Card(CardSuit.SPADES, CardValue.THREE),
            Card(CardSuit.SPADES, CardValue.FIVE),
            Card(CardSuit.DIAMONDS, CardValue.THREE)
        )

        game.players[0].handCards = mutableListOf(
            Card(CardSuit.SPADES, CardValue.QUEEN),
            Card(CardSuit.DIAMONDS, CardValue.JACK),
            Card(CardSuit.HEARTS, CardValue.KING),
            Card(CardSuit.CLUBS, CardValue.ACE),
            Card(CardSuit.DIAMONDS, CardValue.SEVEN),
            Card(CardSuit.SPADES, CardValue.TWO),
            Card(CardSuit.CLUBS, CardValue.FIVE),
            Card(CardSuit.HEARTS, CardValue.THREE)
        )
        // Test: The default state of the game is correct
        assertEquals(8, game.players[0].handCards.size)
        assertTrue(game.players[0].drawDeck.isNotEmpty())

        // Test: The player redraw hand card without an error
        assertDoesNotThrow { rootService.playerActionService.redrawHand() }

        // Test: The state of the game has changed
        assertEquals(1, game.currentPlayer)
        assertEquals(5, game.players[0].handCards.size)
    }

    /** Test redraw player has fewer than 8 cards on his hand  */
    @Test
    fun `test redraw card when not enough hand cards`() {
        val game = rootService.currentGame
        assertNotNull(game)

        // Set up game
        game.currentPlayer = 0
        game.players[0].drawDeck = mutableListOf(
            Card(CardSuit.SPADES, CardValue.JACK),
            Card(CardSuit.HEARTS, CardValue.FIVE),
            Card(CardSuit.SPADES, CardValue.THREE),
            Card(CardSuit.SPADES, CardValue.FIVE),
            Card(CardSuit.DIAMONDS, CardValue.THREE)
        )

        game.players[0].handCards = mutableListOf(
            Card(CardSuit.SPADES, CardValue.QUEEN),
            Card(CardSuit.DIAMONDS, CardValue.JACK),
            Card(CardSuit.HEARTS, CardValue.KING),
            Card(CardSuit.CLUBS, CardValue.ACE),
            Card(CardSuit.DIAMONDS, CardValue.SEVEN)
        )

        val exception = assertThrows<IllegalArgumentException> {
            rootService.playerActionService.redrawHand()
        }
        assertEquals("Player does not meet the conditions to redraw hand!", exception.message)
    }

    /** Test redraw when draw deck is empty */
    @Test
    fun `test redraw card with deck empty`() {
        val game = rootService.currentGame
        assertNotNull(game)

        // Set up game
        game.currentPlayer = 0
        game.players[0].drawDeck = mutableListOf()

        game.players[0].handCards = mutableListOf(
            Card(CardSuit.SPADES, CardValue.QUEEN),
            Card(CardSuit.DIAMONDS, CardValue.JACK),
            Card(CardSuit.HEARTS, CardValue.KING),
            Card(CardSuit.CLUBS, CardValue.ACE),
            Card(CardSuit.DIAMONDS, CardValue.SEVEN)
        )

        val exception = assertThrows<IllegalArgumentException> {
            rootService.playerActionService.redrawHand()
        }
        assertEquals("Player does not meet the conditions to redraw hand!", exception.message)
    }

}


