package UI;

import UI.GamePanel;

import javax.swing.*;
import java.awt.*;

public class GamingModeLabel extends JTextArea {
    private int gamingType;
    private int playerNum;

    public GamingModeLabel(GamePanel gp)
    {
        this.gamingType = gamingType;
        this.playerNum = playerNum;
        setBounds(700 , 20 , 300, 50);
        setForeground(Color.DARK_GRAY);
        setEditable(false);
        setFont(new Font("Times New Roman" , Font.BOLD , 20));
        gp.add(this);
        gp.repaint();
    }

    public void setTextContext(int gamingType , int playerNum)
    {
        if(gamingType == 1)
        {
            setText("individual competitive mode");
        }

        else
        {
            setText("group competitive mode");
        }

        if(playerNum == 1)
        {
            append("\n number of people: 2");
        }

        else if(playerNum == 2)
        {
            append("\n number of people: 3");
        }

        else
        {
            append("\n number of people: 4");
        }
    }
}

