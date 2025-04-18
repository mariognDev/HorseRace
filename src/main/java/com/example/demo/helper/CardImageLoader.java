package com.example.demo.helper;

import com.example.demo.model.Cards.Card;
import javafx.scene.image.Image;

import java.net.URL;

public class CardImageLoader {

    /**
     * Method load an image of Object card and his path
     *
     * @param card      to found and show his image from file
     * @param imagePath String with image path
     * @return a Object image
     */
    public static Image loadCardImage(Card card, String imagePath) {

        String description = card.getDescription();

        String fileName = description.replace(" ", "_") + ".png";

        String fullPath = imagePath + "/" + fileName;

        URL resource = CardImageLoader.class.getResource(fullPath);

        if (resource != null) {
            return new Image(resource.toExternalForm());
        } else {
            throw new IllegalArgumentException("Card image " + description + " not found");

        }


    }

}

