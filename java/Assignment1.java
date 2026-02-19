import java.util.Scanner;
import java.util.Arrays;
import java.util.HashSet;

public class Assignment1 {

  // rank and suit arrays
  private static String[] RANKS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
  private static String[] SUITS = {"C", "D", "H", "S"};

  public static void main(String[] args) {

    // arrays required in main method 
    int[] deck = new[52];
    int[] player = new int[5];
    int[] dealer = new int[5];

    Scanner name = new Scanner(System.in);

    // prompts the user for their name
    System.out.print("Please enter your name: ");
    String input = name.nextLine();

    // displays a message welcoming the user
    // check this XX!!
    System.out.println("Hello and Welcome, " + input);

    // variable 
    int choice;

    // do-while loop for menu

    if(args.length > 0) {
      parseCmdLineArgs(args, player, dealer); //STUDENT: Insert player and dealer arrays in call here
    } else {
      //STUDENT: Insert method calls for populating the hands yourself using a shuffled deck here
    
    }//end if

  }//end main

  // 
  // private static void

  // the deck is shuffled
  private static void shuffle(int[] d) {
    for(int i = d.length – 1; i > 0; i--) {
      int rnd = (int) (Math.random() * (i + 1));
      int temp = d[i];
      d[i] = d[rnd];
      d[rnd] = temp;
      }//end for
    }//end shuffle

  //
  // private static void shuffle 

  private static String getCardString(int id) {
    return RANKS[id % 13] + SUITS[id / 13];
  } // end getCardString

  public static void parseCmdLineArgs(String[] args, int[] player, int[] dealer) {

    if (player == null || dealer == null)
      throw new IllegalArgumentException("Player and dealer arrays cannot be null.");
    
    if (player.length != 5 || dealer.length != 5)
      throw new IllegalArgumentException("Player and dealer arrays must both be length 5.");
    
    boolean sawP = false;
    boolean sawB = false;
    
    int pIndex = 0;
    int dIndex = 0;
    int mode = 0;
    
    HashSet<Integer> used = new HashSet<>();
    
    for(int i = 0; i < args.length; i++) {
      
      String token = args[i].toUpperCase();
      
      if( token.equals("-P") ) {
        sawP = true;
        mode = 1;
        continue;
      }//end if

      if( token.equals("-D") ) {
        sawB = true;
        mode = 2;
        continue;
      }//end if
      
      
      if(mode == 0) {
        throw new IllegalArgumentException("Card '" + token +
                                           "' provided before a flag. Use -p and -d.");
      }//end if
      
      int id = convertToId(token);
      if(id == -1) {
        throw new IllegalArgumentException("Invalid card: " + token + " (expected e.g., AS, 10H, QC)");
      }//end if

      if( !used.add(id) ) {
        throw new IllegalArgumentException("Duplicate card specified: " + token);
      }//end if
      
      if(mode == 1) {
        if(pIndex >= 5)
          throw new IllegalArgumentException("Too many player cards. Exactly 5 required.");
        player[pIndex++] = id;
      } else {
        if(dIndex >= 5)
          throw new IllegalArgumentException("Too many dealer/banker cards. Exactly 5 required.");
        dealer[dIndex++] = id;
      }//end if
      
    }//end for

    if (!sawP || !sawB) {
      throw new IllegalArgumentException("Both -p and -d must be specified.");
    }//end if

    // Exactly 5 cards each
    if(pIndex != 5 || dIndex != 5) {
      throw new IllegalArgumentException("Exactly 5 cards must be specified for each hand. " +
                                         "Got player=" + pIndex + ", dealer=" + dIndex + ".");
    }//end if
    
  }//end

  public static int convertToId(String card) {

    if (card == null) return -1;
    
    card = card.trim().toUpperCase();
    if(card.length() < 2 || card.length() > 3) return -1;

    String rankStr = card.substring(0, card.length() - 1);
    String suitStr = card.substring(card.length() - 1);

    String[] ranks = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
    String[] suits = {"C","D","H","S"};

    int rankIndex = -1;
    int suitIndex = -1;
    
    for(int i = 0; i < ranks.length; i++) {
      if( ranks[i].equals(rankStr) ) {
        rankIndex = i;
        break;
      }//end if
    }//end for

    for(int i = 0; i < suits.length; i++) {
      if( suits[i].equals(suitStr) ) {
        suitIndex = i;
        break;
      }//end if
    }//end for
    
    if(rankIndex == -1 || suitIndex == -1) return -1;
    
    return suitIndex * 13 + rankIndex;
    
  }//end converToId

  
}//end Assignment1
