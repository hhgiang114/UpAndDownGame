package service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

/** Class to check correct order of switch  players
 * checks [GameService.switchPlayer]
 */
class SwitchPlayerTest {
    /** Test if the game switch player correctly */
    @Test
    fun `test ChangeToNextPlayer`() {
        val game = RootService()
        val playerNames = listOf("Alice", "Bob")
        game.gameService.startNewGame(playerNames)

        assertEquals("Alice", game.currentGame!!.players.first().namePlayer) //check for first player
        game.gameService.switchPlayer() //changing the current player in the game
        assertEquals("Bob", game.currentGame!!.players[1].namePlayer) //check for second player
        game.gameService.switchPlayer()
        assertEquals("Alice", game.currentGame!!.players[0].namePlayer) //back to the first player in the list
    }

    /** Test switch player when actual game does not exist */
    @Test
    fun `ChangeToNextPlayer without game`() {
        //Set the current game of the root service to null
        val game = RootService()
        game.currentGame = null

        // Test: No game is currently active
        assertThrows<IllegalStateException> { game.gameService.switchPlayer() }
    }

}


