package com.tankbattle.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.tankbattle.controllers.GameManager;

public class MenuPanel extends JPanel {
    private BufferedImage backgroundImage;

    public MenuPanel() {
        setLayout(new GridBagLayout());

        JPanel parentPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(parentPanel, BoxLayout.Y_AXIS);
        parentPanel.setLayout(boxLayout);
        parentPanel.setBackground(new Color(255, 255, 255, 180));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel hostnamePanel = new JPanel();
        hostnamePanel.setOpaque(false);
        JLabel hostnameLabel = new JLabel("Hostname: ");
        JTextField hostnameTextField = new JTextField("localhost", 10);

        hostnamePanel.add(hostnameLabel);
        hostnamePanel.add(hostnameTextField);

        JPanel usernamePanel = new JPanel();
        usernamePanel.setOpaque(false);
        JLabel usernameLabel = new JLabel("Username: ");
        JTextField usernameTextField = new JTextField(10);

        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameTextField);

        JPanel connectPanel = new JPanel();
        connectPanel.setOpaque(false);
        JButton connectButton = new JButton("Connect");

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String url = hostnameTextField.getText();
                String username = usernameTextField.getText();
                GameManager.getInstance().startGame(url, username);
            }
        });

        connectPanel.add(connectButton);

        parentPanel.add(hostnamePanel);
        parentPanel.add(usernamePanel);
        parentPanel.add(connectPanel);

        add(parentPanel, gbc);

        try {
            backgroundImage = ImageIO.read(new File("src/main/java/com/tankbattle/assets/images/background.png"));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, this);
    }
}
