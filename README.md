# Nim Multiplayer Puzzle Game
Nim is a Java program that I made for a university assignment. There are two classes. The client class sends and receives inputs and outputs from the server class. This allows two instances of the client class to play Nim together.  

To run the program, there is a specific sequence of actions required to play Nim. It follows as:
1.	Run Server.java. Then, input your difficult via Server.java console.
2.	Run Client.java to connect player 1 to Server.java.
3.	Run Client.java again to connect player 2 to Server.java.
4.	Player 1 or 2 can now play while the other awaits their turn. 

The rules and winning strategies are: 
A.  Whichever player removes the last marble loses the game.
B.  A player can remove half of the pile or less.
C.  Odd numbers are rounded down. For example, 67/2 is 33 remainder 1 or 3/2 = 1 remainder 1. The remainder is dismissed.
D.  When 3 or less marbles are left, only 1 can be taken.
