package service

import entity.Card
import entity.CardSuit
import entity.CardValue
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertNotNull
import kotlin.test.*

/**
 * Class for testing the [GameService.showWinner]
 */
class ShowWinnerTest {
    private var rootService = RootService()

    /** Set up for test */
    @BeforeTest
    fun setUp() {
        rootService = RootService()
        rootService.gameService.startNewGame(listOf("Alice", "Bob"))
    }

    /** Test showing winner if no game is currently active. */
    @Test
    fun showWinnerNoGame() {
        // Set the current game of the root service to null
        rootService.currentGame = null

        // Test: No game is currently active
        assertThrows<IllegalStateException> { rootService.gameService.showWinner() }
    }

    /** Test show winner when current player does have no hand card and draw card left */
    @Test
    fun testShowWinner() {
        val game = rootService.currentGame
        assertNotNull(game)

        // Set up game
        game.currentPlayer = 0
        game.players[0].drawDeck.clear()
        game.players[0].handCards.clear()
        rootService.gameService.showWinner()
        assertEquals("Winner is: Alice", rootService.gameService.showWinner())
    }

    /** Test show winner both player pass */
    @Test
    fun `test show winner with pass`() {
        val game = rootService.currentGame
        assertNotNull(game)

        // Set up game
        game.lastPass = true
        game.currentPlayer = 0

        game.players[0].drawDeck.clear()
        game.players[0].handCards = mutableListOf(
            Card(CardSuit.SPADES, CardValue.QUEEN),
            Card(CardSuit.DIAMONDS, CardValue.JACK),
            Card(CardSuit.HEARTS, CardValue.KING),
            Card(CardSuit.CLUBS, CardValue.ACE),
        )

        game.players[1].drawDeck.clear()
        game.players[1].handCards = mutableListOf(
            Card(CardSuit.DIAMONDS, CardValue.QUEEN),
            Card(CardSuit.SPADES, CardValue.TWO),
            Card(CardSuit.HEARTS, CardValue.THREE)
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

        rootService.gameService.showWinner()
        assertEquals("Winner is: Bob", rootService.gameService.showWinner())
    }

    /** Test show winner both player pass and draw */
    @Test
    fun `test show winner draw`() {
        val game = rootService.currentGame
        assertNotNull(game)

        // Set up game
        game.lastPass = true
        game.currentPlayer = 0

        game.players[0].drawDeck.clear()
        game.players[0].handCards = mutableListOf(
            Card(CardSuit.SPADES, CardValue.QUEEN),
            Card(CardSuit.DIAMONDS, CardValue.JACK)
        )

        game.players[1].drawDeck.clear()
        game.players[1].handCards = mutableListOf(
            Card(CardSuit.DIAMONDS, CardValue.QUEEN),
            Card(CardSuit.SPADES, CardValue.TWO)
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

        rootService.gameService.showWinner()
        assertEquals("This is a tie!", rootService.gameService.showWinner())
    }

}