package UI;

import Database.LoginConnection;
import Gomoku.Command;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameLogin extends JPanel{
    private Image backgroundImg;
    private UserInterface ui;
    private JLabel gameLabelText;
    private JLabel accountLabel;
    private JTextField accountInput;
    private JLabel password;
    private JPasswordField passwordInput;
    private JButton confirmButton;
    private JButton registerButton;

    public GameLogin(UserInterface ui)
    {
        this.ui = ui;
        setBounds(0, 0, 1200, 680);
        setLayout(null);
        backgroundImg = Toolkit.getDefaultToolkit().getImage("img/loginPic.png");
        setGameLabelText();
        setLoginArea();
        setVisible(true);

    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(backgroundImg , 0 , 0 , getWidth() , getHeight() , this);
    }

    public void setGameLabelText()
    {
        gameLabelText = new JLabel();
        gameLabelText.setFont(new Font(Font.DIALOG_INPUT,  Font.BOLD|Font.ITALIC, 50));
        gameLabelText.setText("Super Gomoku");
        gameLabelText.setBounds(400, 0, 400 ,150);
        gameLabelText.setVisible(true);
        this.add(gameLabelText);
    }

    public void setLoginArea()
    {
        accountLabel = new JLabel("account:");
        accountLabel.setBounds(380,230,100,30);
        accountLabel.setFont(new Font(Font.DIALOG_INPUT , Font.BOLD,20));
        add(accountLabel);
        accountInput = new JTextField();
        accountInput.setBounds(533,240,200,20);
        add(accountInput);
        password = new JLabel("password:");
        password.setBounds(380, 300 ,150 ,30);
        password.setFont(new Font(Font.DIALOG_INPUT , Font.BOLD,20));
        add(password);
        passwordInput = new JPasswordField();
        passwordInput.setBounds(533 , 310, 200, 20);
        add(passwordInput);

        confirmButton = new JButton("log in");
        confirmButton.setBounds(430, 400, 100 ,30);
        add(confirmButton);

        registerButton = new JButton("register");
        registerButton.setBounds(570 , 400 , 100 ,30);
        add(registerButton);

        confirmButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                String acc = accountInput.getText();
                String pas = passwordInput.getText();
                accountLabel.setVisible(false);
                accountInput.setVisible(false);
                password.setVisible(false);
                passwordInput.setVisible(false);
                confirmButton.setVisible(false);
                registerButton.setVisible(false);
                ui.getGameClientLogic().connect(acc , pas);
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