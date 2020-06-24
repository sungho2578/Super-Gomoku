package Gomoku;

import java.util.ArrayList;

public class GomokuGame {
    private int gamingMode;
    private int playerNum;
    private String roomOwner;
    private ArrayList<String> playerInRoom;


    public GomokuGame(int gamingMode , int playerNum, String roomOwner)
    {
        this.gamingMode = gamingMode;
        this.roomOwner = roomOwner;
        this.playerNum = playerNum;
        playerInRoom = new ArrayList<>();
        playerInRoom.add(roomOwner);
    }

    public int getPlayerNum()
    {
        return playerNum;
    }

    public ArrayList<String> getPlayerInRoom()
    {
        return playerInRoom;
    }

    public String getRoomOwner()
    {
        return roomOwner;
    }

    public int getCurrentPlayerNum()
    {
        return playerInRoom.size();
    }

    public int getGamingMode()
    {
        return gamingMode;
    }
}
