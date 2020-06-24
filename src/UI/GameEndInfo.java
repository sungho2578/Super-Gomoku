package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameEndInfo extends JDialog {
    private GamingBoard gb;
    private JPanel endInfo;
    private JTextArea winningInfo;
    private boolean isWin;
    private JButton confirmButton;

    public GameEndInfo(boolean isWin , GamingBoard gb)
    {
        this.gb = gb;
        this.isWin = isWin;
        setTitle("announcement");
        setModal(true);
        setLayout(new BorderLayout());
        setBounds(200,200,100,100);
        endInfo = new JPanel();
        endInfo.setLayout(new BorderLayout());
        setEndInfo();
        setVisible(true);
    }

    public void setEndInfo()
    {
        winningInfo = new JTextArea();
        if(isWin)
        {
            winningInfo.setText("congratulations! You win!");
        }
        else
        {
            winningInfo.setText("Sorry, you lose");
        }
        endInfo.add(winningInfo , BorderLayout.CENTER);

        confirmButton = new JButton("confirm");

        endInfo.add(confirmButton , BorderLayout.SOUTH);
        add(endInfo);

        confirmButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                gb.getGamePanel().getUserInterface().showMainInterface();
                gb.getGamePanel().getUserInterface().updateGP();
                setVisible(false);
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


    }
}
