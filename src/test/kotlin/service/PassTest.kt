package service

import entity.Card
import entity.CardSuit
import entity.CardValue
import org.junit.jupiter.api.assertThrows
import kotlin.test.*


/**
 * Class for testing the [PlayerActionService.pass] methods of the [PlayerActionService].
 */

class PassTest {
    private var rootService = RootService()

    /** Set up for test */
    @BeforeTest
    fun setUp() {
        rootService = RootService()
        rootService.gameService.startNewGame(listOf("Alice", "Bob"))
    }

    /** Test pass if no game is currently active. */
    @Test
    fun testPassNoGame() {
        // Set the current game of the root service to null
        rootService.currentGame = null

        // Test: No game is currently active
        assertThrows<IllegalStateException> { rootService.playerActionService.pass() }
    }

    /** Test pass when player can still draw card*/
    @Test
    fun `test pass while can still draw card`() {
        val game = rootService.currentGame
        assertNotNull(game)

        // Set up game
        game.currentPlayer = 0
        game.players[0].drawDeck = mutableListOf(
            Card(CardSuit.SPADES, CardValue.JACK)
        )
        game.players[0].handCards = mutableListOf()

        assertFails { rootService.playerActionService.pass() }
    }

    /** Test pass when player can still redraw hand */
    @Test
    fun `test pass while can still redraw hand`() {
        val game = rootService.currentGame
        assertNotNull(game)

        // Set up game
        game.currentPlayer = 0
        game.players[0].drawDeck = mutableListOf(
            Card(CardSuit.SPADES, CardValue.JACK)
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
        assertFails { rootService.playerActionService.pass() }
    }

    /** Test pass when player can pass */
    @Test
    fun `test pass first`() {
        val game = rootService.currentGame
        assertNotNull(game)

        // Set up game
        game.lastPass = false
        game.currentPlayer = 0
        game.players[0].drawDeck.clear()
        game.players[0].handCards = mutableListOf(
            Card(CardSuit.SPADES, CardValue.QUEEN)
        )

        game.centerDeck1 = mutableListOf(
            Card(CardSuit.DIAMONDS, CardValue.TWO),
            Card(CardSuit.HEARTS, CardValue.EIGHT),
            Card(CardSuit.CLUBS, CardValue.SEVEN)
        )

        game.centerDeck2 = mutableListOf(
            Card(CardSuit.CLUBS, CardValue.SIX),
            Card(CardSuit.DIAMONDS, CardValue.SEVEN),
            Card(CardSuit.CLUBS, CardValue.SIX)
        )

        rootService.playerActionService.pass()

        assertEquals(1, game.currentPlayer)
        assertEquals(true, game.lastPass)
    }

    /** Test pass when player can pass and last pass is true */
    @Test
    fun `test pass second `() {
        val game = rootService.currentGame
        assertNotNull(game)

        // Set up game
        game.lastPass = true
        game.currentPlayer = 1
        game.players[0].drawDeck.clear()
        game.players[0].handCards = mutableListOf(
            Card(CardSuit.SPADES, CardValue.QUEEN)
        )

        game.players[1].drawDeck.clear()
        game.players[1].handCards = mutableListOf(
            Card(CardSuit.DIAMONDS, CardValue.QUEEN)
        )


        game.centerDeck1 = mutableListOf(
            Card(CardSuit.DIAMONDS, CardValue.TWO),
            Card(CardSuit.HEARTS, CardValue.EIGHT),
            Card(CardSuit.CLUBS, CardValue.SEVEN)
        )

        game.centerDeck2 = mutableListOf(
            Card(CardSuit.CLUBS, CardValue.SIX),
            Card(CardSuit.DIAMONDS, CardValue.SEVEN),
            Card(CardSuit.CLUBS, CardValue.SIX)
        )

        rootService.playerActionService.pass()
        assertEquals(true, game.lastPass)
    }

}