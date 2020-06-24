package UI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlayerSign extends JPanel {
    private GamePanel gp;


    public PlayerSign(GamePanel gp)
    {
        this.gp = gp;
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBounds(700 , 100 , 300, 50);
        setBackground(Color.GRAY);
        add(new JLabel(gp.getUserInterface().getGameClientLogic().getMyname()));
        gp.add(this);
        repaint();

    }


    public void updatePlayerSign(ArrayList<String> currentPlayerInGame)
    {
        removeAll();
        for(int i = 0 ; i < currentPlayerInGame.size() ; i++)
        {
            System.out.println("current player" + currentPlayerInGame.get(i));
            add(new JLabel(currentPlayerInGame.get(i)));
        }
        updateUI();
    }




}
