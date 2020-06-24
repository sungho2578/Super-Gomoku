package UI;

import Gomoku.GameClientLogic;

import javax.swing.*;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    private GamingBoard gb;
    private GamingInfo gInfo;
    private ChattingPanel cp;
    private UserInterface ui;
    private GamingModeLabel gml;
    private PlayerSign vs;


    public GamePanel(UserInterface ui)
    {
        this.ui = ui;
        setLayout(null);
        setBounds(0,0,1200,680);
        gb = new GamingBoard(this);
        gInfo = new GamingInfo(this);
        cp = new ChattingPanel(this);
        gml = new GamingModeLabel(this);
        vs = new PlayerSign(this);
        setVisible(true);
    }


    public GamingBoard getGamingBoard()
    {
        return gb;
    }

    public GamingInfo getGamingInfo()
    {
        return gInfo;
    }

    public ChattingPanel getChattingPanel() {return cp;}

    public PlayerSign getPlayerSign() {return vs;}

    public GamingModeLabel getGamingModelLabel() {
        return gml;
    }

    public UserInterface getUserInterface()
    {
        return ui;
    }














}