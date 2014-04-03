package test;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import main.Card;
import main.TreasureCard;

import org.junit.Test;

public class CardTest {
  private void testFactoryForCardName(String name) {
    Card card = Card.makeCard(name);
    assertNotNull(card);
    assertEquals(name, card.getName());
  }

  @Test
  public void testFactoryForFestivalCard() {
    testFactoryForCardName(Card.CARD_NAME_FESTIVAL);
  }

  @Test
  public void testFactoryForLaboratory() {
    testFactoryForCardName(Card.CARD_NAME_LABORATORY);
  }

  @Test
  public void testFactoryForMarket() {
    testFactoryForCardName(Card.CARD_NAME_MARKET);
  }

  @Test
  public void testFactoryForSmithy() {
    testFactoryForCardName(Card.CARD_NAME_SMITHY);
  }

  @Test
  public void testFactoryForVillage() {
    testFactoryForCardName(Card.CARD_NAME_VILLAGE);
  }

  @Test
  public void testFactoryForWoodcutter() {
    testFactoryForCardName(Card.CARD_NAME_WOODCUTTER);
  }

  @Test
  public void testFactoryForEstate() {
    testFactoryForCardName(Card.CARD_NAME_ESTATE);
  }

  @Test
  public void testFactoryForDuchy() {
    testFactoryForCardName(Card.CARD_NAME_DUCHY);
  }

  @Test
  public void testFactoryForProvince() {
    testFactoryForCardName(Card.CARD_NAME_PROVINCE);
  }

  @Test
  public void testFactoryForCopper() {
    testFactoryForCardName(Card.CARD_NAME_COPPER);
  }

  @Test
  public void testFactoryForSilver() {
    testFactoryForCardName(Card.CARD_NAME_SILVER);
  }

  @Test
  public void testFactoryForGold() {
    testFactoryForCardName(Card.CARD_NAME_GOLD);
  }

  @Test(expected = NoSuchElementException.class)
  public void makeInvalidCard() {
    Card.makeCard("Dutcher");
  }

  @Test
  public void testCloneCard() {
    Card card = Card.makeCard(Card.CARD_NAME_GOLD);
    Card clone = Card.makeCard(card);
    
    assertNotNull(clone);
    assertEquals(card.getClass(), clone.getClass());
    assertEquals(card.getName(), clone.getName());
    assertEquals(((TreasureCard)card).getValue(), ((TreasureCard)clone).getValue());
    assertFalse("Clone object should not be equal to its original", card == clone);
  }
}
