package service

import entity.*

/**
 * PlayerActionService describes all possible actions of a specific player, such as
 * play a card, draw a card, change hand cards, pass
 */
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

    /************************************ PLAY CARD ************************************/

    /**
     * play a card
     * if a player has a valid card on their hand, they may place it face-up onto one of the two central piles
     * @throws IllegalArgumentException if the card is not valid
     */
    fun playCard(card: Card) {
        // Get current game and check if it is currently running
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }
        val actPlayer = game.players[game.currentPlayer]

        // Check if the selected card is valid for either center deck
        require(canPlayCard(card)) { "Selected card is not valid!" }

        // Remove card to play from hand
        actPlayer.handCards.remove(card)

        // Add the valid card on top of center deck 1 or center deck 2
        when {
            isValidCard(card, game.centerDeck1) -> {
                // Because the center deck card is faced up -> put it in the last of the list
                game.centerDeck1.add(game.centerDeck1.size, card)
            }

            isValidCard(card, game.centerDeck2) -> {
                // Because the center deck card is faced up -> put it in the last of the list
                game.centerDeck2.add(game.centerDeck2.size, card)
            }
        }
        game.lastPass = false

        // Update GUI
        onAllRefreshables { refreshAfterPlayCard(card) }
        rootService.gameService.switchPlayer()
    }

    /**
     * Checks if the player meets the requirements to play a card
     */
    fun canPlayCard(card: Card): Boolean {
        // Get current game and check if it is currently running
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }
        val actPlayer = game.players[game.currentPlayer]

        require(card in actPlayer.handCards)
        return isValidCard(card, game.centerDeck1) || isValidCard(card, game.centerDeck2)
    }

    /**
     * Check if the card to play is valid with respect to the rule of the game
     */
    fun isValidCard(card: Card, centerDeck: MutableList<Card>): Boolean {

        val cardValues = CardValue.entries //Ordered Enum values
        val topPileCard = centerDeck.last()
        val topPileCardIndex = cardValues.indexOf(topPileCard.value)
        val cardIndex = cardValues.indexOf(card.value)

        // 1 level
        val oneLevel = setOf(
            (topPileCardIndex + 1) % cardValues.size, //1 level up
            (topPileCardIndex - 1 + cardValues.size) % cardValues.size //1 level down and handles negative numbers
        )

        // 2 levels
        val twoLevels = if (card.suit == topPileCard.suit) {
            setOf(
                (topPileCardIndex + 2) % cardValues.size, //2 levels up
                (topPileCardIndex - 2 + cardValues.size) % cardValues.size //2 levels down and handles negative numbers
            )
        } else {
            emptySet()
        }

        // Check if the cardIndex matches a valid move
        return cardIndex in (oneLevel + twoLevels)
    }


    /************************************ DRAW CARD ************************************/
    /**
     * Check if player has at least 9 hand cards and his own draw pile is not empty
     */
    fun canDrawCard(): Boolean {
        // Get current game and check if it is currently running
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }
        val actPlayer = game.players[game.currentPlayer]

        return (actPlayer.handCards.size < 9 && actPlayer.drawDeck.isNotEmpty())
    }

    /**
     * Draw a card: player draws one card from his own draw pile
     */
    fun drawCard() {
        // Get current game and check if it is currently running
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }
        val actPlayer = game.players[game.currentPlayer]

        require(canDrawCard()) { "Player does not meet the conditions to draw card!" }

        // Get the top card of the draw stack and add it to the current player hand
        val drawnCard = actPlayer.drawDeck.removeFirst()
        actPlayer.handCards.add(drawnCard)

        // Update GUI
        onAllRefreshables {
            refreshAfterDrawCard(drawnCard)
        }
        rootService.gameService.switchPlayer()
    }

    /************************************ REDRAW HAND CARD ************************************/

    /**
     * Replace hand cards
     * player may have all their hand cards put back onto his draw pile,
     * then get 5 new hand cards from the shuffled draw pile
     */
    fun redrawHand() {
        // Get current game and check if it is currently running
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }
        val actPlayer = game.players[game.currentPlayer]

        require(canRedrawHand()) { "Player does not meet the conditions to redraw hand!" }

        // Player puts all hand cards back onto his own draw pile, then shuffle the draw pile
        actPlayer.drawDeck.addAll(actPlayer.handCards)
        actPlayer.handCards.clear()
        actPlayer.drawDeck.shuffle()

        // Draw 5 new cards from the shuffled draw pile
        repeat(5) {
            val drawnCard = actPlayer.drawDeck.removeFirst()
            actPlayer.handCards.add(drawnCard)
        }

        // Last action was not pass
        game.lastPass = false

        // Update GUI
        onAllRefreshables { refreshAfterRedrawHand() }
        rootService.gameService.switchPlayer()
    }

    /**
     * Check if player has at least 8 hand cards and his own draw pile is not empty
     */
    fun canRedrawHand(): Boolean {
        // Get current game and check if it is currently running
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }
        val actPlayer = game.players[game.currentPlayer]

        return (actPlayer.handCards.size > 7 && actPlayer.drawDeck.isNotEmpty())
    }

    /************************************ PASS ************************************/

    /**
     * Check if a player can pass: can not draw a card, play a card or redraw hand cards
     */
    fun canPass(): Boolean {
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }
        val actPlayer = game.players[game.currentPlayer]

        // Check if the player can play at least one card
        val canPlayAtLeastOneCard = actPlayer.handCards.any { card ->
            isValidCard(card, game.centerDeck1) || isValidCard(card, game.centerDeck2)
        }
        return !(canPlayAtLeastOneCard || canDrawCard() || canRedrawHand())
    }

    /**
     * Pass
     * if both players pass consecutively, the game ends and calculates the result
     */
    fun pass() {
        // Get current game and check if it is currently running
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }

        require(canPass()) { "Player does not meet the conditions to pass!" }
        if (game.lastPass) {
            rootService.gameService.showWinner()
            onAllRefreshables { refreshAfterShowWinner() }
        } else {
            game.lastPass = true
            rootService.gameService.switchPlayer()
        }
    }

}


