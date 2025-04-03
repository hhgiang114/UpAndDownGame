package gui

import entity.CardSuit
import entity.CardValue
import tools.aqua.bgw.visual.ImageVisual

/**
 * The full raster image containing the suits as rows (plus one special row for blank/back)
 * and values as columns (starting with the ace). As the ordering in the image is not the same
 * as the order in which the suits are declared in CardSuit, mappings via row and column
 * are required.
 */
const val CARDS_FILE = "card_deck.png"

const val IMG_HEIGHT = 196
const val IMG_WIDTH = 140

/**
 * Provides access to the src/main/resources/card_deck.png file that contains all card images
 * in a raster. The returned ImageVisual objects of frontImageFor, blankImage,
 * and backImage are 130x200 pixels.
 */
class CardImageLoader {

    /**
     * Provides the card image for the given CardSuit and CardValue
     */
    fun frontImageFor(suit: CardSuit, value: CardValue) =
        getImageByCoordinates(value.column, suit.row)

    private val CARDS_FILE = "card_deck.png"

//    /**
//     * Provides a blank (white) card
//     */
//    val blankImage: ImageVisual get() = getImageByCoordinates(0, 4)

    /**
     * Provides the back side image of the card deck
     */
    //val backImage: ImageVisual get() = getImageByCoordinates(2, 4)
    val backImage = ImageVisual("card_back.jpg")

    /**
     * retrieves from the full raster image CARDS_FILE the corresponding sub-image
     * for the given column [x] and row [y]
     *
     * @param x column in the raster image, starting at 0
     * @param y row in the raster image, starting at 0
     *
     */
    private fun getImageByCoordinates(x: Int, y: Int) = ImageVisual(
        CARDS_FILE,
        IMG_WIDTH,
        IMG_HEIGHT,
        x * IMG_WIDTH,
        y * IMG_HEIGHT,
    )
}

/**
 * As the CARDS_FILE does not have the same ordering of suits
 * as they are in CardSuit, this extension property provides
 * a corresponding mapping to be used when addressing the row.
 *
 */
val CardSuit.row
    get() = when (this) {
        CardSuit.SPADES -> 0
        CardSuit.HEARTS -> 1
        CardSuit.CLUBS -> 2
        CardSuit.DIAMONDS -> 3
    }

/**
 * As the CARDS_FILE does not have the same ordering of values
 * as they are in CardValue, this extension property provides
 * a corresponding mapping to be used when addressing the column.
 */
val CardValue.column
    get() = when (this) {
        CardValue.TWO -> 0
        CardValue.THREE -> 1
        CardValue.FOUR -> 2
        CardValue.FIVE -> 3
        CardValue.SIX -> 4
        CardValue.SEVEN -> 5
        CardValue.EIGHT -> 6
        CardValue.NINE -> 7
        CardValue.TEN -> 8
        CardValue.JACK -> 9
        CardValue.QUEEN -> 10
        CardValue.KING -> 11
        CardValue.ACE -> 12
    }
