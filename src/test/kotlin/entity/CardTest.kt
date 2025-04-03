package entity

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for class [Card]
 */
class CardTest {
    /**
    * Check if the suit and the value are correctly set when creating a new card
    */
    @BeforeTest
    fun testCardValid(){
        //When creating a card
        val card = Card(CardSuit.HEARTS, CardValue.ACE)

        //Then the suit and value should be correctly assigned
        assertEquals(CardSuit.HEARTS, card.suit)
        assertEquals(CardValue.ACE, card.value)
    }

    /**
     * Check if the string returned by toString() contains the correct suit and value of a card
     */
    @Test
    fun `test Card toString representation`() {
        //Given a card
        val card = Card(CardSuit.SPADES, CardValue.KING)

        //should return the correct string representation
        assertEquals("♠K", card.toString())
    }

}