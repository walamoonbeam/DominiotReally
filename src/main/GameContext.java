package main;

import main.cards.Card;

/**
 * The game context holds the current state of the game. It undergoes a lifecycle enforced by the
 * TurnController whereby the current context is cloned, mutated, validated, and if valid becomes
 * the new current state.
 */
public class GameContext {

  public static interface DecisionDelegate {
    public static final int CARD_IN_HAND_IGNORED = -1;

    public boolean decideBoolean(GameContext context, String question);

    public int decideCardInHand(GameContext context, PlayerDeck deck, String question,
        boolean canIgnore);

    public boolean decideCardInDeck(GameContext context, PlayerDeck deck, String question);

    public int decideCardInDeck(GameContext context, PlayerDeck deck, String question,
        int numberOfCards);
  }

  private int treasureCount;
  private int actionCount;
  private int buyCount;

  private TurnController turnController;
  private DecisionDelegate decisionDelegate;

  private int lumpSumTreasureCount;
  private boolean useLumpSumTreasure;

  /**
   * Creates a new GameContext with all fields default initialized for the first turn of a new game.
   */
  public GameContext(TurnController turnController) {
    this();
    this.turnController = turnController;
  }

  public GameContext() {
    this.treasureCount = 0;
    this.actionCount = 1;
    this.buyCount = 1;
  }

  /**
   * Clone constructor. Creates a new GameContext that is a duplicate of the argument. For possible
   * undoing up a card, which may or may not be implemented.
   */
  public GameContext(GameContext context) {
    this.treasureCount = context.treasureCount;
    this.actionCount = context.actionCount;
    this.buyCount = context.buyCount;
    this.turnController = context.turnController;
    this.decisionDelegate = context.decisionDelegate;
  }

  public int getActionCount() {
    return this.actionCount;
  }

  public int getTreasureCount() {
    return useLumpSumTreasure ? this.lumpSumTreasureCount : this.treasureCount;
  }

  public int getBuyCount() {
    return this.buyCount;
  }

  public void adjustActionCountByDelta(int delta) {
    this.actionCount += delta;
  }

  public void adjustTreasureCountByDelta(int delta) {
    if (!useLumpSumTreasure)
      this.treasureCount += delta;
  }

  public void adjustBuyCountByDelta(int delta) {
    this.buyCount += delta;
  }

  public Player getPlayer() {
    return this.turnController.getPlayer();
  }

  public TurnController getTurnController() {
    return this.turnController;
  }

  public DecisionDelegate getDecisionDelegate() {
    return this.decisionDelegate;
  }

  public void setDecisionDelegate(DecisionDelegate decisionDelegate) {
    this.decisionDelegate = decisionDelegate;
  }

  public boolean decideBoolean(String question) {
    assertValidDecisionDelegate();

    return decisionDelegate.decideBoolean(this, question);
  }

  public int decideCardInHand(String question, boolean canIgnore) {
    assertValidDecisionDelegate();
    return decisionDelegate.decideCardInHand(this, turnController.getPlayer().getPlayerDeck(),
        question, canIgnore);
  }

  public int decideCardInOpponentHand(String question, boolean canIgnore) {
    assertValidDecisionDelegate();
    return decisionDelegate.decideCardInHand(this, turnController.getOpponent().getPlayerDeck(),
        question, canIgnore);
  }

  public void setLumpSumTreasureCount(int count) {
    lumpSumTreasureCount = count;
    useLumpSumTreasure = true;
  }

  public boolean decideCardInOpponentDeck(String question) {
    assertValidDecisionDelegate();
    return this.decisionDelegate.decideCardInDeck(this, turnController.getOpponent()
        .getPlayerDeck(), question);
  }

  public boolean decideCardInOwnDeck(String question) {
    assertValidDecisionDelegate();
    return this.decisionDelegate.decideCardInDeck(this, turnController.getPlayer().getPlayerDeck(),
        question);
  }

  private void assertValidDecisionDelegate() {
    if (decisionDelegate == null)
      throw new RuntimeException("Attempting to make a decision without a decision delegate");
  }

  public int decideCardInOpponentDeck(String question, int numberOfCards) {
    assertValidDecisionDelegate();
    return this.decisionDelegate.decideCardInDeck(this, turnController.getOpponent()
        .getPlayerDeck(), question, numberOfCards);
  }

  public int decideCardInOwnDeck(String question, int numberOfCards) {
    assertValidDecisionDelegate();
    return this.decisionDelegate.decideCardInDeck(this, turnController.getPlayer().getPlayerDeck(),
        question, numberOfCards);
  }

  public void invalidateLumpSumTreasure() {
    lumpSumTreasureCount = 0;
    useLumpSumTreasure = false;
  }

  private boolean trashCurrentCard = false; // TODO: Add test to ensure we do not transfer this to copies

  public void setShouldTrashCurrentCard() {
    trashCurrentCard = true;
  }

  public boolean getShouldTrashCurrentCard() {
    return trashCurrentCard;
  }

  public boolean curseOpponent() {
    return this.turnController.curseOpponent();
  }

  public void opponentDrawNumCards(int num) {
    this.turnController.opponentDrawNumCards(num);
  }

  public boolean shouldPerformMaliciousActions() {
    if (getTurnController().getOpponent().getPlayerDeck().getHand()
        .contains(Card.makeCard(Card.CARD_NAME_MOAT))) {
      return !decideBoolean("Attackee has a moat. Does the attackee want to use moat to nullify attack?");
    }
    return true;
  }

}
