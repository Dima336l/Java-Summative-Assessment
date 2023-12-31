package summative.assesment;

import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SummativeAssesment {

//    Function to handle input through Scanner.
    public static String handleScanner(String type) {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        switch (type) {
            case "next": {
                input = scanner.next();
                break;
            }
            case "nextLine": {
                input = scanner.nextLine();
                break;
            }
        }
        return input;
    }

    public static void handleExit(String input, String exitChar) {
        if (input.toLowerCase().equals(exitChar.toLowerCase())) {
            System.out.println("Bye Bye");
            System.exit(0);
        }
    }

    public static void firstPrompt() {
        String input;
        boolean isValid;
        System.out.println("Strategic dice game");
        do {
            System.out.print("Play game (1) or Exit game (0) > ");
            input = handleScanner("next");
//            If input other than 1 or 0 display error message
            isValid = (!(input.equals("1") || input.equals("0")));
            if (isValid) {
                System.out.println("\nInvalid input, Enter '1' or '0'\n");
            }
        } while (isValid);
//        Exiting the program in case user selects 0
        handleExit(input, "0");
    }

    public static void printLines() {
        final int NUM_OF_LINES = 57;
        for (int i = 0; i < NUM_OF_LINES; i++) {
            System.out.print("-");
        }
        System.out.println("");

    }

//    Function to draw the table
    public static void drawTable(Map<Integer, String> scoreArrPlayer1, Map<Integer, String> scoreArrPlayer2, Map<Integer, String> selectionArrPlayer1, Map<Integer, String> selectionArrPlayer2) {
        int player1Total, player2Total;
        List<String> categories = Arrays.asList("Ones", "Twos", "Threes", "Fours", "Fives", "Sixes", "Sequence 20", "TOTAL");
        final int NUM_OF_ROWS = 6;
        player1Total = calculateTotal(scoreArrPlayer1);
        player2Total = calculateTotal(scoreArrPlayer2);
        printLines();
        System.out.printf(
                "|%-23s|%4s%-11s|%4s%-11s|%n", "", "", "Player1", "", "Player2");
        printLines();
        for (int i = 0; i <= NUM_OF_ROWS; i++) {
            System.out.printf("|%-23s|%7s%-8s|%7s%-8s|%n", categories.get(i), "", scoreArrPlayer1.get(i), "", scoreArrPlayer2.get(i));
            printLines();
        }
        System.out.printf("|%-23s|%7s%-8s|%7s%-8s|%n", "TOTAL", "", player1Total, "", player2Total);
        printLines();
    }

    public static void displayRound(int roundNumber) {
        System.out.println("\n\n----------");
        System.out.println("Round " + roundNumber);
        System.out.println("----------\n");

    }

    public static void displayFirstPrompt(int numberOfThrows, int playerNum, int i, int count, int NUM_OF_THROWS) {
        if (count != NUM_OF_THROWS) { // If there are no more dices to throw, we skip this step
            if (i == 0) {
                System.out.print("\nFirst throw of this turn, ");
            } else {
                System.out.print("Next throw of this turn, ");
            }
            displayNumOfThrows(numberOfThrows, playerNum);
        }
    }

    public static void displayNumOfThrows(int numberOfThrows, int playerNum) {
        System.out.print("Player " + playerNum + " to throw " + numberOfThrows + " dice.\n");
        handleThrows(numberOfThrows, playerNum);
    }

    public static String handleThrows(int numberOfThrows, int playerNum) {
        String input;
        boolean isValid;
        do {
            System.out.print("Throw " + numberOfThrows + " dice, enter 't' to throw or 'f' to forfiet > ");
            input = handleScanner("next");
            isValid = (!(input.toLowerCase().equals("t") || input.toLowerCase().equals("f"))); //input validation
            if (isValid) {
                System.out.println("\nInvalid input, Enter 't' or 'f'\n");
            }
            handleForfeit(input, playerNum);
        } while (isValid);
        return input;
    }

    public static void handleForfeit(String input, int playerNum) {
        if (input.toLowerCase().equals("f")) {
            System.out.println("\nPlayer " + playerNum + " forfeited.\nGame over.");
            System.exit(0);
        }
    }

    public static int remainingThrowsPrompt(int remainingThrows, int count, int NUM_OF_THROWS) {
        if (count != NUM_OF_THROWS) { // If no more dices left to throw, no more remaining number of throws for that turn so skip this step
            if (remainingThrows > 0) {
                System.out.println("\n" + remainingThrows + " throws remaining for this turn.\n");
            }
            remainingThrows--;
        }
        return remainingThrows;
    }

    public static int generateRandomNumber() {
        Random random = new Random();
        int min = 1;
        int max = 6;
        int randomNumber = random.nextInt(max - min + 1) + min; // generate random num between 1 and 6
        return randomNumber;
    }

    public static ArrayList<Integer> generateDiceThrow(int numOfThrows) {
        ArrayList<Integer> arr = new ArrayList<>();
        if (numOfThrows > 0) {
            System.out.print("\nThrow: ");
            for (int i = 0; i < numOfThrows; i++) {
                int num = generateRandomNumber();
                arr.add(num);
                System.out.print("[ " + num + " ] ");
            }
            System.out.println("");
        }
        return arr;
    }

    //    Function to select category
    public static String selectCategoryOrDefer(String categorySelected, int i) {
        String selectOrDefer = "";
        Boolean itsValid;
        if (i != 2) { // Check if it is last throw so that user doesn't get to defer
            if (categorySelected.equals("")) { // Check if a category has been selected, if not execute the loop
                do {
                    selectOrDefer = selectCategory();
                    itsValid = checkIfCategoryValid(selectOrDefer);
                } while (itsValid);
            }
        }
        return selectOrDefer;
    }

    public static String selectCategory() {
        System.out.print("\nEnter 's' to select category (number on die/dice) or 'd' to defer > ");
        String input = handleScanner("next");
        System.out.println("");
        return input;
    }

//    Function to handle categories 
    public static String handleCategory(String categorySelected, String selectOrDefer, Map<Integer, String> selectionArrPlayer1, Map<Integer, String> selectionArrPlayer2, int playerNum, int i) {
        if (i == 2 && categorySelected.equals("")) {  // Check if its last iteration, if so exit the function. This is done to ensure that player can't select or defer, and its forced to choose a selection
            categorySelected = handleSelection(selectionArrPlayer1, selectionArrPlayer2, playerNum, i); // Since its a function that returns a string , return "" exits the function
        } else if (!(selectOrDefer.equals(""))) { // Check if user has already made a selection
            switch (selectOrDefer.toLowerCase()) {
                case "s": {
                    categorySelected = handleSelection(selectionArrPlayer1, selectionArrPlayer2, playerNum, i);
                    break;
                }
                case "d": {
                    handleDefer();
                    break;
                }
            }
        }
        return categorySelected;
    }

    public static String handleSelection(Map<Integer, String> selectionArrPlayer1, Map<Integer, String> selectionArrPlayer2, int playerNum, int i) {
        String choice;
        boolean itsValid;
        System.out.println("\nSelect category to play.");
        do {
            System.out.print("Ones (1) Twos (2) Threes (3) Fours (4) Fives (5) Sixes (6) or Sequence (7) > ");
            choice = handleScanner("nextLine");
            itsValid = isValidCategory(choice, selectionArrPlayer1, selectionArrPlayer2, playerNum); // Validation check
        } while (!(itsValid));
        updateSelectionArray(choice, playerNum, selectionArrPlayer1, selectionArrPlayer2);
        return choice;
    }

//  Function to play the selection 1 to 6
    public static int playSelection1to6(int count, String categorySelected, ArrayList<Integer> arr) {
        count = countingThrows(categorySelected, arr);
        return count;
    }

    public static boolean checkIfCategoryValid(String input) {
        boolean ItsValid;
        ItsValid = (!(input.toLowerCase().equals("s") || input.toLowerCase().equals("d")));
        if (ItsValid) {
            System.out.println("\nInvalid input, Enter 's' or 'd'\n");
        }
        return ItsValid;
    }

//    Function to check if category selected is valid
    public static Boolean isValidCategory(String input, Map<Integer, String> selectionArrPlayer1, Map<Integer, String> SelectionArrPlayer2, int playerNum) {
        Boolean itsValid = false;
        String[] tokens;
        tokens = input.split("\\s+");
        if (tokens.length > 1) {
            System.out.println("You can only select 1 category");
        } else {
            try {
                int intInput = Integer.valueOf(input);
                if (intInput >= 1 && intInput <= 7) {
                    itsValid = true;
                } else {
                    System.out.println("There is no such category");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input not a number");
            }
        }
        return itsValid && hasBeenSelected(input, selectionArrPlayer1, SelectionArrPlayer2, playerNum);
    }

//      Function to check if the category has not already been selected
    public static Boolean hasBeenSelected(String input, Map<Integer, String> selectionArrPlayer1, Map<Integer, String> SelectionArrPlayer2, int playerNum) {
        switch (playerNum) { //        Switching on playerNum to check approrpiate arrays
            case 1: {
                if (selectionArrPlayer1.containsValue(input)) {
                    System.out.println("\nThat was already selected, please choose something else");
                    return false;
                }
                break;
            }
            case 2: {
                if (SelectionArrPlayer2.containsValue(input)) {
                    System.out.println("\nThat was already selected, please choose something else");
                    return false;
                }
                break;
            }
        }
        return true;
    }

    public static void updateSelectionArray(String choice, int playerNum, Map<Integer, String> selectionArrPlayer1, Map<Integer, String> selectionArrPlayer2) {
        //        Switching on playerNum to update appropriate arrays
        int index = Integer.valueOf(choice) - 1; // Getting the appropriate index to update the arrray
        if (playerNum == 1) {
            selectionArrPlayer1.put(index, (choice));
        } else {
            selectionArrPlayer2.put(index, (choice));
        }
    }

//        Function to upadte the appropriate selection arrays 
    public static void updateScoreArrays(String categorySelected, int playerNum, int total, Map<Integer, String> scoreArrPlayer1, Map<Integer, String> scoreArrPlayer2) {
        int index = Integer.valueOf(categorySelected) - 1; // Calculating the index based on category selected          
        if (playerNum == 1) {
            scoreArrPlayer1.put(index, (Integer.toString(total)));
        } else {
            scoreArrPlayer2.put(index, (Integer.toString(total)));
        }
    }

    public static void handleDefer() {
        System.out.println("Selection deferred\n");

    }

//    Function to display selection
    public static void displaySelected(String input) {
        String[] selections = {"Ones", "Twos", "Threes", "Fours", "Fives", "Sixes", "Sequence 20"};
        int intValue = Integer.parseInt(input);
        System.out.println("\n" + selections[intValue - 1] + " selected.");

    }

//    Function to play selection 
    public static int playSelection(String categorySelected, int count, ArrayList<Integer> diceArr, ArrayList<String> player1SequenceArray, ArrayList<String> player2SequenceArray, int playerNum, int NUM_OF_THROWS) {
        if (!(categorySelected.equals(""))) { // Check if a category is selected in order to play
            if (count != NUM_OF_THROWS) { // Check if we have any more dices to throw
                if (categorySelected.equals("7")) { // Check if selection is a sequence
                    switch (playerNum) { // Switching on playerNum in order to update the appropriate arrarys
                        case 1: {
                            player1SequenceArray = handleSequence(diceArr, player1SequenceArray);
                            count = player1SequenceArray.size();
                            break;
                        }
                        case 2: {
                            player2SequenceArray = handleSequence(diceArr, player2SequenceArray);
                            count = player2SequenceArray.size();
                            break;
                        }
                    }
                } else { // Selection is 1 to 6
                    count += playSelection1to6(count, categorySelected, diceArr);
                    settingDiceAside(categorySelected, count);
                }
            }
        }
        return count;
    }

    //    Function to handle the sequence selection
    public static ArrayList<String> handleSequence(ArrayList<Integer> arr, ArrayList<String> sequenceArray) {
        ArrayList<Integer> indexesSelected = new ArrayList<>();
        displaySequenceSelection(arr);
        boolean isValid;
        do {
            indexesSelected = getSequenceInput();
            isValid = checkSequenceInputValidity(arr, indexesSelected);

        } while (!(isValid));
        sequenceArray = addSequenceDices(arr, indexesSelected, sequenceArray);
        displaySequenceSelected(arr, sequenceArray);
        return sequenceArray;
    }

    //    Function to display dice sequence selection
    public static void displaySequenceSelection(ArrayList<Integer> arr) {
        System.out.println("\n0. None");
        for (int i = 1; i <= arr.size(); i++) {
            System.out.println(i + "." + "[ " + arr.get(i - 1) + " ]");
        }
    }

//    Function for reading sequence input
    public static ArrayList<Integer> getSequenceInput() {
        ArrayList<Integer> sequenceArray = new ArrayList<>();
        String input;
        String[] tokens;
        boolean isValid = false;
        do {
            System.out.print("\nEnter which dice you wish to set aside using the number labels separated by a space (eg., 1 , 3 , 5) or enter 0 for none > ");
            input = handleScanner("nextLine");
            tokens = input.split("\\s+"); // Spliting based on whitespace
            for (String token : tokens) {
                try {
//                Trying to parse the token as an integer
                    int num = Integer.parseInt(token);
                    sequenceArray.add(num);
                    isValid = true;
//                If the token is not an integer, handle the exception
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input detected, please try again");
                    sequenceArray.clear(); // Reseting the sequence array so user starts with a new sequence
                    break;
                }
            }
        } while (!(isValid));
        return sequenceArray;
    }

//    Function to check the validity of sequence input
    public static boolean checkSequenceInputValidity(ArrayList<Integer> arr, ArrayList<Integer> sequenceArray) {
        Set<Integer> set = new HashSet<>(sequenceArray);
        final int SEQ_ARR_SIZE = 5;
        if (sequenceArray.isEmpty()) {
            System.out.println("\nYou haven't seleceted anything, plase try again");
            return false;
        }
        if (sequenceArray.size() > SEQ_ARR_SIZE) {
            System.out.println("You have selected to many dice, plase try again");
            return false;
        } else if (set.size() < sequenceArray.size()) {
            System.out.println("\nThere are duplicates, plase try again");
            return false;
        } else {
            for (int num : sequenceArray) {
                if (num < 0 || num > arr.size()) {
                    System.out.println("\nInput out of bounds, plase try again");
                    return false;
                }
                if (num == 0 && sequenceArray.size() > 1) {
                    System.out.println("\nYou chose nothing AND something, that don't make sense");
                    return false;
                }
            }
        }
        return true;
    }

    public static ArrayList<String> addSequenceDices(ArrayList<Integer> arr, ArrayList<Integer> indexesSelected, ArrayList<String> sequenceArraySelected) {
        for (int num : indexesSelected) {
            int index = num - 1;
            if (index >= 0 && index < arr.size()) {
                int numbr = arr.get(index);
                sequenceArraySelected.add(Integer.toString(numbr));
            }
        }
        return sequenceArraySelected;
    }

    public static void displaySequenceSelected(ArrayList<Integer> arr, ArrayList<String> sequenceArraySelected) {
        int count = 0;
        Collections.sort(sequenceArraySelected);
        if (sequenceArraySelected.isEmpty()) { // Check if player has selected any dice to keep if not display the message
            System.out.println("\nYou have selected no dice to keep.");
        } else {
            System.out.println("\nYou have selected the following dice to keep.");
            for (String numbar : sequenceArraySelected) {
                System.out.print("[" + numbar + "]");
                count++;
            }
        }
        System.out.println("\n");
    }

    public static int countingThrows(String input, ArrayList<Integer> arr) {
        int count = 0;
        int diceNumSelected = Integer.parseInt(input);
        for (Integer diceNum : arr) {
            if (diceNum == diceNumSelected) {
                count++;
            }
        }
        System.out.print("\nThat throw had " + count + " dice with value " + diceNumSelected);
        System.out.println("");
        return count;
    }

    public static int settingDiceAside(String input, int count) {
        if (count != 0) {
            System.out.print("Setting aside " + count + " dice: ");
            for (int i = 0; i < count; i++) {
                System.out.print("[ " + input + " ] ");
            }
            System.out.println("");
        }
        System.out.println("");
        return count;
    }

    public static int displayTotal(String categorySelected, int count, int playerNum, ArrayList<String> sequenceArrayPlayer1, ArrayList<String> sequenceArrayPlayer2, Map<Integer, String> scoreArrPlayer1, Map<Integer, String> scoreArrPlayer2) {
        int total;

        if (categorySelected.equals("7")) { // Calculate sequence total
            total = displaySequenceTotal(sequenceArrayPlayer1, sequenceArrayPlayer2, playerNum);
        } else {
            total = displayNormalTotal(categorySelected, count, playerNum); // Calculate normal total
        }
        updateScoreArrays(categorySelected, playerNum, total, scoreArrPlayer1, scoreArrPlayer2);
        return total;
    }

    public static int displayNormalTotal(String categorySelected, int count, int playerNum) {
        int diceNum = Integer.parseInt(categorySelected);
        int total = diceNum * count;
        System.out.println("\nPlayer " + playerNum + " made " + count + " with value " + categorySelected + " and scores " + total + " for that round.");
        return total;
    }

//    Function to display the total if player has selected a sequence  
    public static int displaySequenceTotal(ArrayList<String> player1SequenceArray, ArrayList<String> player2SequenceArray, int playerNum) {
        Boolean isValidSequence = false;
        switch (playerNum) {
            case 1: {
                isValidSequence = itsValidSequence(player1SequenceArray);
                break;
            }
            case 2: {
                isValidSequence = itsValidSequence(player2SequenceArray);
                break;
            }
        }
        return displaySeqeunceResults(isValidSequence, playerNum);
    }

    public static int displaySeqeunceResults(boolean isValidSequence, int playerNum) {
        int total = 0;
        if (isValidSequence) {
            System.out.println("A correct sequence has been established");
            System.out.println("Player " + playerNum + " scores 20 for the sequence category.");
            total = 20;
        } else {
            System.out.println("A correct sequence has not been established");
            System.out.println("Player " + playerNum + " scores 0 for the sequence category.");
        }
        return total;

    }

    public static Boolean itsValidSequence(ArrayList<String> sequence) {
        final int SEQ_ARR_SIZE = 5;
        if (sequence.size() != SEQ_ARR_SIZE) { // Check if the sequence array has 5 elements, if not its not a sequence and exit
            return false;
        }
        for (int i = 0; i < sequence.size() - 1; i++) { // Check till sequencesize - 1 so no out of bounds error
            if ((Integer.parseInt(sequence.get(i))) != Integer.parseInt(sequence.get(i + 1)) - 1) { // Check if the previous number is equal to next number - 1
                return false;
            }
        }
        return true;
    }

    public static int switchPlayerNum(int playerNum) {
        return playerNum == 1 ? 2 : 1;
    }

//    Function to determine who wins
    public static int determineWinner(Map<Integer, String> player1ScoreArr, Map<Integer, String> player2ScoreArr) {
        int player1Total = 0, player2Total = 0;
        for (int i = 0; i < player1ScoreArr.size(); i++) {
            player1Total += Integer.valueOf(player1ScoreArr.get(i));
            player2Total += Integer.valueOf(player2ScoreArr.get(i));
        }
        if (player1Total > player2Total) {
            return 1;
        } else {
            return 2;
        }
    }

    public static int calculateTotal(Map<Integer, String> player1ScoreArr) {
        int total = 0;
        int scoreToInt;
        for (int i = 0; i < player1ScoreArr.size(); i++) {
            if (!(player1ScoreArr.get(i).equals(""))) {
                scoreToInt = Integer.parseInt(player1ScoreArr.get(i));
                total += scoreToInt;
            }
        }
        return total;
    }

//    Function to display the winner
    public static void displayWinner(int playerNum) {
        System.out.println("\nGame over.\nPlayer " + playerNum + " has won the game\n");
        playGame();
    }

    public static void playRound(int NUM_OF_THROWS, int numOfRemainingDiceToThrow, int NUM_OF_DICES_AT_START, int playerNum, ArrayList<Integer> diceArr, String selectOrDefer, int total, Map<Integer, String> player1SelectionArr, Map<Integer, String> player2SelectionArr, Map<Integer, String> player1ScoreArr, Map<Integer, String> player2ScoreArr, ArrayList<String> player1SequenceArray, ArrayList<String> player2SequenceArray) {
        int diceScored = 0;
        int remainingThrows = 2;
        String categorySelected = "";
        for (int i = 0; i < NUM_OF_THROWS; i++) {
            numOfRemainingDiceToThrow = NUM_OF_DICES_AT_START - diceScored; //Calculating the number of remaining dice to throw 
            displayFirstPrompt(numOfRemainingDiceToThrow, playerNum, i, diceScored, NUM_OF_DICES_AT_START); // Displaying first prompt
            remainingThrows = remainingThrowsPrompt(remainingThrows, diceScored, NUM_OF_DICES_AT_START); // Calculating the number of remaining throws
            diceArr = generateDiceThrow(numOfRemainingDiceToThrow); // Simulating the dice throws
            selectOrDefer = selectCategoryOrDefer(categorySelected, i); // Prompting the user to select or defer
            categorySelected = handleCategory(categorySelected, selectOrDefer, player1SelectionArr, player2SelectionArr, playerNum, i); // Handling selection or deference
            diceScored = playSelection(categorySelected, diceScored, diceArr, player1SequenceArray, player2SequenceArray, playerNum, NUM_OF_DICES_AT_START); // Playing selection
        }
        total = displayTotal(categorySelected, diceScored, playerNum, player1SequenceArray, player2SequenceArray, player1ScoreArr, player2ScoreArr);
        drawTable(player1ScoreArr, player2ScoreArr, player1SelectionArr, player2SelectionArr);
    }

    public static void initializeArrays(Map<Integer, String> player1SelectionArr, Map<Integer, String> player2SelectionArr, Map<Integer, String> player1ScoreArr, Map<Integer, String> player2ScoreArr, int arraySize) {
        //      Populating the hashMaps with empty strings so we can replace later, for drawing the table with empty cells
        for (int i = 0; i < arraySize; i++) {
            player1SelectionArr.put(i, "");
            player2SelectionArr.put(i, "");
            player1ScoreArr.put(i, "");
            player2ScoreArr.put(i, "");
        }
    }

    public static int playGame() {
        int arraySize = 7;
        final int NUM_OF_DICES_AT_START = 5;
        final int NUM_OF_TURNS = 14;
        final int NUM_OF_THROWS = 3;
        int numOfRemainingDiceToThrow = 0;
        int playerNum = 1;
        int winner;
        int roundNum = 1;
        int total = 0;
        String selectOrDefer = "";
        Map<Integer, String> player1SelectionArr = new HashMap<>(arraySize);//Using hashMaps so that we can insert elements at specific position without shifting other elements
        Map<Integer, String> player2SelectionArr = new HashMap<>(arraySize);
        Map<Integer, String> player1ScoreArr = new HashMap<>(arraySize);
        Map<Integer, String> player2ScoreArr = new HashMap<>(arraySize);
        initializeArrays(player1SelectionArr, player2SelectionArr, player1ScoreArr, player2ScoreArr, arraySize);
        ArrayList<Integer> diceArr = new ArrayList<>(arraySize); //No need for shifting so just use ArrayLists
        ArrayList<String> player1SequenceArray = new ArrayList<>(arraySize);
        ArrayList<String> player2SequenceArray = new ArrayList<>(arraySize);
        firstPrompt();
        drawTable(player1ScoreArr, player2ScoreArr, player1SelectionArr, player2SelectionArr);
        for (int k = 0; k < NUM_OF_TURNS; k++) {
            if (k % 2 == 0) // Check if 2 turns have been played, if so display next round prompt
                displayRound(roundNum++);
            playRound(NUM_OF_THROWS, numOfRemainingDiceToThrow, NUM_OF_DICES_AT_START,
                    playerNum, diceArr, selectOrDefer, total, player1SelectionArr, player2SelectionArr,
                    player1ScoreArr, player2ScoreArr, player1SequenceArray, player2SequenceArray);
            playerNum = switchPlayerNum(playerNum);
        }
        winner = determineWinner(player1ScoreArr, player2ScoreArr);
        return winner;
    }

    public static void main(String[] args) {
        int playerWinner = playGame();
        displayWinner(playerWinner);

    }
}
