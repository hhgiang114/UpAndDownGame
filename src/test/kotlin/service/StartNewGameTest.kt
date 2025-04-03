package service

import kotlin.test.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

/**
 * Provides tests for starting new game
 * Check [GameService.startNewGame] and [GameService.startTurn]
 */
class StartNewGameTest {
    private var rootService = RootService()

    /** Set up for test */
    @BeforeTest
    fun setUp() {
        rootService = RootService()
        rootService.gameService.startNewGame(listOf("Alice", "Bob"))
    }

    /** Test creating a game with player */
    @Test
    fun `test StartNewGame`() {
        val game = rootService.currentGame
        kotlin.test.assertNotNull(game)

        // Test: The game has been created with the correct players
        assertEquals(2, game.players.size)
        assertEquals("Alice", game.players[0].namePlayer)
        assertEquals("Bob", game.players[1].namePlayer)

        // Check player hands
        assertEquals(5, game.players[0].handCards.size)
        assertEquals(5, game.players[1].handCards.size)

        // Check central decks
        assertEquals(1, game.centerDeck1.size)
        assertEquals(1, game.centerDeck2.size)

        // Check draw decks
        assertEquals(20, game.players[0].drawDeck.size)
        assertEquals(20, game.players[1].drawDeck.size)

        // Check current player
        assertEquals(0, game.currentPlayer)
    }

    /** Test starting a turn with no game active. */
    @Test
    fun testStartTurnNoGame() {
        // Set the current game of the root service to null
        rootService.currentGame = null

        // Test: No game is currently active
        assertThrows<IllegalStateException> { rootService.gameService.startTurn() }
    }
}
