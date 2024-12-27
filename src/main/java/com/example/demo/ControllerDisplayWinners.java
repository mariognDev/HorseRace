package com.example.demo;

import com.example.demo.helper.File.FileWinnersAccess;
import com.example.demo.helper.Pause;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class ControllerDisplayWinners {

    @FXML
    public Label winnersLabel;

    @FXML
    public Button backButtonWinnersDisplay;


    /**
     * Method to show all winners in saved in file txt.
     */
    @FXML
    public void initialize() {
        FileWinnersAccess fileWinnersAccess = new FileWinnersAccess();
        fileWinnersAccess.loadWinnersFromJson();
        List<String[]> winners = fileWinnersAccess.getWinnersList();
        StringBuilder winnerJackpot = new StringBuilder();
        for(String [] winner : winners){
             winnerJackpot.append(winner[0]).append("...").append(winner[1]).append(" €\n");
        }
        Pause.updateLabelWithPause(winnersLabel, String.valueOf(winnerJackpot),1,null);
    }

    @FXML
    protected  void goToDisplay2(){
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("display2.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) backButtonWinnersDisplay.getScene().getWindow();

            stage.setScene(scene);
        }catch (Exception e){
            System.out.println("ERROR goToDisplay2 " + e.getMessage());
        }

    }





}
