package service

import entity.Card
import entity.CardSuit
import entity.CardValue
import org.junit.jupiter.api.assertThrows
import kotlin.test.*

/**
 * Check if the action play card happens correctly
 * Check [PlayerActionService.playCard]
 */
class PlayCardTest {
    private var rootService = RootService()

    /** Set up for test */
    @BeforeTest
    fun setUp() {
        rootService = RootService()
        rootService.gameService.startNewGame(listOf("Alice", "Bob"))
    }

    /** Test playing card when actual game does not exist */
    @Test
    fun testPlayCardNoGame() {
        // Set the current game of the root service to null
        rootService.currentGame = null

        // Test: Playing a card throws an error
        assertThrows<IllegalStateException> {
            rootService.playerActionService.playCard(Card(CardSuit.CLUBS, CardValue.TWO))
        }
    }

    /** Test playing a valid card. */
    @Test
    fun `test play valid card`() {
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
            Card(CardSuit.HEARTS, CardValue.NINE),
            Card(CardSuit.CLUBS, CardValue.ACE),
            Card(CardSuit.DIAMONDS, CardValue.SEVEN),
            Card(CardSuit.SPADES, CardValue.TWO),
            Card(CardSuit.CLUBS, CardValue.FIVE),
            Card(CardSuit.HEARTS, CardValue.THREE)
        )

        game.centerDeck1 = mutableListOf(Card(CardSuit.DIAMONDS, CardValue.TWO), Card(CardSuit.HEARTS, CardValue.TEN))
        game.centerDeck2 = mutableListOf(Card(CardSuit.CLUBS, CardValue.SIX), Card(CardSuit.DIAMONDS, CardValue.SEVEN))

        val cardToPlay = game.players[0].handCards[2]

        assertEquals(8, game.players[0].handCards.size)
        assertEquals(2, game.centerDeck1.size)

        rootService.playerActionService.playCard(cardToPlay)

        assertEquals(7, game.players[0].handCards.size)
        assertEquals(3, game.centerDeck1.size)

    }

    /** Test playing an invalid card. */
    @Test
    fun `test play invalid card`() {
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

        game.centerDeck1 = mutableListOf(Card(CardSuit.DIAMONDS, CardValue.TWO), Card(CardSuit.HEARTS, CardValue.EIGHT))
        game.centerDeck2 = mutableListOf(Card(CardSuit.CLUBS, CardValue.SIX), Card(CardSuit.DIAMONDS, CardValue.SEVEN))

        val cardToPlay = game.players[0].handCards[3]

        assertEquals(8, game.players[0].handCards.size)
        assertEquals(2, game.centerDeck1.size)

        val exception = assertThrows<IllegalArgumentException> {
            rootService.playerActionService.playCard(cardToPlay)
        }

        assertEquals("Selected card is not valid!", exception.message)

    }
}