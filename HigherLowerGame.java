import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HigherLowerGame {
    private static final String SCORE_FILE = "highscore.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        

        int savedScore = loadScore();
        System.out.println("Welcome back! Your previous saved score: " + savedScore);

        int targetNumber = random.nextInt(100) + 1;
        int currentGuess = 0;
        int currentRoundScore = 100;
        
        System.out.println("I've picked a number between 1 and 100. Start guessing!");


        while (currentGuess != targetNumber) {
            System.out.print("Enter your guess: ");
            currentGuess = scanner.nextInt();

            if (currentGuess < targetNumber) {
                System.out.println("HIGHER!");
                currentRoundScore -= 5;
            } else if (currentGuess > targetNumber) {
                System.out.println("LOWER!");
                currentRoundScore -= 5;
            } else {
                System.out.println("Correct! You win this round.");
            }

            if (currentRoundScore < 0) currentRoundScore = 0;
        }


        System.out.println("Your score for this round: " + currentRoundScore);
        saveScore(currentRoundScore);
        
        scanner.close();
    }


    public static void saveScore(int score) {
        try (FileWriter writer = new FileWriter(SCORE_FILE)) {
            writer.write(String.valueOf(score));
            System.out.println("Score saved to " + SCORE_FILE);
        } catch (IOException e) {
            System.out.println("Error saving score: " + e.getMessage());
        }
    }


    public static int loadScore() {
        File file = new File(SCORE_FILE);
        if (!file.exists()) return 0; 

        try (Scanner fileScanner = new Scanner(file)) {
            if (fileScanner.hasNextInt()) {
                return fileScanner.nextInt();
            }
        } catch (IOException e) {
            System.out.println("Error loading score.");
        }
        return 0;
    }
}