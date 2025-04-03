package gui

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color
import kotlin.system.exitProcess

/**
 * The result menu scene of the game.
 * Show up when opening game application, or after one finished game and the player clicks restart
 * @param rootService The root service to which this scene belongs
 */
class ResultMenuScene(private val rootService: RootService) :
    MenuScene(1920, 1080, background = ColorVisual(Color(0xc3e9f8))), Refreshable {

    // Display scene name
    private val sceneLabel = Label(
        text = "GAME RESULT",
        width = 800, height = 150, posX = 560, posY = 100,
        alignment = Alignment.CENTER,
        font = Font(size = 70)
    )

    // Display final result of the game: winner or a tie
    private val resultLabel = Label(
        text = "",
        width = 500, height = 130, posX = 710, posY = 325,
        alignment = Alignment.CENTER,
        font = Font(size = 70)
    )

    // Button to restart the game
    val restartButton = Button(
        text = "RESTART",
        width = 350, height = 130, posX = 1010, posY = 840,
        font = Font(size = 40)
    ).apply {
        onMouseClicked = {
            rootService.gameService.onAllRefreshables { refreshAfterRestartNewGame() }
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

    // Initialize the scene and all components to it
    init {
        addComponents(sceneLabel, resultLabel, restartButton, quitButton)
    }

    /**
     * The refreshAfterShowWinner method is called by the service layer after a game has ended.
     * It sets the result of the game.
     */
    override fun refreshAfterShowWinner() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        resultLabel.text = rootService.gameService.showWinner()
    }
}