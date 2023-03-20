import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.util.Random;
import java.util.Scanner;
import java.io.IOException;
public class Hangman {
    static int correct = 0, incorrect = 0, turns = 0;
    static char[] userInput =new char[54];
    static char[] charTable;
    static char[][] hangmanBoard = {{' ','_','_','_','_','_','_'}, //0
            {' ','|',' ',' ',' ',' ','|'}, //1
            {' ',' ',' ',' ',' ',' ','|'}, //2
            {' ',' ',' ',' ',' ',' ','|'}, //3
            {' ',' ',' ',' ',' ',' ','|'},}; //4
/*cords ROW, COLUMN
    2,1 - head
    3,0 - arm one
    3,1 - torso
    3,2 - arm two
    4,0 - leg one
    4,2 - leg two
*/

    public static void main(String[] args) throws IOException {
        Random rand = new Random();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1 if you would like to play with your own list of words or enter 2 to play the game with our words!");
        String input = sc.nextLine();
        while (!(input.equals("1")) && !(input.equals("2"))) {
            System.out.println("Enter a valid answer, refer to question.");
            input = sc.nextLine();
        }

        //If user wants to play the game with their own words
        if (input.equals("1")) {
            System.out.println("Please enter a list of words (at least 5) you would like to use separated by a comma. (Ex. apple, banana, cat)");
            String extractLine = sc.nextLine();
            String[] usersWords = extractLine.split(",");
            while(true) {
                try {
                    usersWords = extractLine.split(",");
                    if (usersWords[4] != null) break;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Please enter at least 5 words separated by a comma.");
                    extractLine = sc.nextLine();
                }
            }
            //Trim it
            for (int i = 0; i < usersWords.length;i++)
                usersWords[i] = usersWords[i].trim();

            //Game Officially Starts <!>
            int randomInt = rand.nextInt(0, usersWords.length-1);
            String chosenWord = usersWords[randomInt];
            getCharTable(chosenWord);

            while (true) {
                for (char[] i : hangmanBoard) {
                    for (int j = 0; j < hangmanBoard[0].length; j++) {
                        System.out.print(i[j]);
                    }
                    System.out.println();
                }

                for (char i : charTable) {
                    System.out.print(i+" ");
                }
                System.out.println("\nPlease enter a letter! (At any moment you may enter '/view' to view your incorrect answers so far!");
                String letterInp = sc.nextLine();
                if (!(letterInp.equalsIgnoreCase("/view"))) {
                    char c = letterInp.charAt(0);
                    checkLetter(c, chosenWord);
                    userInput[turns] = c;
                    turns++;
                    if (checkWin(chosenWord)) {
                        System.out.println("Congratulations you won the game!");
                        break;
                    } if (checkLost()) {
                        System.out.println("You lost the game!\nThe word was "+chosenWord);
                        break;
                    }
                } else {
                    viewInput();
                }
            }
        }

        //If user wants to play the game with our words
        else {
            //TODO: Finish this web scraping??
            String url = "https://www.hangmanwords.com/words";
            String systemsWords[] = new String[213];
            try {
                Document doc = Jsoup.connect(url).get();
                Elements words = doc.select("ul.Words_wordlist__i4vT0 > li");
                for (int i = 0; i < words.size(); i++) {
                    systemsWords[i] = (words.get(i).text()).trim();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            int y = rand.nextInt(0,212);
            String chosenWord = systemsWords[y];
            getCharTable(chosenWord);

            while (true) {
                for (char[] i : hangmanBoard) {
                    for (int j = 0; j < hangmanBoard[0].length; j++) {
                        System.out.print(i[j]);
                    }
                    System.out.println();
                }

                for (char i : charTable) {
                    System.out.print(i+" ");
                }

                System.out.println("\nPlease enter a letter! (At any moment you may enter '/view' to view your incorrect answers so far!");
                String letterInp = sc.nextLine();
                if (!(letterInp.equalsIgnoreCase("/view"))) {
                    char c = letterInp.charAt(0);
                    checkLetter(c, chosenWord);
                    userInput[turns] = c;
                    turns++;
                    if (checkWin(chosenWord)) {
                        System.out.println("Congratulations you won the game!");
                        break;
                    } if (checkLost()) {
                        System.out.println("You lost the game!\nThe word was "+chosenWord);
                        break;
                    }
                } else {
                    viewInput();
                }
            }
            }
        }

    public static void getCharTable(String word) {
        charTable = new char[word.length()];
        for (int i = 0; i < word.length();i++) {
            charTable[i] = '_';
        }
    }
    public static void checkLetter(char letterGuessed, String word) {
        int counter = 0;
        for (int i = 0; i < word.length();i++) {
           if (word.charAt(i) == letterGuessed) {
               charTable[i] = letterGuessed;
               System.out.println("???");
               System.out.println(charTable);
               correct++;
            } else {
                counter++;
                if (counter == word.length()) {
                    incorrect++;
                    switch (incorrect) {
                        case 1:
                            hangmanBoard[2][1] = 'O';
                            break;
                        case 2:
                            hangmanBoard[3][1] = '|';
                            break;
                        case 3:
                            hangmanBoard[3][0] = '/';
                            break;
                        case 4:
                            hangmanBoard[3][2] = '\\';
                            break;
                        case 5:
                            hangmanBoard[4][0] = '/';
                            break;
                        case 6:
                            hangmanBoard[4][2] = '\\';
                            break;
                    }
                }
            }
        }
    }
    public static void viewInput() {
        for (char i : userInput) {
            System.out.print(i+" ");
        }
        System.out.println("\n");
    }
    public static boolean checkWin(String word) {
        if (word.length()==correct)
            return true;
        return false;
    }
    public static boolean checkLost() {
        if (incorrect==6)
            return true;
        return false;
    }
}

