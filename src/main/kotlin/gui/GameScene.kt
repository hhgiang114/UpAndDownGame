package gui

import entity.*
import service.RootService
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

/**
 * Game Scene display the game board and all game components
 * @param rootService The root service to which this scene belongs
 */

class GameScene(private val rootService: RootService) :
    BoardGameScene(1920, 1080, background = ColorVisual(Color(0xc3e9f8))), Refreshable {

    // Map Card objects to CardView objects
    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    private val cardImageLoader = CardImageLoader()

    // Overlay the game scene to inform the turn of which player
    private val overlayPane = Pane<ComponentView>(
        posX = 0, posY = 0,
        width = 1920, height = 1080,
        visual = ColorVisual(Color(12, 32, 39, 240))
    ).apply {
        isVisible = false
    }

    // Display name and face up hand card of current player
    private val playerHand = LinearLayout<CardView>(
        posX = 300, posY = 800,
        width = 1320, height = 200,
        alignment = Alignment.CENTER,
        spacing = -60
    )

    private val playerName = Label(
        width = 500, height = 150,
        posX = 80, posY = 100,
        text = "Player 1",
        visual = ColorVisual(Color(0xE6F3FA)),
        font = Font(40, Color(0x000000))
    )

    //Display the face down stack hand card and name of opponent
    private val opponentHand = LinearLayout<CardView>(
        width = 1320, height = 200,
        posX = 1055, posY = 470,
        alignment = Alignment.CENTER,
        spacing = -90
    ).apply {
        rotation = 90.0
    }

    private val opponentName = Label(
        width = 500, height = 150,
        posX = 1370, posY = 100,
        text = "Player 2",
        visual = ColorVisual(Color(0xE6F3FA)),
        font = Font(40, Color(0x000000))
    )

    // Display the center deck. The already played cards faced up on top
    private val centerStack1 = CardStack<CardView>(
        width = 130, height = 200,
        posX = 800, posY = 204,
        alignment = Alignment.CENTER
    )

    private val centerStack2 = CardStack<CardView>(
        width = 130, height = 200,
        posX = 990, posY = 204,
        alignment = Alignment.CENTER
    )

    // Display the face down draw stack of players
    private val drawStack1 = CardView(
        width = 130, height = 200,
        posX = 450, posY = 430,
        front = cardImageLoader.backImage
    )

    private val drawStack2 = CardView(
        width = 130, height = 200,
        posX = 1370, posY = 430,
        front = cardImageLoader.backImage
    )

    // Display the number of card in draw stack
    private val drawStackCount1 = Label(
        width = 130, height = 50,
        posX = 450, posY = 380,
        text = "",
        alignment = Alignment.CENTER,
        font = Font(30, Color(0x000000))
    )

    private val drawStackCount2 = Label(
        width = 130, height = 50,
        posX = 1370, posY = 380,
        text = "",
        alignment = Alignment.CENTER,
        font = Font(30, Color(0x000000))
    )

    // Button when player wants to draw a card, enabled when player is allowed to redraw, otherwise disabled
    private val drawButton = Button(
        text = "DRAW",
        width = 250, height = 50,
        posX = 830, posY = 600,
        font = Font(25, Color(0xFFFFFFF)),
        visual = ColorVisual(Color(0x75A8C7))
    ).apply {
        onMouseClicked = {
            if (rootService.playerActionService.canDrawCard()) {
                rootService.playerActionService.drawCard()
            }
        }
    }

    // Button when player wants to replace his hand cards, enabled when player is allowed to redraw, otherwise disabled
    private val redrawButton = Button(
        width = 250, height = 50,
        posX = 830, posY = 720,
        text = "REDRAW",
        font = Font(25, Color(0xFFFFFFF)),
        visual = ColorVisual(Color(0x75A8C7))
    ).apply {
        onMouseClicked = {
            if (rootService.playerActionService.canRedrawHand()) {
                rootService.playerActionService.redrawHand()
            }
        }
    }

    // Button when other actions no possible enabled when player is allowed to pass, otherwise disabled
    private val passButton = Button(
        width = 250, height = 50,
        posX = 830, posY = 660,
        text = "PASS",
        font = Font(25, Color(0xFFFFFFF)),
        visual = ColorVisual(Color(0x75A8C7))
    ).apply {
        onMouseClicked = {
            if (rootService.playerActionService.canPass()) {
                rootService.playerActionService.pass()
            }
        }
    }

    // Area for dragging and dropping cards from player's hand onto the center deck 1
    private val dropArea1 = Area<CardView>(
        posX = 725, posY = 100,
        width = 240, height = 388
    ).apply {
        // A dropAcceptor function checks if the dragged card is valid to be played
        // and consumes the dragged element if true
        dropAcceptor = { dragEvent ->
            when (dragEvent.draggedComponent) {
                // If the dragged component is a CardView, the dropAcceptor checks if the card is valid
                is CardView -> {
                    val card = cardMap.backward(dragEvent.draggedComponent as CardView)
                    val game = checkNotNull(rootService.currentGame)
                    rootService.playerActionService.isValidCard(card, game.centerDeck1)
                }
                else -> false
            }
        }

        // The onDragDropped plays the valid card
        onDragDropped = { event ->
            val cardView = event.draggedComponent as CardView
            val card = cardMap.backward(cardView)

            // Play the card
            rootService.playerActionService.playCard(card)
        }
    }

    // Area for dragging and dropping cards from player's hand onto the center deck 1
    private val dropArea2 = Area<CardView>(
        posX = 965, posY = 100,
        width = 240, height = 388,
    ).apply {
        // A dropAcceptor function checks if the dragged card is valid
        // and consumes the dragged element if true
        dropAcceptor = { dragEvent ->
            when (dragEvent.draggedComponent) {
                // If the dragged component is a CardView, the dropAcceptor checks if the card is valid
                is CardView -> {
                    val card = cardMap.backward(dragEvent.draggedComponent as CardView)
                    val game = checkNotNull(rootService.currentGame)
                    rootService.playerActionService.isValidCard(card, game.centerDeck2)
                }
                else -> false
            }
        }

        // The onDragDropped event handler plays the card if it is valid
        onDragDropped = { event ->
            val cardView = event.draggedComponent as CardView
            val card = cardMap.backward(cardView)

            // Play the card
            rootService.playerActionService.playCard(card)
        }
    }

    // Initialize the scene and all components to it
    init {
        // Add all components to the scene
        addComponents(
            playerHand, playerName,
            opponentHand, opponentName,
            centerStack1, centerStack2,
            drawStack1, drawStack2,
            overlayPane,
            dropArea1, dropArea2,
            drawStackCount1, drawStackCount2,
            drawButton, passButton, redrawButton
        )
    }

    /**
     * The refreshAfterGameStart method is called by the service layer
     * after a new game has been started.
     */
    override fun refreshAfterStartNewGame() {
        // Get current game and check if it is currently running
        val game = rootService.currentGame ?: return

        // Initializes the CardView objects for all cards in the deck
        // and adds them to the BidirectionalMap "cards"
        cardMap.clear()
        CardValue.entries.forEach { value ->
            CardSuit.entries.forEach { suit ->
                cardMap[Card(suit, value)] = CardView(
                    posX = 0,
                    posY = 0,
                    width = 130,
                    height = 200,
                    front = cardImageLoader.frontImageFor(suit, value),
                    back = cardImageLoader.backImage
                )
            }
        }

        // Clear the center stack and add the top card of center deck to it
        centerStack1.clear()
        centerStack2.clear()

        centerStack1.add(cardMap[game.centerDeck1.last()] as CardView)
        centerStack2.add(cardMap[game.centerDeck2.last()] as CardView)

        // Show the face up card of the center stack
        centerStack1.peek().showFront()
        centerStack2.peek().showFront()
    }

    /**
     * The refreshAfterTurnStart is called by the service layer after a new turn has started
     */
    override fun refreshAfterTurnStart() {
        // Get the current game from the rootService and return if no game is currently active
        val game = rootService.currentGame ?: return

        // Clear all hand
        playerHand.clear()
        opponentHand.clear()

        val player = game.players[game.currentPlayer]
        // Set the playerName to the name of the current player
        playerName.text = player.namePlayer + " is playing"

        // Add all cards of the current player's hand to the playerHand
        player.handCards.forEach { card ->
            val cardView = cardMap[card] as CardView
            cardView.showFront()
            cardView.isDraggable = true
            playerHand.add(cardView)
        }

        // Get the opponent
        val otherPlayer = game.players[1 - game.currentPlayer]
        opponentName.text = otherPlayer.namePlayer + " waits here"

        // Add all cards of the other player's hand to the opponentHand
        otherPlayer.handCards.forEach { card ->
            val cardView = cardMap[card] as CardView
            cardView.showBack()
            cardView.isDraggable = false
            opponentHand.add(cardView)
        }

        // Update the drawStackCount to display the number of cards in the draw stack
        drawStackCount1.text = player.drawDeck.size.toString() + "/20"
        drawStackCount2.text = otherPlayer.drawDeck.size.toString() + "/20"

        drawButton.isVisible = true
        redrawButton.isVisible = true
        passButton.isVisible = true
    }

    /**
     * The refreshAfterTurnStart is called by the service layer after current player plays a card
     */
    override fun refreshAfterPlayCard(card: Card) {
        // Get the current game from the rootService and return if no game is currently active
        val game = rootService.currentGame ?: return

        val cardView = (cardMap[card] as CardView).apply {
            onMouseEntered = null
            onMouseExited = null
            onMouseClicked = null
            rotation = (-10..10).random().toDouble()
            isDraggable = false
            showFront()
        }

        playerHand.remove(cardView)

        when (card) {
            game.centerDeck1.last() -> centerStack1.push(cardView)
            game.centerDeck2.last() -> centerStack2.push(cardView)
        }
    }

    /**
     * The refreshAfterDrawCard is called by the service layer after current player draws a card
     */
    override fun refreshAfterDrawCard(card: Card) {
        // Get the current game from the rootService and return if no game is currently active
        val game = rootService.currentGame ?: return

        // Look up the corresponding CardView in the BidirectionalMap "cards"
        val cardView = cardMap[card] as CardView
        cardView.showFront()

        // Add the card to the playerHand
        playerHand.add(cardView)

        // Update the drawStackCount to display the number of cards in the draw stack
        val actPlayer = game.players[game.currentPlayer]
        drawStackCount1.text = actPlayer.drawDeck.size.toString() + "/20"
    }

    override fun refreshAfterRedrawHand() {
        val game = rootService.currentGame ?: return
        val actPlayer = game.players[game.currentPlayer]

        // Look up the corresponding CardView in the BidirectionalMap "cards"
        playerHand.clear()
        actPlayer.handCards.forEach { card ->
            val cardView = cardMap[card] as CardView
            cardView.showFront()
            cardView.isDraggable = true
            playerHand.add(cardView)
        }
        drawStackCount1.text = actPlayer.drawDeck.size.toString()
    }

    /**
     * The refreshAfterChangePlayer informs it is player's turn. Click READY button to begin to play
     */
    override fun refreshAfterChangePlayer() {
        val game = rootService.currentGame ?: return
        val actPlayer = game.players[game.currentPlayer]

        drawButton.isVisible = false
        redrawButton.isVisible = false
        passButton.isVisible = false

        // Clear the overlayPane from possible previous components and make it visible
        overlayPane.clear()
        overlayPane.isVisible = true

        // Add a label and a button to the overlayPane
        overlayPane.add(
            Label(
                width = 798, height = 122,
                posX = 561, posY = 479,
                text = "It's " + actPlayer.namePlayer + " turn",
                alignment = Alignment.CENTER,
                font = Font(100, Color(0xE7EFF2))
            )
        )
        overlayPane.add(
            Button(
                posX = 835, posY = 637,
                width = 250, height = 90,
                text = "READY",
                font = Font(50, Color(0xFFFFFFF)),
                visual = ColorVisual(Color(0x75A8C7))
            ).apply {
                onMouseClicked = {
                    overlayPane.isVisible = false
                    rootService.gameService.startTurn()
                }
            }
        )
    }

}

