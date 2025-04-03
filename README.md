# Software Practice Project 1 (Individual project)
A simple card game "Up and Down" will be implemented as a Kotlin application using [BoardGameWork](https://github.com/tudo-aqua/bgw).

## Materials and Setup
"Up and Down" is a card game for two players and is played with a standard deck of 52 cards:

{Diamonds, Hearts, Spades, Clubs} × {2, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen, King, Ace}

At the beginning of the game, the game materials are distributed among the two players:

1. Hand Cards: Each player receives five face-down hand cards at the beginning of the game.
2. Draft Pile: The remaining 42 cards are divided into two face-down draw piles (one for each player) of 21 cards each.
3. Central Pile: The top card from each draw pile is placed face up in the center of the table. These two cards form the two face-up central piles.

## Goal of the Game
The goal is to get rid of all cards from your draw pile and all hand cards by placing valid cards on the central piles. A player wins as soon as he has no more cards.

## How to Play
The starting player is randomly chosen. Then, players alternate turns. Each turn, a player has exactly one of the following options:

### Playing a Card

* If the player can play a card, it can be placed face-up from their hand onto one of the two central piles.
* The card must be either one rank higher or lower than the card on top of the chosen central pile. The following order applies: 2 < 3 < 4 < 5 < 6 < 7 < 8 < 9 < 10 < Jack < Queen < King < Ace < 2. The suit (diamonds, hearts, spades, clubs) is irrelevant. 

Example 1: If a 7 of spades is on the pile, a 6 of hearts or an 8 of clubs may be played.
If a card of the same suit (diamonds, hearts, spades, clubs) as the card on top of the chosen central pile is placed, it may be two steps higher or lower. The same order applies as above.

Example 2: If a king of spades is on the pile, a jack of spades or a 2 of spades may also be played (in addition to queens and aces of any suit).
Drawing a Card

* If you have a maximum of 9 cards in your hand, you may draw a card from your draw pile.

### Replacing Hand Cards

If you have at least 8 cards in your hand and the draw pile is not empty, you may shuffle your hand cards into your draw pile and then draw 5 new cards from the newly shuffled draw pile into your hand.

### Passing

If none of the other three actions can be performed, you must pass. You cannot pass voluntarily.

## End of the Game
The game ends when a player has discarded all cards from their draw pile and all cards from their hand. This player wins the game.

The game also ends when both players have consecutively passed. In this case, the player with the fewest cards in their hand wins. In the event of a tie, the game is considered a draw.

## Program Requirements
The program to be developed should control the game flow and ensure compliance with the game rules. Additional features that are not directly based on the game rules should be implemented:

* Names should be configurable for players when the program starts.
* Players take turns selecting their actions on the same screen (hot-seat mode).
* The hidden hand cards of one player should be visible only to that player. Therefore, a "next player" screen is required that covers all cards and only displays the next player's cards after confirmation.
* At the end of the game the winner should be declared.