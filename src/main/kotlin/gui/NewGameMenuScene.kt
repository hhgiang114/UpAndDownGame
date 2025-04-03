package gui

import service.RootService
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color
import kotlin.system.exitProcess

/**
 * The start scene of the game.
 * Show up when opening game application, or after one finished game and the player clicks restart
 * @param rootService The root service to which this scene belongs
 */

class NewGameMenuScene(private val rootService: RootService) :
    MenuScene(1920, 1080, background = ColorVisual(Color(0xc3e9f8))), Refreshable {

    // Display the name of the game
    private val startScene = Label(
        width = 800, height = 150, posX = 560, posY = 100,
        text = "Up And Down Game",
        alignment = Alignment.CENTER,
        font = Font(size = 70)
    )

    // Players enter their name here
    private val player1Input = TextField(
        prompt = "Name of player 1",
        width = 800, height = 130, posX = 560, posY = 325,
        font = Font(size = 30),
    )

    private val player2Input = TextField(
        prompt = "Name of player 2",
        width = 800, height = 130, posX = 560, posY = 540,
        font = Font(size = 30)
    )

    private val playerList = mutableListOf(player1Input, player2Input)

    // Button to start the game
    val startButton = Button(
        text = "START",
        width = 350, height = 130, posX = 1010, posY = 840,
        font = Font(size = 40)
    ).apply {
        // When clicked, the game is started with the entered player names
        onMouseClicked = {
            val playerNames = playerList.filter { it.text.isNotBlank() }.map { it.text }
            if (playerNames.size == 2) {
                rootService.gameService.startNewGame(playerNames)
            }
        }
    }

    // Button to quit the game
    val quitButton = Button(
        text = "QUIT",
        width = 350, height = 130, posX = 560, posY = 840,
        font = Font(size = 40)
    ).apply {
        onMouseClicked = {
            exitProcess(0)
        }
    }

    // Initialize the scene by adding all components to the screen
    init {
        addComponents(
            startScene,
            player1Input, player2Input,
            startButton, quitButton
        )
    }

}


