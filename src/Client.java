
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); //USED FOR USER INPUT
        
        try { 
            while(true) {
                Socket socket = new Socket("localhost", 7777); //SOCKET AND READER/WRITER TO CONNECT TO SERVER
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Connected to server.");

                boolean end = false, turn = false;
                String reply = br.readLine(); //GETS STRING STATING IF THIS PLAYER GOES FIRST
                
                if (reply.contains("first")) { //SETS WHAT PLAYER GOES FIRST ACCORDING TO A MESSAGE FROM SERVER
                    turn = true;
                    System.out.println("Your turn.");
                }
                                
                while (!end) {
                    while (turn) { //PLAYER PLAYS IF TURN IS TRUE, THIS IS SET BY THE SERVER
                        System.out.println("I ran");
                        reply = br.readLine(); //GETS NUMBER OF MARBLES FROM SERVER
                        int marbles = Integer.parseInt(reply); //CHANGE STRING TO INT
                        
                        if (marbles % 2 == 0) { //IF ODD, HALF AND THEN ROUND DOWN
                            marbles -= marbles / 2;
                        }
                        else { //IF EVEN, HALF IT
                            marbles -= ((marbles / 2) - 1);
                        }
                        
                        System.out.print("Take upto " + marbles + " marbles.\n");
                        String input = scanner.nextLine(); //INPUT NUMBER
                        bw.write(input + "\n");
                        bw.flush(); 
                        reply = br.readLine();
                        
                        if (reply.contains("false")) {
                            System.out.println("End turn");
                            turn = false;
                            break;
                        }
                    }
                    
                    while (!turn) { //PLAYER WAITS FOR OPPONENT IF TURN IS FALSE
                        System.out.println("Opponents turn.");
                        reply = br.readLine();
                        
                        if (reply.contains("true")) {
                            System.out.println("Your turn");
                            turn = true;
                            break;
                        }
                    }
                    
                    /*reply = br.readLine(); //MESSAGES INDICATING WIN OR LOSE OR INVALID MOVE

                    if (reply.contains("return")) {
                        reply = br.readLine();
                        System.out.println(reply);
                    }
                    else if (reply.contains("win")) {
                        reply = br.readLine();
                        System.out.println(reply);
                        System.out.println("Congratulation! You won the game.");
                        break;
                    } else if (reply.contains("invalid")) {
                        reply = br.readLine();
                        System.out.println(reply);
                        System.out.println("The move you made is invalid.");
                        continue;
                    }*/
                }
                
                socket.close();
                br.close();
                bw.close();
                break;
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}
