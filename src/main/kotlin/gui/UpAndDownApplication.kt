package gui

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.util.Font


/**
 * Main class of the Up and Down application
 * @property rootService connects all scenes of the game
 * @property newGameMenuScene shows game name and player name
 * @property gameScene shows game itself
 * @property resultMenuScene end scene shows results and allows to restart
 */
class UpAndDownApplication : BoardGameApplication("Up and Down Game"), Refreshable {

    // Central service from which all others are created/accessed
    // Create the game and scenes and pass them the root service
    private val rootService = RootService()
    private val newGameMenuScene = NewGameMenuScene(rootService)
    private val gameScene = GameScene(rootService)

    /**
     * This menu scene is show after each finished game
     */
    private val resultMenuScene = ResultMenuScene(rootService)
    // Initialize the application by adding refreshables and setting the initial scene
    init {
        loadFont("MedodicaRegular.otf", "Medodica Regular", Font.FontWeight.NORMAL)
        // Register refreshables for the application and every scene
        rootService.addRefreshables(
            this, newGameMenuScene, gameScene, resultMenuScene
        )
        // Set the initial scene to the main menu
        this.showGameScene(gameScene)
        this.showMenuScene(newGameMenuScene,0)
    }

    /** After game was started hides menu scene */
    override fun refreshAfterStartNewGame() {
        this.hideMenuScene(500)
    }

    /** After game was ended shows result scene */
    override fun refreshAfterShowWinner() {
        this.showMenuScene(resultMenuScene)
    }

    /** After the result scene and back to the main start new game scene */
    override fun refreshAfterRestartNewGame() {
        this.showMenuScene(newGameMenuScene)
    }

}



