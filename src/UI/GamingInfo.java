package UI;

import javax.swing.*;
import java.awt.*;

public class GamingInfo extends JPanel {
    private GamePanel gp;
    private JTextArea infoArea;

    public GamingInfo(GamePanel gp)
    {
        System.out.println("ginfo constructor called");
        setBounds(650,180,450,200);
        setLayout(new BorderLayout());
        infoArea = new JTextArea();
        this.gp = gp;
        String gamingNotice = gp.getUserInterface().getGameClientLogic().getMyname() + " has enterted this room";
        infoArea.setText(gamingNotice);
        infoArea.setBackground(Color.WHITE);
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setVisible(true);
        add(new JScrollPane(infoArea) , BorderLayout.CENTER);
        setVisible(true);
        gp.add(this);
        gp.repaint();
    }


    public void appendGoing()
    {
        infoArea.append("\n" + "it's your turn");
        updateUI();
    }

    public JTextArea getInfoArea()
    {
        return infoArea;
    }

}
