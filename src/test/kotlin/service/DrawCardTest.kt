package service

import entity.Card
import entity.CardSuit
import entity.CardValue
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.*

/**
 * Class for testing the [PlayerActionService.drawCard] methods of the [PlayerActionService].
 */
class DrawCardTest {
    private var rootService = RootService()

    /** Set up for test */
    @BeforeTest
    fun setUp() {
        rootService = RootService()
        rootService.gameService.startNewGame(listOf("Alice", "Bob"))
    }

    /** Test drawing a card if no game is currently active. */
    @Test
    fun testDrawCardNoGame() {
        // Set the current game of the root service to null
        rootService.currentGame = null

        // Test: No game is currently active
        assertThrows<IllegalStateException> { rootService.playerActionService.drawCard() }
    }

    /** Test drawing a card from a non-empty draw stack when player is allowed to draw */
    @Test
    fun testDrawCard() {
        val game = rootService.currentGame
        assertNotNull(game)

        // Set up game
        game.currentPlayer = 0
        game.players[0].drawDeck = mutableListOf(
            Card(CardSuit.SPADES, CardValue.JACK))

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

        // Test: The player draws a card without an error
        assertDoesNotThrow { rootService.playerActionService.drawCard() }

        // Test: The state of the game has changed
        assertEquals(8 + 1, game.players[0].handCards.size)
        assertTrue(game.players[0].drawDeck.isEmpty())
        assertEquals(1, game.currentPlayer)

    }

    /** Test drawing a card from a non-empty draw stack when player is not allowed to draw */
    @Test
    fun `test draw card with hand full`() {
        val game = rootService.currentGame
        assertNotNull(game)

        // Set up game
        game.currentPlayer = 0
        game.players[0].drawDeck = mutableListOf(
            Card(CardSuit.SPADES, CardValue.JACK))

        game.players[0].handCards = mutableListOf(
            Card(CardSuit.SPADES, CardValue.QUEEN),
            Card(CardSuit.DIAMONDS, CardValue.JACK),
            Card(CardSuit.HEARTS, CardValue.KING),
            Card(CardSuit.CLUBS, CardValue.ACE),
            Card(CardSuit.DIAMONDS, CardValue.SEVEN),
            Card(CardSuit.SPADES, CardValue.TWO),
            Card(CardSuit.CLUBS, CardValue.FIVE),
            Card(CardSuit.HEARTS, CardValue.THREE),
            Card(CardSuit.CLUBS, CardValue.SEVEN),
            Card(CardSuit.HEARTS, CardValue.TWO),
        )

        val exception = assertThrows<IllegalArgumentException> {
            rootService.playerActionService.drawCard()
        }

        assertEquals("Player does not meet the conditions to draw card!", exception.message)
    }

    /** Test drawing a card from a empty draw stack */
    @Test
    fun `test draw card from empty stack`() {
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
            Card(CardSuit.DIAMONDS, CardValue.SEVEN),
            Card(CardSuit.SPADES, CardValue.TWO),
            Card(CardSuit.CLUBS, CardValue.FIVE),
            Card(CardSuit.HEARTS, CardValue.THREE),
            Card(CardSuit.CLUBS, CardValue.SEVEN),
            Card(CardSuit.HEARTS, CardValue.TWO),
        )

        val exception = assertThrows<IllegalArgumentException> {
            rootService.playerActionService.drawCard()
        }

        assertEquals("Player does not meet the conditions to draw card!", exception.message)
    }

}

