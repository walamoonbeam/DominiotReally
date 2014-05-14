package test;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Test;

public class LocalizationTest {
  
  @Test
  public void testENCardsTranslation(){
    Locale currentLocale = new  Locale("en", "US");
    ResourceBundle messages = ResourceBundle.getBundle("MessageBundle", currentLocale);;
    
    assertEquals(messages.getString("Estate"), "Victory: +1 Victory Point");
    
    assertEquals(messages.getString("Woodcutter"), "Action: +1 Buy, +2 Coins");
    
    assertEquals(messages.getString("Witch"), "Action: +2 Cards, Each other player gains a Curse card.");
    
    assertEquals(messages.getString("Bureaucrat"), "Action: Gain a silver card; put it on top of your deck. Each other player reveals a Victory card from his hand and puts it on his deck (or reveals a hand with no Victory cards).");
  }
  
  @Test
  public void testSPCardsTranslation(){
    Locale currentLocale = new  Locale("sp", "SP");
    ResourceBundle messages = ResourceBundle.getBundle("MessageBundle", currentLocale);;
    
 assertEquals(messages.getString("Estate"), "Victoria: +1 Punto de Victoria");
    
    assertEquals(messages.getString("Woodcutter"), "Acci�n: +1 Compra, +2 Monedas");
    
    assertEquals(messages.getString("Witch"), "Acci�n: +2 Tarjetas. Cada otro jugador gana una tarjeta de maldici�n.");
    
    assertEquals(messages.getString("Bureaucrat"), "Acci�n: Obtenga una tarjeta de plata; lo puso en la parte superior de tu mazo. Cada otro jugador muestra una carta de la victoria de su mano y la coloca en su mazo (o revela una mano sin cartas de Victoria).");
    
    
  }
}
