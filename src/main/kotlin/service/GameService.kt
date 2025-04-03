package service

import entity.*

/**
 * This service class is responsible for modifying the game entities and player actions.
 * @property rootService The root service, which provides access to all other services
 */

class GameService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Start a new game
     * Set up game and choose player
     * @property players the list of players who will participate in the game.
     */
    fun startNewGame(players: List<String>) {
        // Create deck for the game: 52 cards
        val deck = createDeck()

        // Divide the deck into 2 piles, each pile consists of 26 cards
        val halfDeck1 = deck.subList(0, 26).toMutableList()
        val halfDeck2 = deck.subList(26, 52).toMutableList()

        // Hand out 5 cards for each player
        val handCard1 = mutableListOf<Card>()
        repeat(5) { handCard1.add(drawOneCard(halfDeck1)) }

        val handCard2 = mutableListOf<Card>()
        repeat(5) { handCard2.add(drawOneCard(halfDeck2)) }

        // Create 2 central piles
        val centerDeck1 = mutableListOf(drawOneCard(halfDeck1))
        val centerDeck2 = mutableListOf(drawOneCard(halfDeck2))

        // Draw deck for player is the remaining
        val drawDeck1 = halfDeck1
        val drawDeck2 = halfDeck2

        // Create players
        val player1 = Player(players[0], handCard1, drawDeck1)
        val player2 = Player(players[1], handCard2, drawDeck2)
        val playerList = listOf(player1, player2)

        // CurrentPlayer: index of the currently active player. By start game player1
        val currentPlayer = playerList.indexOf(player1)

        // Create a game
        val game = UpAndDownGame(
            players = playerList,
            currentPlayer = currentPlayer
        )

        game.lastPass = false
        game.centerDeck1 = centerDeck1
        game.centerDeck2 = centerDeck2

        // Set the current game of the root service to the created game
        rootService.currentGame = game

        // Update GUI
        onAllRefreshables { refreshAfterStartNewGame() }

        // Start turn of the current player. At beginning player1
        startTurn()
    }

    /**
     * Create a shuffled deck of 52 cards
     */
    private fun createDeck(): MutableList<Card> {
        val deck = mutableListOf<Card>() //create an empty deck
        for (suit in CardSuit.entries) {
            for (value in CardValue.entries) {
                deck.add(Card(suit, value))
            }
        }
        deck.shuffle()
        return deck
    }

    /**
     * Remove 1 card from the not empty pile
     */
    private fun drawOneCard(drawPile: MutableList<Card>): Card {
        require(drawPile.isNotEmpty()) { "Draw pile should not be empty" }
        return drawPile.removeFirst()
    }

    /**
     * startTurn: start the turn of current player
     * At start of the game, start game for player1
     */
    fun startTurn() {
        // Get the current game from the root service and check if it is running
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }

        // Update GUI
        onAllRefreshables { refreshAfterTurnStart() }
    }

    /**
     * Switch player:
     * end turn of current player to change to next one
     */
    fun switchPlayer() {
        //get current game and check if it is currently running
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }

        // Calculate the index next player
        val currentPlayer = (game.currentPlayer + 1) % game.players.size

        // Update game to other player
        game.currentPlayer = currentPlayer

        // Update GUI
        onAllRefreshables { refreshAfterChangePlayer() }
    }

    /**
     * Show Winner: declare the winner of the game or a tie
     */
    fun showWinner(): String {
        // Get current game and check if it is currently running
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }
        val actPlayer = game.players[game.currentPlayer]

        // Check if hand card and draw pile is empty
        if (actPlayer.handCards.isEmpty() && actPlayer.drawDeck.isEmpty()) {
            return "Winner is: " + actPlayer.namePlayer
        } else {
            val player1 = game.players[0]
            val player2 = game.players[1]

            // Calculate remaining cards of players
            val player1card = player1.handCards.size + player1.drawDeck.size
            val player2card = player2.handCards.size + player2.drawDeck.size
            val result = when {
                player1card < player2card -> "Winner is: " + player1.namePlayer
                player1card > player2card -> "Winner is: " + player2.namePlayer
                else -> "This is a tie!"
            }
            return result
        }
    }


}