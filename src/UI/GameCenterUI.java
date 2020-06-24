package UI;

import UI.UserInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class GameCenterUI extends JPanel {
    UserInterface ui;
    private ArrayList<GameRoomData> gameRoomDataList;

    public GameCenterUI(UserInterface ui)
    {
        this.ui = ui;
        gameRoomDataList = new ArrayList<>();
        setBounds(0,0,1200,680);
        setLayout(new FlowLayout());
        setVisible(false);
    }

    public UserInterface getUserInterface()
    {
        return ui;
    }

    public void addNewGameRoom(int gameModel , int playerNum , String roomOwner)
    {
        GameRoomData newRoomData = new GameRoomData(gameModel , playerNum , roomOwner);
        gameRoomDataList.add(newRoomData);
    }

    public void updateGameRoomUI()
    {
        for(int i = 0 ; i < gameRoomDataList.size() ; i++)
        {
            GameRoomData currentRoom = gameRoomDataList.get(i);
            int gamingMode = currentRoom.getGamingMode();
            int playerNum = currentRoom.getPlayerNum();
            String roomOwner = currentRoom.getRoomOwner();
            JButton enterButton;

            JPanel newRoom = new JPanel();
            newRoom.setPreferredSize(new Dimension(200, 300));
            newRoom.setSize(200,300);
            newRoom.setBackground(Color.green);
            newRoom.setLayout(new BorderLayout());

            if(gamingMode == 1)
            {
                JTextArea jtf = new JTextArea("Game: individual competitive mode");
                jtf.setSize(100,200);
                jtf.setBackground(Color.PINK);
                jtf.setEditable(false);
                newRoom.add(jtf , BorderLayout.CENTER);
                if(playerNum == 1)
                {
                    jtf.append("\n player num: 2");
                }

                else if(playerNum == 2)
                {
                    jtf.append("\n player num: 3");
                }

                else if(playerNum == 3)
                {
                    jtf.append("\n player num: 4");
                }
                jtf.append("\n room owner: " + roomOwner);
            }

            else
            {
                JTextArea jtf = new JTextArea("Game: group competitive mode");
                jtf.setSize(100,200);
                //jtf.setBounds(0,0,100,200);
                jtf.setBackground(Color.PINK);
                jtf.setEditable(false);
                newRoom.add(jtf , BorderLayout.CENTER);

                if(playerNum == 1)
                {
                    jtf.append("\n player num: 2");
                }

                else if(playerNum == 2)
                {
                    jtf.append("\n player num: 3");
                }

                else if(playerNum == 3)
                {
                    jtf.append("\n player num: 4");
                }
                jtf.append("\n room owner: " + roomOwner);
            }

            enterButton = new JButton("enter");
            newRoom.add(enterButton , BorderLayout.SOUTH);

            enterButton.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    setVisible(false);
                    ui.getGameClientLogic().enterGameRoom(roomOwner);
                    ui.showGamePanel(gamingMode , playerNum);
                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {

                }
            });

            add(newRoom);
            System.out.println("add_new_room_called");
            newRoom.setVisible(true);
            updateUI();


        }
    }




    class GameRoomData
    {
        int gamingMode;
        int playerNum;
        String roomOwner;

        public GameRoomData(int gamingMode , int playerNum , String roomOwner)
        {
            this.gamingMode = gamingMode;
            this.playerNum = playerNum;
            this.roomOwner = roomOwner;
        }

        public int getGamingMode()
        {
            return gamingMode;
        }

        public int getPlayerNum()
        {
            return playerNum;
        }

        public String getRoomOwner()
        {
            return roomOwner;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof  GameRoomData)
            {
                GameRoomData grd = (GameRoomData)obj;
                if(grd.getRoomOwner().equals(this.getRoomOwner()))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }

            else
            {
                return false;
            }
        }
    }




}
