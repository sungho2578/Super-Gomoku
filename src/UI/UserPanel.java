package UI;


import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserPanel extends JList<String>
{
    private JButton invite;
    private GamePanel gp;

    public UserPanel(GamePanel gp)
    {
        this.gp = gp;
        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout());
        setModel(new DefaultListModel<String>());
        invite = new JButton("invite");
        add(invite , BorderLayout.SOUTH);
        setBounds(615 , 200 , 300 , 200);
        setInviteButton();
        gp.add(this);
    }

    public DefaultListModel getListModel()
    {
        return (DefaultListModel)this.getModel();
    }

    public void setInviteButton()
    {
        invite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int currentIndex = getSelectedIndex();
                String userToInvite = getModel().getElementAt(currentIndex);

                if(userToInvite.equals(gp.getUserInterface().getGameClientLogic().getMyname()))
                {
                    JDialog invalidInvite = new JDialog();
                    invalidInvite.setTitle("error");
                    invalidInvite.setModal(true);
                    invalidInvite.setLayout(new BorderLayout());
                    invalidInvite.setBounds(200,200,100,100);
                    JLabel invalidNotice = new JLabel();
                    invalidNotice.setText("can not invite yourself");
                    invalidInvite.add(invalidNotice , BorderLayout.CENTER);
                    JButton confirmInvalidate = new JButton("OK");
                    invalidInvite.add(confirmInvalidate , BorderLayout.SOUTH);
                    confirmInvalidate.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            invalidInvite.setVisible(false);
                        }
                    });

                    invalidInvite.setVisible(true);

                }

                else
                {
                    //gp.getGameClientLogic().invite(userToInvite);
                }

            }
        });
    }



}
