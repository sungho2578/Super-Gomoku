package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChattingPanel extends JPanel {
    JTextField inputText;
    JTextArea showText;
    GamePanel gp;
    String textToSend;


    public ChattingPanel(GamePanel gp)
    {
        this.gp = gp;
        setBackground(Color.pink);
        setLayout(new BorderLayout());
        //setBounds(920,0,300,200);
        setBounds(650 , 410 , 450, 200);
        inputText = new JTextField();
        showText = new JTextArea();
        inputText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    textToSend = inputText.getText() +"\n";
                    gp.getUserInterface().getGameClientLogic().sendChattingText(textToSend , gp.getUserInterface().getGameClientLogic().getMyname());
                    showText.append(gp.getUserInterface().getGameClientLogic().getMyname() +": " +textToSend);
                    inputText.setText("");
                    gp.repaint();
                }

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
        add(showText , BorderLayout.CENTER);
        add(inputText , BorderLayout.SOUTH);
        setVisible(true);
        gp.add(this);
        gp.repaint();

    }

    public void updateTextArea(String textToUpdate)
    {
        showText.append(textToUpdate);
        updateUI();
    }



}
