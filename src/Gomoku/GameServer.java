package Gomoku;

import Database.LoginConnection;
import com.mysql.cj.xdevapi.Client;
import javafx.scene.Group;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Random;

public class GameServer extends JFrame {
    private ServerSocket serverSocket = null;
    public static final int PORT = 4801;
    private JTextArea connectionNotice;
    private ArrayList<Client> clientList;
    private ArrayList<GomokuGame> gomukuGames;

    public static void main(String args[])
    {
        GameServer gs = new GameServer();
        gs.startServer();
    }

    public GameServer(){
        super("super Gomoku Server");
        gomukuGames = new ArrayList<>();

        setVisible(true);
        setLayout(null);
        setBounds(300,300,500,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        connectionNotice = new JTextArea();
        connectionNotice.setBounds(0,0,500,200);
        add(connectionNotice);
        clientList = new ArrayList<>();

    }

    public void startServer()
    {
        try
        {
            serverSocket = new ServerSocket(PORT);
            while(true)
            {
                Socket s = serverSocket.accept();
                InputStream is = s.getInputStream();
                OutputStream os = s.getOutputStream();
                DataInputStream dis = new DataInputStream(is);
                DataOutputStream dos = new DataOutputStream(os);
                String msgFromServer = dis.readUTF();
                String[] message = msgFromServer.split("\\|");
                if(message[0].equals(Command.LOGIN))
                {
                    //System.out.println("login part");
                    String account = message[1];
                    String password = message[2];
                    if(LoginConnection.vertifyAccount(account , password))
                    {
                        String[] retString = LoginConnection.getInfo(account);
                        String acc = retString[0];
                        String name = retString[1];
                        System.out.println("name retireved from server "+ name);
                        int pts = Integer.parseInt(retString[2]);
                        dos.writeUTF(Command.LOGIN + "|" + acc + "|" + name + "|" + pts);
                        Client newClient = new Client(name , s);
                        connectionNotice.append(name + " is" + " connected" + "\n");
                        connectionNotice.paintImmediately(connectionNotice.getBounds());
                        clientList.add(newClient);
                        addNewUserToCurrentUser(name);
                        addCurrentUserToNewUser(s);
                        new ServerThread(newClient).start();
                    }
                }

            }

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addNewUserToCurrentUser(String newUserName)
    {
        //System.out.println("addNewUserToCurrentUser called");
        try
        {

            for(int i = 0 ; i < clientList.size() ; i++)
            {
                if(newUserName.equals(clientList.get(i).getClientName()))
                {
                    continue;
                }
                Socket currentSocket = clientList.get(i).getSocket();
                DataOutputStream dos = new DataOutputStream(currentSocket.getOutputStream());
                dos.writeUTF(Command.UPDATE_USER+"|"+newUserName);
            }

        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public void addCurrentUserToNewUser(Socket newUserSocket)
    {
        Socket s = newUserSocket;
        try
        {
            String messageToSend = Command.INITIALIZE_USER;
            for(int i = 0 ; i < clientList.size() ; i++)
            {
                messageToSend = messageToSend + "|"+clientList.get(i).getClientName();
            }
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF(messageToSend);
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void tellUserNewGameRoom(int gamingType , int playerNum , String roomOwner)
    {
        try
        {
            for(int i = 0 ; i < clientList.size() ; i++)
            {
                Socket s = clientList.get(i).getSocket();
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(Command.CREATE_GAME + "|" + gamingType + "|" + playerNum + "|"+ roomOwner);
            }
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    //this method is called when the server receive a "enter room" message, meaning that an user has entered
    //a game room, and the information about the gameroom should be updated (the class GroupGame)
    public void updateRoomStatus(String playerToEnter , String roomOwner)
    {
        GomokuGame currentGomokuGame = null;
        for(int i = 0 ; i < gomukuGames.size() ; i++)
        {
            currentGomokuGame = gomukuGames.get(i);
            if(currentGomokuGame.getRoomOwner().equals(roomOwner))
            {
                currentGomokuGame.getPlayerInRoom().add(playerToEnter);
                break;
            }
        }

        System.out.println("update room status called");
        System.out.println("group game size" + gomukuGames.size());

        if(currentGomokuGame.getCurrentPlayerNum() == currentGomokuGame.getPlayerNum())
        {
            System.out.println("game start");
            System.out.println("current player num" + currentGomokuGame.getPlayerNum());
            tellPlayersStart(currentGomokuGame);
        }

    }

    public GomokuGame findRoomByPlayer(String player)
    {
        GomokuGame foundRoom = null;

        for(int i = 0 ; i < gomukuGames.size() ; i++)
        {
            GomokuGame currentGroupGame = gomukuGames.get(i);
            if(currentGroupGame.getPlayerInRoom().contains(player))
            {
                foundRoom = currentGroupGame;
                break;
            }
        }

        return foundRoom;
    }

    public void tellPlayersStart(GomokuGame gameRoom)
    {
        try
        {
            int gamingMode = gameRoom.getGamingMode();
            int playerNum = gameRoom.getPlayerNum();
            if(gamingMode == 1)
            {
                if(playerNum == 3)
                {
                    ArrayList<String> playersInRoom = gameRoom.getPlayerInRoom();
                    String player1 = playersInRoom.get(0);
                    String player2 = playersInRoom.get(1);
                    String player3 = playersInRoom.get(2);

                    Socket s1 = findSocketByName(player1);
                    Socket s2 = findSocketByName(player2);
                    Socket s3 = findSocketByName(player3);

                    DataOutputStream dos1 = new DataOutputStream(s1.getOutputStream());
                    DataOutputStream dos2 = new DataOutputStream(s2.getOutputStream());
                    DataOutputStream dos3 = new DataOutputStream(s3.getOutputStream());

                    dos1.writeUTF(Command.GAME_START);
                    dos2.writeUTF(Command.GAME_START);
                    dos3.writeUTF(Command.GAME_START);

                    dos1.writeUTF(Command.ASSIGN_COLOR + "|" + gamingMode + "|" + playerNum + "|" + player1 + "|" + player2 + "|" + player3);
                    dos2.writeUTF(Command.ASSIGN_COLOR + "|" + gamingMode + "|" + playerNum + "|" + player1 + "|" + player2 + "|" + player3);
                    dos3.writeUTF(Command.ASSIGN_COLOR + "|" + gamingMode + "|" + playerNum + "|" + player1 + "|" + player2 + "|" + player3);

                }

                else if(playerNum == 4)
                {
                    ArrayList<String> playersInRoom = gameRoom.getPlayerInRoom();
                    String player1 = playersInRoom.get(0);
                    String player2 = playersInRoom.get(1);
                    String player3 = playersInRoom.get(2);
                    String player4 = playersInRoom.get(3);

                    Socket s1 = findSocketByName(player1);
                    Socket s2 = findSocketByName(player2);
                    Socket s3 = findSocketByName(player3);
                    Socket s4 = findSocketByName(player4);

                    DataOutputStream dos1 = new DataOutputStream(s1.getOutputStream());
                    DataOutputStream dos2 = new DataOutputStream(s2.getOutputStream());
                    DataOutputStream dos3 = new DataOutputStream(s3.getOutputStream());
                    DataOutputStream dos4 = new DataOutputStream(s4.getOutputStream());

                    dos1.writeUTF(Command.GAME_START);
                    dos2.writeUTF(Command.GAME_START);
                    dos3.writeUTF(Command.GAME_START);
                    dos4.writeUTF(Command.GAME_START);

                    dos1.writeUTF(Command.ASSIGN_COLOR + "|" + gamingMode + "|" + playerNum + "|" + player1 + "|" + player2 + "|" + player3 + "|" + player4);
                    dos2.writeUTF(Command.ASSIGN_COLOR + "|" + gamingMode + "|" + playerNum + "|" + player1 + "|" + player2 + "|" + player3 + "|" + player4);
                    dos3.writeUTF(Command.ASSIGN_COLOR + "|" + gamingMode + "|" + playerNum + "|" + player1 + "|" + player2 + "|" + player3 + "|" + player4);
                    dos4.writeUTF(Command.ASSIGN_COLOR + "|" + gamingMode + "|" + playerNum + "|" + player1 + "|" + player2 + "|" + player3 + "|" + player4);
                }
            }

            else
            {
                if(playerNum == 2) {
                    ArrayList<String> playersInRoom = gameRoom.getPlayerInRoom();
                    String player1 = playersInRoom.get(0);
                    String player2 = playersInRoom.get(1);
                    Socket s1 = findSocketByName(player1);
                    Socket s2 = findSocketByName(player2);
                    DataOutputStream dos1 = new DataOutputStream(s1.getOutputStream());
                    DataOutputStream dos2 = new DataOutputStream(s2.getOutputStream());
                    dos1.writeUTF(Command.GAME_START);
                    dos2.writeUTF(Command.GAME_START);
                    dos1.writeUTF(Command.ASSIGN_COLOR + "|" + gamingMode + "|" + playerNum + "|" + player1 + "|" + player2);
                    dos2.writeUTF(Command.ASSIGN_COLOR + "|" + gamingMode + "|" + playerNum + "|" + player1 + "|" + player2);
                    System.out.println("assign color sent for 2 players sent");
                }

                else if(playerNum == 4)
                {
                    ArrayList<String> playersInRoom = gameRoom.getPlayerInRoom();
                    String player1 = playersInRoom.get(0);
                    String player2 = playersInRoom.get(1);
                    String player3 = playersInRoom.get(2);
                    String player4 = playersInRoom.get(3);
                    Socket s1 = findSocketByName(player1);
                    Socket s2 = findSocketByName(player2);
                    Socket s3 = findSocketByName(player3);
                    Socket s4 = findSocketByName(player4);
                    DataOutputStream dos1 = new DataOutputStream(s1.getOutputStream());
                    DataOutputStream dos2 = new DataOutputStream(s2.getOutputStream());
                    DataOutputStream dos3 = new DataOutputStream(s3.getOutputStream());
                    DataOutputStream dos4 = new DataOutputStream(s4.getOutputStream());
                    dos1.writeUTF(Command.GAME_START);
                    dos2.writeUTF(Command.GAME_START);
                    dos3.writeUTF(Command.GAME_START);
                    dos4.writeUTF(Command.GAME_START);
                    dos1.writeUTF(Command.ASSIGN_COLOR + "|" + gamingMode + "|" + playerNum + "|" + player1 + "|" + player2 + "|" + player3 + "|" + player4);
                    dos2.writeUTF(Command.ASSIGN_COLOR + "|" + gamingMode + "|" + playerNum + "|" + player1 + "|" + player2 + "|" + player3 + "|" + player4);
                    dos3.writeUTF(Command.ASSIGN_COLOR + "|" + gamingMode + "|" + playerNum + "|" + player1 + "|" + player2 + "|" + player3 + "|" + player4);
                    dos4.writeUTF(Command.ASSIGN_COLOR + "|" + gamingMode + "|" + playerNum + "|" + player1 + "|" + player2 + "|" + player3 + "|" + player4);
                }
            }

        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }




    public void updateAllPlayerSign(String roomOwner)
    {
        try
        {
            GomokuGame foundGroupGame = findRoomByPlayer(roomOwner);
            ArrayList<String> allPlayersInRoom = foundGroupGame.getPlayerInRoom();
            String allPlayerNames = Command.UPDATE_PLAYER_IN_ROOM;
            for(int i = 0 ; i < allPlayersInRoom.size() ; i++)
            {
                allPlayerNames = allPlayerNames + "|" + allPlayersInRoom.get(i);
            }
            for(int i = 0 ; i < allPlayersInRoom.size() ; i++)
            {
                Socket s = findSocketByName(allPlayersInRoom.get(i));
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(allPlayerNames);
            }

        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }




    public Socket findSocketByName(String clientName)
    {

        Socket socket = null;
        for(int i = 0 ; i < clientList.size() ; i++)
        {
            Client c = clientList.get(i);
            if(c.getClientName().equals(clientName))
            {
                socket =  c.getSocket();
            }
        }

        System.out.println("find socket by name called and the returned socket is " + socket);
        return socket;
    }

    public void tellGoToOthers(String sender , String color , String row , String column , ArrayList<String> allOthersInRoom)
    {
        try
        {
            for(int i = 0 ; i < allOthersInRoom.size() ; i++)
            {
                String currentPlayer = allOthersInRoom.get(i);
                if(currentPlayer.equals(sender))
                {
                    continue;
                }
                Socket s = findSocketByName(currentPlayer);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(Command.GO + "|" + sender + "|" + color + "|" + row + "|" + column);

            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void tellNextGo(String sender , ArrayList<String> allPlayersInRoom)
    {
        try
        {
            int playerIndexInRoom = allPlayersInRoom.indexOf(sender);
            int newIndex;
            if(playerIndexInRoom == allPlayersInRoom.size() - 1)
            {
                newIndex = 0;
            }
            else
            {
                newIndex = playerIndexInRoom + 1;
            }

            String playerToNotify = allPlayersInRoom.get(newIndex);
            Socket s = findSocketByName(playerToNotify);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF(Command.MOVE);
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

    }




    class Client
    {
        private String clientName;
        private Socket s;
        public Client(String clientName , Socket s)
        {
            this.clientName = clientName;
            this.s = s;
        }
        public String getClientName()
        {
            return clientName;
        }

        public Socket getSocket()
        {
            return s;
        }
    }

    class ServerThread extends Thread
    {
        private DataInputStream dis;
        private DataOutputStream dos;
        private Client c;
        public ServerThread(Client c)
        {
            this.c = c;
        }

        public void run()
        {
            while(true)
            {
                try{
                    dis = new DataInputStream(c.getSocket().getInputStream());
                    String clientMessage = dis.readUTF();
                    String[] message = clientMessage.split("\\|");

                    if(message[0].equals(Command.CREATE_GAME))
                    {
                        System.out.println("server received");
                        int gamingType = Integer.parseInt(message[1]);
                        int playerNum = Integer.parseInt(message[2]);
                        String roomOwner = message[3];
                        System.out.println("room owner " + roomOwner);
                        GomokuGame gg = new GomokuGame(gamingType , playerNum + 1 , roomOwner);
                        System.out.println("gameType" + gamingType + "plaerNum"+playerNum+1);
                        gomukuGames.add(gg);
                        tellUserNewGameRoom(gamingType , playerNum , roomOwner);
                    }

                    else if(message[0].equals(Command.ENTER_ROOM))
                    {
                        System.out.println("system receive enter room");
                        String playerToEnter = message[1];
                        String roomOwner = message[2];
                        updateRoomStatus(playerToEnter , roomOwner);
                        updateAllPlayerSign(roomOwner);
                    }

                    else if(message[0].equals(Command.GO))
                    {
                        String sender = message[1];
                        String color = message[2];
                        String row = message[3];
                        String column = message[4];
                        GomokuGame gameRoom = findRoomByPlayer(sender);
                        ArrayList<String> allPlayersInRoom = gameRoom.getPlayerInRoom();
                        tellGoToOthers(sender , color , row , column , allPlayersInRoom);
                        tellNextGo(sender , allPlayersInRoom);

                    }

                    else if(message[0].equals(Command.TEXT))
                    {
                        String sender = message[1];
                        String text = message[2];
                        ArrayList<String> playerInRoom = findRoomByPlayer(sender).getPlayerInRoom();
                        for(int i = 0 ; i < playerInRoom.size() ; i++)
                        {
                           String currentPlayer = playerInRoom.get(i);
                           if(currentPlayer.equals(sender))
                           {
                               continue;
                           }
                           Socket s = findSocketByName(currentPlayer);
                           DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                           dos.writeUTF(Command.TEXT + "|" + sender + "|" + text);
                        }
                    }

                    else if(message[0].equals(Command.WIN))
                    {
                        String winner = message[1];
                        GomokuGame gameRoom = findRoomByPlayer(winner);
                        gomukuGames.remove(gameRoom);
                        if(gameRoom.getGamingMode() == 1){
                            ArrayList<String> playersInRoom = gameRoom.getPlayerInRoom();
                            for(int i = 0 ; i < playersInRoom.size() ; i++)
                            {
                                if(playersInRoom.get(i).equals(winner))
                                {
                                    continue;
                                }
                                String loser = playersInRoom.get(i);
                                Socket s = findSocketByName(loser);
                                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                                dos.writeUTF(Command.LOSE);
                            }

                        }
                        else if(gameRoom.getGamingMode() == 2)
                        {
                            if(gameRoom.getPlayerNum() == 2)
                            {
                                ArrayList<String> playersInRoom = gameRoom.getPlayerInRoom();
                                if(playersInRoom.get(0).equals(winner))
                                {
                                    String loser = playersInRoom.get(1);
                                    Socket s = findSocketByName(loser);
                                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                                    dos.writeUTF(Command.LOSE);
                                }
                                else
                                {
                                    String loser = playersInRoom.get(0);
                                    Socket s = findSocketByName(loser);
                                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                                    dos.writeUTF(Command.LOSE);
                                }
                            }

                            else if(gameRoom.getPlayerNum() == 4)
                            {
                                ArrayList<String> playerInRoom = gameRoom.getPlayerInRoom();
                                if(playerInRoom.get(0).equals(winner) || playerInRoom.get(1).equals(winner))
                                {
                                    String winner2;
                                    if(playerInRoom.get(0).equals(winner))
                                    {
                                        winner2 = playerInRoom.get(1);
                                    }

                                    else
                                    {
                                        winner2 = playerInRoom.get(0);
                                    }
                                    String loser1 = playerInRoom.get(2);
                                    String loser2 = playerInRoom.get(3);

                                    Socket socketWinner2 = findSocketByName(winner2);
                                    DataOutputStream dos = new DataOutputStream(socketWinner2.getOutputStream());
                                    dos.writeUTF(Command.WIN);

                                    Socket socketLoser1 = findSocketByName(loser1);
                                    dos = new DataOutputStream(socketLoser1.getOutputStream());
                                    dos.writeUTF(Command.LOSE);

                                    Socket socketLoser2 = findSocketByName(loser2);
                                    dos = new DataOutputStream(socketLoser2.getOutputStream());
                                    dos.writeUTF(Command.LOSE);
                                }

                                else
                                {
                                    String winner2;
                                    if(playerInRoom.get(2).equals(winner))
                                    {
                                        winner2 = playerInRoom.get(3);
                                    }
                                    else
                                    {
                                        winner2 = playerInRoom.get(2);
                                    }
                                    String loser1 = playerInRoom.get(0);
                                    String loser2 = playerInRoom.get(1);

                                    Socket socketWinner2 = findSocketByName(winner2);
                                    DataOutputStream dos = new DataOutputStream(socketWinner2.getOutputStream());
                                    dos.writeUTF(Command.WIN);

                                    Socket socketLoser1 = findSocketByName(loser1);
                                    dos = new DataOutputStream(socketLoser1.getOutputStream());
                                    dos.writeUTF(Command.LOSE);

                                    Socket socketLoser2 = findSocketByName(loser2);
                                    dos = new DataOutputStream(socketLoser2.getOutputStream());
                                    dos.writeUTF(Command.LOSE);
                                }
                            }

                        }
                    }

                    else if(message[0].equals(Command.QUIT))
                    {
                        String userToQuit = message[1];
                        GomokuGame game = findRoomByPlayer(userToQuit);
                        gomukuGames.remove(game);
                    }


                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }


    }




}
