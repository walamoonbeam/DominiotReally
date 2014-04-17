package main;

public class WitchCard extends ActionCard {

  public WitchCard(String name, String description, int cost) {
    super(Card.CARD_NAME_WITCH, "+2 Cards, Each other player gains a Curse card.", 5);
    this.numCardsDraw = 2;
  }
  
  @Override
  public void performAction(GameContext context) {
    super.performAction(context);
    context.curseOpponent();
  }

}
