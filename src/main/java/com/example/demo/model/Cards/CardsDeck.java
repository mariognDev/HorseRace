package com.example.demo.model.Cards;
import java.util.ArrayList;
import java.util.LinkedList;


public class CardsDeck {

    private final LinkedList<Card> cardsDeck = new LinkedList<>();
    private final int[] num = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    private final CardSuit[] cardSuits = {CardSuit.GOLD, CardSuit.CLUBS, CardSuit.CUPS, CardSuit.SWORDS};
    private final CardFace[] cardFaces = {CardFace.JACK, CardFace.KNIGHT, CardFace.KING};
    private Card card;
    private final ArrayList<Integer> numCartes;

    /**
     * Method to create the cards deck every time you want to play
     * Create Cards from two vectors (number and suit) and add them to the deck ArrayList
     */
    public CardsDeck() {
        for (int i = 0; i < num.length; i++) {
            for (int j = 0; j < cardSuits.length; j++) {
                card = new NumeredCard(num[i], cardSuits[j]);
                cardsDeck.add(card);
            }
        }
        for (CardFace face : cardFaces) {
            for (int j = 0; j < cardSuits.length; j++) {
                card = new FacedCard(face, cardSuits[j]);
                cardsDeck.add(card);
            }
        }

        numCartes = new ArrayList<>();
        // display deck
        /*for (Card i: cardsDeck){
            System.out.println(i);
        }*/

    }

    /**
     * Method to make relation between numCarta(int) until cardsDeck(Card)
     *
     * @return Card that isn`t repeated
     */
    public Card getCardFromDeck() {
        Card cartadonada;
        int numCarta = comprovarNumCartes();

        cartadonada = cardsDeck.get(numCarta);
        return cartadonada;
    }

    /**
     * Helper method that returns a random number whose not exist in numCartes and was added
     *
     * @return a number that was added in numCartes
     */
    private int comprovarNumCartes() {
        boolean trobada;
        int numCarta;
        do {
            trobada = false;
            numCarta = (int) (Math.random() * 40);
            if (numCartes.size() > 39) {
                numCartes.clear();


            } else {
                if (!numCartes.isEmpty())
                    if (numCartes.contains(numCarta))
                        trobada = true;
            }
        } while (trobada);
        numCartes.add(numCarta);
        return numCarta;
    }
}
