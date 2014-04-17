package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.List;

import main.GameContext;
import main.GameController;
import main.Player;
import main.SupplyDeck;
import main.TurnController;
import main.cards.Card;

import org.junit.Before;
import org.junit.Test;

public class GameControllerTest {
  Player player;
  TurnController turnController;
  SupplyDeck supplyDeck;
  GameContext context;
  GameController gameController;

  @Before
  public void setUp() throws Exception {
    player = new Player("Test Player");
    List<Card> cards =
        Arrays.asList(new Card[] {Card.makeCard(Card.CARD_NAME_FESTIVAL),
            Card.makeCard(Card.CARD_NAME_LABORATORY), Card.makeCard(Card.CARD_NAME_MARKET),
            Card.makeCard(Card.CARD_NAME_SMITHY), Card.makeCard(Card.CARD_NAME_VILLAGE),
            Card.makeCard(Card.CARD_NAME_WOODCUTTER)});
    supplyDeck = new SupplyDeck(cards);
    turnController = new TurnController(player, null, supplyDeck, null);
    context = new GameContext(turnController);
    gameController = new GameController(context.getDecisionDelegate());
  }

  @Test
  public void testTurnChanges() {
    TurnController previousTurn = gameController.getCurrentTurn();
    gameController.endCurrentTurn();
    assertFalse(previousTurn.equals(gameController.getCurrentTurn()));
  }

  @Test
  public void testSupplyDeck_IsSame() {
    SupplyDeck oldSupplyDeck = gameController.getSupplyDeck();
    assertEquals(oldSupplyDeck, gameController.getSupplyDeck());
  }

  @Test
  public void testEndsGameWhenAVictoryCardPileIsDepleted() {
    int providenceCardIndex = 5;

    // This is just to catch if we ever change the order of cards in the resource deck
    assertEquals(Card.makeCard(Card.CARD_NAME_PROVINCE), gameController.getSupplyDeck()
        .getResourceCardRoster().get(providenceCardIndex).getCard());

    // Simulate deplete of supply deck
    while (gameController.getSupplyDeck().buyResourceCardAtIndex(providenceCardIndex) != null);

    assertEquals(0, gameController.getSupplyDeck().getResourceCardRoster().get(providenceCardIndex)
        .getSupply());
    assertFalse(gameController.isActiveGame());
  }

}
