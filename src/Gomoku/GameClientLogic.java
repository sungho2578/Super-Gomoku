package Gomoku;

import Database.LoginConnection;
import UI.ChattingPanel;
import UI.GamingInfo;
import UI.UserInterface;
import com.mysql.cj.protocol.a.MysqlBinaryValueDecoder;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class GameClientLogic {
    private String myName;
    private String account;
    private int points;
    private Socket s;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Color myColor;
    private boolean isGaming;
    private boolean isGoing;
    private UserInterface ui;
    private ArrayList<String> onlineUsers;

    public GameClientLogic()
    {
        onlineUsers = new ArrayList<>();
        isGaming = false;
        isGoing = false;
    }


    public void setMyColor(Color myColor)
    {
        this.myColor = myColor;
    }

    public Color getMyColor()
    {
        return myColor;
    }

    public String getMyColorName()
    {
        if(myColor == Color.BLACK)
        {
            return "BLACK";
        }

        else if(myColor == Color.WHITE)
        {
            return "WHITE";
        }

        else if(myColor == Color.GREEN)
        {
            return "GREEN";
        }

        else
        {
            return "BLUE";
        }

    }


    public void setGamingStatus(boolean isGaming)
    {
        this.isGaming = isGaming;
    }

    public boolean getGamingStatus()
    {
        return isGaming;
    }

    public ArrayList<String> getOnlineUsers()
    {
        return onlineUsers;
    }

    public void setIsGoing(boolean isGoing) {

        this.isGoing = isGoing;
        if (isGoing == true)
        {
            ui.getGamePanel().getGamingInfo().appendGoing();
        }
    }

    public void setUserInterface(UserInterface ui)
    {
        this.ui = ui;
    }

    public boolean getIsGoing()
    {
        return isGoing;
    }

    public boolean connect(String account , String password)
    {
        try{
            s = new Socket(Command.SEVER_IP , GameServer.PORT);
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF(Command.LOGIN+"|"+ account + "|" + password);
            new ReceiveThread(s).start();
            return true;
        }

        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }


    public void createGameRoom(int gamingType , int playerNum , String roomOwner)
    {
        try
        {
            dos.writeUTF(Command.CREATE_GAME + "|" + gamingType + "|" + playerNum + "|" + roomOwner);
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public void sendGo(int row, int column)
    {
        try
        {
            dos.writeUTF(Command.GO + "|" + ui.getGameClientLogic().getMyname() + "|" + getMyColorName() + "|" + row + "|" + column);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public void sendChattingText(String textToSend , String sender)
    {
        try{
            dos.writeUTF(Command.TEXT + "|" + sender + "|" + textToSend);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void enterGameRoom(String roomOwner)
    {
        try{
            //System.out.println("enter_game_room_called");
            dos.writeUTF(Command.ENTER_ROOM + "|" + myName + "|" + roomOwner);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Color findCorrespondingColor(String color)
    {
        if(color.equals("BLACK"))
        {
            return Color.BLACK;
        }

        else if(color.equals("WHITE"))
        {
            return Color.WHITE;
        }

        else if(color.equals("BLUE"))
        {
            return Color.BLUE;
        }

        else
        {
            return Color.GREEN;
        }
    }

    public void sendWin()
    {
        try
        {
            dos.writeUTF(Command.WIN + "|" + myName);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateQuit()
    {
        try
        {
            dos.writeUTF(Command.QUIT + "|" + myName);
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getAccount()
    {
        return account;
    }

    public void setMyName(String name)
    {
        myName = name;
    }

    public String getMyname()
    {
        return myName;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }

    public int getPoints()
    {
        return points;
    }

    class ReceiveThread extends Thread{
        public Socket s;
        DataInputStream dis;
        DataOutputStream dos;

        public ReceiveThread(Socket s)
        {
            this.s = s;
        }

        public void run()
        {
            try
            {
                dis = new DataInputStream(s.getInputStream());
                while(true)
                {
                    String msgFromServer = dis.readUTF();
                    String[] message = msgFromServer.split("\\|");

                    if(message[0].equals(Command.LOGIN))
                    {
                        String account = message[1];
                        String name = message[2];
                        int points = Integer.parseInt(message[3]);
                        setAccount(account);
                        setMyName(name);
                        setPoints(points);
                        ui.updateUserInterface();
                        ui.getGameLogin().setVisible(false);
                        ui.setMainInterface();

                    }

                    else if(message[0].equals(Command.INITIALIZE_USER))
                    {
                        for(int i = 1 ; i < message.length ; i++)
                        {
                            System.out.println("message" + message[i]);
                            onlineUsers.add(message[i]);
                        }
                        ui.repaint();
                    }

                    else if(message[0].equals(Command.UPDATE_USER))
                    {
                        //System.out.println("update_new_user" + message[1]);
                        String newUserName = message[1];
                        onlineUsers.add(newUserName);
                        ui.getMainInterface().updateOnlineUsers();
                        System.out.println(onlineUsers.size());
                        ui.repaint();
                    }

                    else if(message[0].equals(Command.CREATE_GAME))
                    {
                        System.out.println("received_command");
                        int gamingmode = Integer.parseInt(message[1]);
                        int playerNum = Integer.parseInt(message[2]);
                        String roomOwner = message[3];
                        ui.getGameCenterUI().addNewGameRoom(gamingmode , playerNum , roomOwner);

                    }

                    else if(message[0].equals(Command.UPDATE_PLAYER_IN_ROOM))
                    {
                        System.out.println("received_update_player_in_room");
                        ArrayList<String> playerInRoom = new ArrayList<>();
                        for(int i = 1 ; i < message.length ; i++)
                        {
                            playerInRoom.add(message[i]);
                        }
                        ui.getGamePanel().getPlayerSign().updatePlayerSign(playerInRoom);
                    }

                    else if(message[0].equals(Command.GAME_START))
                    {
                        ui.getGamePanel().getGamingInfo().getInfoArea().append("\n Game Start");
                        setGamingStatus(true);
                    }

                    else if(message[0].equals(Command.ASSIGN_COLOR))
                    {
                        int gamingMode = Integer.parseInt(message[1]);
                        int playerNum = Integer.parseInt(message[2]);

                        if (gamingMode == 1)
                        {
                            if(playerNum == 3)
                            {
                                String blackPlayer = message[3];
                                String whitePlayer = message[4];
                                String bluePlayer = message[5];
                                ui.getGamePanel().getGamingInfo().getInfoArea().append("\n" + blackPlayer + " has been assigned black");
                                ui.getGamePanel().getGamingInfo().getInfoArea().append("\n" + whitePlayer + " has been assigned white");
                                ui.getGamePanel().getGamingInfo().getInfoArea().append("\n" + bluePlayer  + " has been assigned blue");
                                if(myName.equals(blackPlayer))
                                {
                                    setMyColor(Color.BLACK);
                                    ui.getGamePanel().getGamingInfo().getInfoArea().append("\n please go first");
                                    setIsGoing(true);
                                }
                                else if(myName.equals(whitePlayer))
                                {
                                    setMyColor(Color.WHITE);
                                }
                                else
                                {
                                    setMyColor(Color.BLUE);
                                }
                            }

                            else
                            {
                                String blackPlayer = message[3];
                                String whitePlayer = message[4];
                                String bluePlayer = message[5];
                                String greenPlayer = message[6];
                                ui.getGamePanel().getGamingInfo().getInfoArea().append("\n" + blackPlayer + " has been assigned black");
                                ui.getGamePanel().getGamingInfo().getInfoArea().append("\n" + whitePlayer + " has been assigned white");
                                ui.getGamePanel().getGamingInfo().getInfoArea().append("\n" + bluePlayer  + " has been assigned blue");
                                ui.getGamePanel().getGamingInfo().getInfoArea().append("\n" + greenPlayer  + " has been assigned green");
                                if(myName.equals(blackPlayer))
                                {
                                    setMyColor(Color.BLACK);
                                    ui.getGamePanel().getGamingInfo().getInfoArea().append("\n please go first");
                                    setIsGoing(true);
                                }
                                else if(myName.equals(whitePlayer))
                                {
                                    setMyColor(Color.WHITE);
                                }
                                else if(myName.equals(bluePlayer))
                                {
                                    setMyColor(Color.BLUE);
                                }
                                else
                                {
                                    setMyColor(Color.GREEN);
                                }
                            }
                        }

                        else
                        {
                            if(playerNum == 2)
                            {
                                String blackPlayer = message[3];
                                String whitePlayer = message[4];
                                ui.getGamePanel().getGamingInfo().getInfoArea().append("\n" + blackPlayer + " has been assigned black");
                                ui.getGamePanel().getGamingInfo().getInfoArea().append("\n" + whitePlayer + " has been assigned white");
                                if(myName.equals(blackPlayer))
                                {
                                    setMyColor(Color.BLACK);
                                    ui.getGamePanel().getGamingInfo().getInfoArea().append("\n please go first");
                                    setIsGoing(true);
                                }
                                else
                                {
                                    setMyColor(Color.WHITE);
                                }
                            }

                            else if(playerNum == 4)
                            {
                                String blackPlayer1 = message[3];
                                String blackPlayer2 = message[4];
                                String whitePlayer1 = message[5];
                                String whitePlayer2 = message[6];
                                ui.getGamePanel().getGamingInfo().getInfoArea().append("\n" + blackPlayer1 + " and " + blackPlayer2 + " have been assigned black");
                                ui.getGamePanel().getGamingInfo().getInfoArea().append("\n" + whitePlayer1 + " and " + whitePlayer2 + " have been assigned white");
                                if(myName.equals(blackPlayer1) || myName.equals(blackPlayer2))
                                {
                                    setMyColor(Color.BLACK);
                                    if(myName.equals(blackPlayer1))
                                    {
                                        setIsGoing(true);
                                    }
                                }
                                else
                                {
                                    setMyColor(Color.WHITE);
                                }
                            }
                        }

                    }

                    else if(message[0].equals(Command.GO))
                    {
                        String sender = message[1];
                        String color = message[2];
                        int row = Integer.parseInt(message[3]);
                        int column = Integer.parseInt(message[4]);
                        Color c = findCorrespondingColor(color);
                        ui.getGamePanel().getGamingBoard().drawNewChessPiece(row , column , c);
                        ui.getGamePanel().getGamingInfo().getInfoArea().append("\n" + sender + " " + row + "," + column);

                    }

                    else if(message[0].equals(Command.MOVE))
                    {
                        setIsGoing(true);
                    }

                    else if(message[0].equals(Command.TEXT))
                    {
                        String sender = message[1];
                        String text= message[2];
                        ui.getGamePanel().getChattingPanel().updateTextArea(sender+" :" + text);
                    }

                    else if(message[0].equals(Command.WIN))
                    {
                        ui.getGamePanel().getGamingBoard().announceWinner();
                    }

                    else if(message[0].equals(Command.LOSE))
                    {
                        ui.getGamePanel().getGamingBoard().announceLoser();
                    }





                }
            }

            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }





}
