
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Server {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isAlone = false, isInvalid = true;
        int marbles = 0;
        
        System.out.println("To select a difficulty, input: ");
        System.out.println("Input '1' for easy \nInput '2' for medium \nInput '3' for hard");

        while (isInvalid) { //DIFFICULTY SELECTION, LOOPS IF INPUT IS NOT BETWEEN 1 AND 3
            String result = scanner.nextLine(); //PROMPT USER
            
            if (result.startsWith("1") || result.startsWith("2") || result.startsWith("3")) {
                isInvalid = false;

                if (result.startsWith("1")) {
                    marbles = randomNumber(2, 20);
                    System.out.println("You have chosen easy.");
                } 
                else if (result.startsWith("2")) {
                    marbles = randomNumber(2, 50);
                    System.out.println("You have chosen medium.");            
                }
                else if (result.startsWith("3")) {
                    marbles = randomNumber(2, 100);
                    System.out.println("You have chosen hard.");
                }
            }
            else{
                System.out.println("Your input was invalid. You must input: 1, 2, or 3.");
            }
        }
        
        /*
        isInvalid = true;
        
        while (isInvalid) { //ACTIVATES AI FOR SINGLE PLAYER SESSION, NO AI ALGORITHM IS IMPLEMENTED BUT THIS CODE SUPPORTS SINGLE-PLAYER
            String result = scanner.nextLine(); //PROMPT USER
            
            if (result.startsWith("y") || result.startsWith("n")) {
                isInvalid = false;

                if (result.startsWith("y")) {
                    isAlone = true;
                    System.out.println("This is a single-player session.");
                } 
                else if (result.startsWith("n")) {
                    isAlone = true;
                    System.out.println("This is a a two player session.");
                }
            }
            else{
                System.out.println("Your input was invalid. You must input: y for yes or n for no.");
            }
        }*/
        
        try { //PLAYER CONNECTIONS
            System.out.println("Awaiting player connection.");
            ServerSocket ss = new ServerSocket(7777); //OPEN A SERVER SOCKET PORT 7777
            
            while (true) {
                Socket pa = ss.accept(); //CONNECT PLAYER 1
                BufferedReader bra = new BufferedReader(new InputStreamReader(pa.getInputStream()));
                BufferedWriter bwa = new BufferedWriter(new OutputStreamWriter(pa.getOutputStream()));
                System.out.println("A player connected.");
                BufferedReader brb = null;
                BufferedWriter bwb = null;
                
                if (!isAlone) { //CONNECTS PLAYER 2 IF NOT ALONE
                    Socket pb = ss.accept(); //CONNECT PLAYER 2
                    brb = new BufferedReader(new InputStreamReader(pb.getInputStream()));
                    bwb = new BufferedWriter(new OutputStreamWriter(pb.getOutputStream()));
                    System.out.println("Two players connected.");
                }
                
                boolean playerOneFirst = randomBool();
               
                if (playerOneFirst) { //PLAYER ONE PLAYS FIRST
                    bwa.write("first\n");
                    bwa.flush();
                    
                    if(!isAlone) {
                        bwb.write("second\n");
                        bwb.flush();
                    }

                    System.out.println("Player 1 goes first.");
                }
                else { //PLAYER TWO PLAYS FIRST
                    bwa.write("second\n");
                    bwa.flush();
                    
                    if (!isAlone) {
                        bwb.write("first\n");
                        bwb.flush();                    
                        System.out.println("Player 2 goes first.");
                    } 
                    else {
                        System.out.println("Computer goes first.");                                        
                    }
                }
                
                boolean playerOneTurn = playerOneFirst, end = false;
                int number = 0, subtract = 0;
                String input = "", output = "";                
                
                while (!end) {
                    while (playerOneTurn) { //PLAYER 1 INPUT PROVESSING
                        bwa.write(marbles + "\n");
                        bwa.flush();

                        input = bra.readLine();
                        subtract = Integer.parseInt(input);
                        output = calculate(marbles, subtract, bwa);
                        
                        //if (output.contains("invalid")) { 
                        
                        //}
                        if (output.contains("end")) {
                            end = true;
                        }
                        else {
                            marbles = parse(output);
                            playerOneTurn = endTurn(playerOneTurn);
                            sendTurn(false, bwa);
                            sendTurn(true, bwb);
                        }
                    }
                    
                    while (!playerOneTurn) { //PLAYER 2 INPUT PROCESSING
                        bwb.write(marbles + "\n");
                        bwb.flush();

                        input = brb.readLine();
                        subtract = Integer.parseInt(input);
                        output = calculate(marbles, subtract, bwb);
                        
                        //if (output.contains("invalid")) {
                        
                        //}
                        if (output.contains("end")) {
                            end = true;
                        }
                        else {
                            marbles = parse(output);
                            playerOneTurn = endTurn(playerOneTurn);
                            sendTurn(true, bwa);
                            sendTurn(false, bwb);
                        }
                    }
                }
                
                break;
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public static  int randomNumber(int min, int max){ //RETURN RANDOM NUMBER
        int value = ThreadLocalRandom.current().nextInt(min, max + 1);
        return value;
    }
    
    public static boolean randomBool(){ //RETURN RANDOM BOOL
        Random random = new Random();
        return random.nextBoolean();
    }

    public static String calculate(int marbles, int subtract, BufferedWriter bw) throws IOException { //RETURN CALCULATION OR MESSAGE
        if (marbles < subtract) { //NOT ENOUGH MABLES TO TAKE
            System.out.println("Move was invalid.");
            bw.write("Move was invalid.\n");
            bw.flush();
            return "invalid";
        }

        marbles -= subtract; //ARITHMETIC

        if (marbles == 0) {
            System.out.println("Game has ended.");
            bw.write("You have won!\n");
            bw.flush();
            return "end";
        }

        System.out.println("There are " + marbles + " marbles left.");
        return Integer.toString(marbles);
    }
    
    public static int parse(String string) { //CONEVERT STRING TO INT
        int value = Integer.parseInt(string);
        return value;
    }
    
    public static String display(int marbles) { //PRINT STRING
        int value = marbles;
        return "There are " + value + " marbles.";
    }

    public static boolean endTurn(boolean value) { //END TURN IN SERVER
        boolean temp = !value;
        return temp;
    }
    
    public static void sendTurn (boolean value, BufferedWriter bw) throws IOException { //END TURN IN CLIENT VIA STRING
        bw.write(value + "\n");
        bw.flush();
    }
}