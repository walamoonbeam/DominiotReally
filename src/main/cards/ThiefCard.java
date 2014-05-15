package main.cards;

import main.GameContext;

public class ThiefCard extends ActionCard {

  public ThiefCard() {
    super(
        Card.CARD_NAME_THIEF,
        "Each other player reveals the top 2 cards of his deck. If they revealed any Treasure cards, they trash one of them that you choose. You may gain any or all of these trashed cards. They discard the other revealed cards.",
        0);
  }

  public void performAction(GameContext context) {
    super.performAction(context);

    if (context.shouldPerformMaliciousActions()) {
      Card card0 = context.getTurnController().getOpponent().getPlayerDeck().getDrawDeck().get(0);
      Card card1 = context.getTurnController().getOpponent().getPlayerDeck().getDrawDeck().get(1);
      if (card0.getType() == Card.CARD_TYPE_TREASURECARD
          || card1.getType() == Card.CARD_TYPE_TREASURECARD) {
        boolean choseATreasureCard = false;
        while (choseATreasureCard != true) {
          int indexToTrash = context.decideCardInOpponentDeck("Choose a TREASURE card to trash", 2);
          Card cardToTrash =
              context.getTurnController().getOpponent().getPlayerDeck().getDrawDeck()
                  .get(indexToTrash);
          if (cardToTrash.getType() == Card.CARD_TYPE_TREASURECARD) {
            boolean keep = context.decideBoolean("Do you want to keep that card?");
            if (keep) {
              context.getTurnController().getPlayer().getPlayerDeck().addCard(cardToTrash);
            }
            context.getTurnController().getOpponent().getPlayerDeck()
                .trashCardInDeckAtIndex(indexToTrash);
            context.getTurnController().getOpponent().getPlayerDeck().discardCardInDeckAtIndex(0);
            choseATreasureCard = true;
          }
        }
      }
    }
  }
}
