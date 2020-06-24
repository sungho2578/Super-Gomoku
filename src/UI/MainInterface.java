package UI;

import Gomoku.GameClientLogic;
import UI.UserInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainInterface extends JPanel {

    private UserInterface ui;
    private ImageIcon myImg;
    private JTextField account;
    private JTextField myName;
    private JTextField points;
    private Image backgroudImage;
    private JTextArea announcement;
    private JTextArea recentGamingRecord;
    private JTextArea onlineUsers;
    private JDialog createRoomDetail;
    private JButton enterGameCenter;
    private JButton createGameRoom;
    private JButton quitButton;
    private GameClientLogic gcl;



    public MainInterface(UserInterface ui , GameClientLogic gcl)
    {
        this.ui = ui;
        this.gcl = gcl;
        backgroudImage = Toolkit.getDefaultToolkit().getImage("img/mainBg.jpg");
        setLayout(null);
        setBounds(0,0,1200,680);
        setRecentGamingRecord();
        setMyIcon();
        setAccount();
        setMyName();
        setPoints();
        setAnnouncement();
        setOnlineUsers();
        setEnterGameCenter();
        setCreateGameRoom();
        setQuit();
        setVisible(true);

    }

    public void setMyIcon()
    {
        myImg =  new ImageIcon("img/black.jpg");
        JLabel jl = new JLabel(myImg);
        jl.setBounds(10,10,250,250);
        add(jl);
    }

    public void setAccount()
    {
        account = new JTextField();
        account.setBackground(Color.WHITE);
        account.setBounds(320 , 30 , 200 , 50);
        account.setText(" account info: " + gcl.getAccount());
        account.setEditable(false);
        add(account);
    }

    public void setMyName()
    {
        myName = new JTextField();
        myName.setBackground(Color.WHITE);
        myName.setBounds(320 , 80 , 200 , 50);
        myName.setText(" Name: "+ gcl.getMyname());
        myName.setEditable(false);
        add(myName);
    }

    public void setPoints()
    {
        points = new JTextField();
        points.setBackground(Color.WHITE);
        points.setBounds(320 , 130 , 200 ,50);
        points.setText(" points: "+ gcl.getPoints());
        points.setEditable(false);
        add(points);
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(backgroudImage , 0 , 0 , getWidth() , getHeight() , this);
    }

    public void setAnnouncement()
    {
        announcement = new JTextArea();
        announcement.setBackground(Color.WHITE);
        announcement.setBounds(20 , 320, 550, 280);
        announcement.setEditable(false);
        announcement.setFont(new Font("Times New Roman" , Font.BOLD , 20));
        announcement.setText("             --------------Announcement-------------------\n Welcome to Super Gomoku " );
        add(announcement);

    }

    public void setRecentGamingRecord()
    {
        recentGamingRecord = new JTextArea();
        recentGamingRecord.setBackground(Color.WHITE);
        recentGamingRecord.setBounds(650,40,500,200);
        recentGamingRecord.setEditable(false);
        recentGamingRecord.setFont(new Font("Times New Roman" , Font.ITALIC , 20));
        recentGamingRecord.setText("         --------your recent gaming records are as follow:");
        add(recentGamingRecord);
    }

    public void setOnlineUsers()
    {
        onlineUsers = new JTextArea();
        onlineUsers.setBackground(Color.WHITE);
        onlineUsers.setBounds(650 , 330 , 500 , 200);
        onlineUsers.setEditable(false);
        onlineUsers.setFont(new Font("Times New Roman" , Font.ITALIC , 20));
        onlineUsers.setText("        ----------current Online Users\n");
        for(int i = 0 ; i < gcl.getOnlineUsers().size() ; i++)
        {
            String currentOlName = gcl.getOnlineUsers().get(i);
            onlineUsers.append("\n" + currentOlName + " is online ");
        }
        add(onlineUsers);
    }

    public void updateOnlineUsers()
    {
        onlineUsers.setText("        ----------current Online Users\n");

        for(int i = 0 ; i < gcl.getOnlineUsers().size() ; i++)
        {
            String currentOlName = gcl.getOnlineUsers().get(i);
            onlineUsers.append("\n" + currentOlName + " is online ");
        }
    }

    public void setEnterGameCenter()
    {
        enterGameCenter = new JButton("see all rooms");
        enterGameCenter.setBounds(650 , 550 , 150 , 50);
        enterGameCenter.setEnabled(true);
        enterGameCenter.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                ui.showGameCenter();
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

        add(enterGameCenter);

    }

    public void setCreateGameRoom(){
        createGameRoom = new JButton("create game room");
        createGameRoom.setBounds(820 , 550 , 150 ,50);
        createGameRoom.setEnabled(true);
        createGameRoom.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                setCreateRoomDetail();
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
        add(createGameRoom);
    }

    public void setCreateRoomDetail()
    {
        createRoomDetail = new JDialog(ui, "create a new game room", true);
        createRoomDetail.setLayout(new FlowLayout());
        JComboBox gameType = new JComboBox();
        gameType.addItem("--please select a game type--");
        gameType.addItem("individual competitive version");
        gameType.addItem("group competitive version");
        createRoomDetail.add(gameType);
        JComboBox maxNum = new JComboBox();
        maxNum.addItem("--please select the number of people in the game");
        maxNum.addItem("2(only allowed for group competitive mode");
        maxNum.addItem("3(only allowed for individual mode)");
        maxNum.addItem("4");
        createRoomDetail.add(maxNum);

        createRoomDetail.setBounds(300,200,400,150);
        JButton confirmButton = new JButton("confirm");
        createRoomDetail.add(confirmButton);

        confirmButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int gamingType = gameType.getSelectedIndex();
                int playerNum = maxNum.getSelectedIndex();
                createRoomDetail.setVisible(false);
                System.out.println("gcl name is " + gcl.getMyname());
                gcl.createGameRoom(gamingType , playerNum , gcl.getMyname());
                setVisible(false);
                ui.showGamePanel(gamingType , playerNum);

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

        createRoomDetail.setVisible(true);

    }


    public void setQuit()
    {
        quitButton = new JButton("quit");
        quitButton.setBounds(990 , 550 , 150 ,50);
        quitButton.setEnabled(true);
        add(quitButton);
    }

}
