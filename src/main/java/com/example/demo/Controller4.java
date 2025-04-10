package com.example.demo;

import com.example.demo.helper.CardImageLoader;
import com.example.demo.helper.Database.DatabaseManager;
import com.example.demo.helper.File.FileLogsAccess;
import com.example.demo.helper.Pause;
import com.example.demo.helper.RoundMaxException;
import com.example.demo.model.Cards.Card;
import com.example.demo.model.GameLogic;
import com.example.demo.model.players.Human;
import com.example.demo.model.players.Player;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Controller4 {

    @FXML
    private Label roundNumberLabel;

    @FXML
    private Pane display4;

    @FXML
    private Label actionLabel;

    @FXML
    private ImageView showCard;

    @FXML
    private Label forwardLabel;

    @FXML
    private Label backwardLabel;

    @FXML
    private Button nextButtonDisplay5;

    @FXML
    private Button backToDisplay2Button;
    private ImageView horse ;


    private boolean winner = false;

    protected static String winHorseSuit;

    protected int round ;

    protected int counterException = 0;

    private FileLogsAccess fileAccess;

    private boolean isGameRuning= true;

    /**
     * Method to initialize the display and indentify if the game is restored or is a new game
     */
    @FXML
    public void initialize() {
        if(ControllerRestoreDisplay.getIsRestoring()){
            System.out.println("Restored Game");
            round = ControllerRestoreDisplay.roundNumber;
            gameStart();

        }else{
            round = 1;
            gameStart();
            System.out.println("Non Restored Game");
        }
    }

    /**
     * Method to start the game, with import createCardsDeck from Logic, then star rounds with gameRound
     */
    private void gameStart() {

        fileAccess = new FileLogsAccess();
        fileAccess.deleteJSON();
        GameLogic.createCardsDeck();
        gameRound();


    }


    /**
     * Method for each round in game, first check if winner its true create a .txt with
     * all movements in the game. if its false, get a card from deck with method getCard().
     */
    protected void gameRound() {
        try {
            GameLogic.checkRound(round, counterException);
            if (winner) {
                nextButtonDisplay5.setOpacity(1);
                nextButtonDisplay5.getStyleClass().add("display1Button");
                nextButtonDisplay5.setStyle("-fx-cursor: hand; -fx-background-color: #CE9D0A; -fx-background-radius: 50px;" );
                return;
            }

            try {
                getCard();
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: not found image");

            }

        } catch (RoundMaxException e) {
            showAlertAndPause(e.getMessage());

        }

    }

    /**
     * Method to get a card from deck show corresponding image from BARAJA, and with methods from helper.Pause
     * to make pause with all interactions in this display.
     */
    private void getCard() {

        Card card = GameLogic.getCardsDeck().getCardFromDeck();
        String imagePath = "/com/example/demo/images/BARAJA";
        Image cardImage = CardImageLoader.loadCardImage(card, imagePath);
        Pause.updateLabelWithPause(roundNumberLabel, String.valueOf(round), 1, () -> {
            //encender un label u otro
            if (round % 5 == 0) {
                forwardLabel.setOpacity(0);
                backwardLabel.setOpacity(1);
            } else {
                backwardLabel.setOpacity(0);
                forwardLabel.setOpacity(1);
            }
            Pause.updateImageWithPause(showCard, cardImage, 1, () -> {
                Pause.updateLabelWithPause(actionLabel, "Card Taken : " + card.getDescription(), 1, () -> {
                    updateHorsePosition(card);
                    if(isGameRuning){
                        round++;
                        gameRound();
                    }else{
                        System.out.println("game Stop");

                    }

                });
            });

        });
    }

    /**
     * Method to create a window to explain the deck is empty. if you want to play more to shuffling again or
     * you want to exit, to display1 ( menu)
     *
     * @param message custom to communicate user something
     */
    public void showAlertAndPause(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("PAUSE GAME");
            alert.setHeaderText(message);
            alert.setContentText("Press ACEPTAR to shuffle again or CANCELAR if you want to exit");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {


                    getCard();                // Reanuda el juego ya pasada la excepcion
                } else {
                    goToDisplay1();              // Va a la pantalla de inicio si se cancela
                }
            });
        });
    }

    /**
     * Method to go display1
     */
    private void goToDisplay1() {

        try {
            isGameRuning = false;
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("display1.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) nextButtonDisplay5.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println("ERROR goToDisplay1 " + e.getMessage());
        }


    }
    /**
     * Method to go display2
     */
    @FXML
    private void goToDisplay2ExitButton() {

        try {
            isGameRuning = false;
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("display2.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) backToDisplay2Button.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println("ERROR goToDisplay1 " + e.getMessage());
        }


    }

    /**
     * Method to move image of horse move one position forward or backward
     *
     * @param card to extract the suit to move horse
     */
    private void updateHorsePosition(Card card) {
        String horseSuit = String.valueOf(card.getSuit());
        horse = (ImageView) display4.lookup(("#KNIGHT_of_" + horseSuit));

        try{
            fileAccess.loadLogsFromJSON();
            fileAccess.addRound(round, card.getDescription());
            fileAccess.saveToJSON();

            DatabaseManager.insertTableRoundInfo(card,round);
        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }

        /*
        System.out.println("Ronda " + round + " : ");
        System.out.println(card.getDescription());
        */

        double newX = 0;
        //back
        if (round % 5 == 0) {
            // elige entre 2 valores siempre el mayor
            int firsPosition = 131;
            newX = Math.max(horse.getLayoutX() - 100, firsPosition);

            //next
        } else {
            if (isWinner(horse, horseSuit)) winner = true;
            newX = horse.getLayoutX() + 100;
        }

        Pause.updateHorsePlaceWithPause(horse, 1, newX, null);
        DatabaseManager.updateHorsePositionDatabase(horseSuit,newX);
    }

    /**
     * Method to check if one horse cross finish line
     * @param horse     image to check if is cross finish line
     * @param horseSuit to check
     * @return true or false if this horse pass finish line
     */
    private boolean isWinner(ImageView horse, String horseSuit) {

        if (horse.getLayoutX() >= 931) {
            Pause.updateLabelWithPause(actionLabel, "FINISH " + horseSuit + " WIN THE RACE", 1, null);
            winHorseSuit = horseSuit;


            try{
                fileAccess.loadLogsFromJSON();
                fileAccess.addRound(round," END GAME " + winHorseSuit + " WINS");
                fileAccess.saveToJSON();

                DatabaseManager.updateWinnerDatabase(winHorseSuit);

            }catch (IOException ex){
                System.out.println("ERROR in isWinner in controller4 " + ex.getMessage());
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Method to go display5
     */
    @FXML
    private void goToDisplay5() {

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("display5.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) nextButtonDisplay5.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println("ERROR goToDisplay5 " + e.getMessage());
        }


    }


    public static String getWinHorseSuit() {
        return winHorseSuit;

    }

    public void setGameData( List<Map<String, Object>> players){
        int jackpot = 0 ;
        roundNumberLabel.setText(String.valueOf(round));
        for(Map<String, Object> player : players){


            String horseSuit = String.valueOf(player.get("suit"));
            int horsePosition = Integer.parseInt(String.valueOf(player.get("layoutX_position")));
            horse = (ImageView) display4.lookup(("#KNIGHT_of_" + horseSuit));
            Pause.updateHorsePlaceWithPause(horse,1,horsePosition,null);

            String name = String.valueOf(player.get("name"));
            int bet = Integer.parseInt(String.valueOf(player.get("bet")));
            Player p = new Human(name,bet,horseSuit);

            jackpot += bet;
            GameLogic.getPlayers().add(p);


        }
        Controller3.setJackpot(jackpot);


    }

}



