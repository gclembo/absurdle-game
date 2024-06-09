package gclembo.absurdle;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 * This is a controller which handles user input and output
 */
public class Controller {
    @FXML
    private GridPane guessBoard;
    @FXML
    private TextField textInput;
    @FXML
    private Label displayText;
    @FXML
    private Button restartButton;

    private final AbsurdleGame absurdle;
    private static final int ROW_HEIGHT = 67;
    private static final int PADDING_WIDTH = 10;


    /**
     * Creates new controller with new Absurdle game.
     */
    public Controller() {
        absurdle = new AbsurdleGame();
    }

    /**
     * Initializes controller and visuals.
     */
    public void initialize() {
        restartButton.setVisible(false);
        displayText.setText("Welcome to Absurdle!");
    }

    /**
     * Starts game of Absurdle over and resets visuals.
     */
    public void resetGame() {
        restartButton.setVisible(false);
        absurdle.startNewGame();
        guessBoard.getChildren().clear();
    }

    /**
     * Given a key press, processes the user's guess if the pressed key was the enter key.
     * @param event Key pressed by user.
     */
    public void getInput(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !absurdle.isGameOver()) {
            try {
                String guess = textInput.getText().toUpperCase();
                processGuess(guess);
                displayText.setText("You guessed: " + guess);
                textInput.clear();
                if (absurdle.isGameOver()) {
                    displayText.setText("You won in " + absurdle.getGuesses() + " moves!");
                    restartButton.setVisible(true);
                }
            } catch (IllegalArgumentException e) {
                displayText.setText(e.getMessage());
            } catch (Exception e) {
                displayText.setText("An error has occurred");
            }
        }
    }

    /**
     * Given a guess, processes the guess and updates the visuals accordingly.
     * @param guess Guess made by the user.
     */
    private void processGuess(String guess) {
        String guessPattern = absurdle.makeGuess(guess);

        for (int i = 0; i < 5; i++) {
            Label guessChar = new Label();
            guessChar.setText(guess.charAt(i) + "");
            guessChar.setPrefSize(100, 100);
            guessChar.setAlignment(Pos.CENTER);
            if (guessPattern.charAt(i) == '2') {
                guessChar.setStyle("-fx-background-color: #00DD00;");
            } else if (guessPattern.charAt(i) == '1') {
                guessChar.setStyle("-fx-background-color: #FFAA00;");
            } else {
                guessChar.setStyle("-fx-background-color: #FFFFFF;");
            }
            guessBoard.add(guessChar, i, absurdle.getGuesses() - 1);
            guessBoard.setPrefHeight((ROW_HEIGHT + PADDING_WIDTH) *
                    guessBoard.getRowCount() + PADDING_WIDTH);
        }
    }
}