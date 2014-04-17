package main;

import java.util.List;

import main.SupplyDeck.CardTuple;

public class TurnController {

  private Player player;
  private Player opponent;
  private SupplyDeck supplyDeck;
  private GameContext currentContext;

  public TurnController(Player player, Player opponent, SupplyDeck supplyDeck,
      GameContext.DecisionDelegate decisionDelegate) {
    this.player = player;
    this.opponent = opponent;
    this.supplyDeck = supplyDeck;

    this.currentContext = new GameContext(this);
    this.currentContext.setDecisionDelegate(decisionDelegate);
  }

  public Player getPlayer() {
    return this.player;
  }
  
  public GameContext getCurrentContext() {
    return this.currentContext;
  }

  private boolean canBuyCardAtIndexInRoster(int index, List<CardTuple> roster) {
    return (this.getCurrentContext().getTreasureCount() >= roster.get(index).getCard().getCost() && this.getCurrentContext().getBuyCount() > 0);
  }

  private boolean tryPurchaseCardHelper(Card card) {
    if (card == null)
      return false;

    this.getCurrentContext().adjustBuyCountByDelta(-1);
    this.getCurrentContext().adjustTreasureCountByDelta(-card.getCost());
    this.getCurrentContext().invalidateLumpSumTreasure();
    
    player.getPlayerDeck().addCard(card);
    return true;
  }

  public boolean tryPurchaseActionCardAtIndex(int index) {
    if (!canBuyCardAtIndexInRoster(index, supplyDeck.getActionCardRoster()))
      return false;

    return tryPurchaseCardHelper(supplyDeck.buyActionCardAtIndex(index));
  }

  public boolean tryPurchaseResourceCardAtIndex(int index) {
    if (!canBuyCardAtIndexInRoster(index, supplyDeck.getResourceCardRoster()))
      return false;

    return tryPurchaseCardHelper(supplyDeck.buyResourceCardAtIndex(index));
  }

  public boolean tryForceInsertResourceCardIntoHand(int index) {
    return tryForceInsertResourceCardIntoHand(this.player, index);
  }
  
  private boolean tryForceInsertResourceCardIntoHand(Player destinationPlayer, int index) {
    Card cardToBuy = supplyDeck.buyResourceCardAtIndex(index);
    if (cardToBuy == null) {
      return false;
    } else {
      destinationPlayer.getPlayerDeck().insertCardIntoHand(cardToBuy);
      return true;
    }
  }

  /**
   * Check if there are still actions left, get card at `index` from player's hand, perform action
   * of card on context, decrement context values correctly.
   * 
   * @param index
   * @return did playing card succeed
   */
  public boolean tryPlayingCardAtIndex(int index) {
    PlayerDeck deck = this.player.getPlayerDeck();
    if (index >= deck.getHand().size() || this.currentContext.getActionCount() <= 0)
      return false;
    
    GameContext context = new GameContext(this.currentContext);
    Card selectedCard = deck.getHand().get(index);
    selectedCard.performAction(context);
    context.adjustActionCountByDelta(-1);
    
    if (context.getShouldTrashCurrentCard())
      deck.trashCardInHandAtIndex(index);
    else
      deck.discardCardInHandAtIndex(index);
    
    this.currentContext = context;
    return true;
  }

  /**
   * Does no checks if you can play the card, and it does not decrement any action counters. This is
   * for special action cards where this does not matter. Does not remove the card either
   * 
   * @param index
   * @return did playing card succeed
   */
  public void forcePlayingCardAtIndex(int index) {
    Card selectedCard = this.player.getPlayerDeck().getHand().get(index);
    selectedCard.performAction(this.currentContext);
  }
  
  public boolean curseOpponent() {
    int curseCardIndex = -1;
    Card curseCard = Card.makeCard(Card.CARD_NAME_CURSE);
    List<CardTuple> roster = this.supplyDeck.getResourceCardRoster();
    
    for (int idx = 0; idx < roster.size(); ++idx) {
      if (roster.get(idx).getCard().equals(curseCard)) {
        curseCardIndex = idx;
        break;
      }
    }
    
    if (curseCardIndex < 0)
      return false;
      
    return tryForceInsertResourceCardIntoHand(this.opponent, curseCardIndex);
  }

}
