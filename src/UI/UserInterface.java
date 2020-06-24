package UI;

import Gomoku.GameClientLogic;

import javax.swing.*;
import java.util.Random;

public class UserInterface extends JFrame {
    private GameClientLogic gcl;
    private MainInterface mi;
    private GameCenterUI gcui;
    private GamePanel gp;
    private GameLogin glogin;


    public UserInterface(GameClientLogic gcl)
    {
        this.gcl = gcl;
        setBounds(0 , 0, 1200 , 680);
        glogin = new GameLogin(this);
        add(glogin);
        gcui = new GameCenterUI(this);
        add(gcui);
        gcl.setUserInterface(this);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void updateUserInterface()
    {
        gp = new GamePanel(this);
    }

    public void setMainInterface()
    {
        mi = new MainInterface(this , gcl);
        add(mi);
    }

    public void showMainInterface()
    {
        gp.setVisible(false);
        mi.setVisible(true);
    }

    public void showGamePanel(int gamingType , int playerNum)
    {
        updateGamePanel(gamingType , playerNum);
        gp.setVisible(false);
        add(gp);
        gp.setVisible(true);
    }


    public void showGameCenter()
    {
        mi.setVisible(false);
        gcui.updateGameRoomUI();
        gcui.setVisible(true);
    }

    public void updateGamePanel(int gamingType , int playerNum)
    {
        gp.getGamingModelLabel().setTextContext(gamingType , playerNum);
    }

    public MainInterface getMainInterface()
    {
        return mi;
    }

    public GamePanel getGamePanel()
    {
        return gp;
    }

    public GameClientLogic getGameClientLogic()
    {
        return gcl;
    }

    public GameCenterUI getGameCenterUI() {return gcui;}

    public GameLogin getGameLogin() {return glogin;}

    public void updateGP()
    {
        gp = new GamePanel(this);
    }

    public static void main(String args[])
    {

        GameClientLogic newClientLogic = new GameClientLogic();
        new UserInterface(newClientLogic);
    }
}
