package com.tankbattle.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;

import com.tankbattle.controllers.GameManager;

public class MenuPanel extends JPanel {
    private BufferedImage backgroundImage;

    public MenuPanel() {
        setLayout(new GridBagLayout());

        try {
            String backgroundPath = "assets/images/background.png";
            URL backgroundResource = this.getClass().getClassLoader().getResource(backgroundPath);
            if (backgroundResource == null) {
                throw new RuntimeException("Resource not found: " + backgroundPath);
            }
            backgroundImage = ImageIO.read(backgroundResource);
        } catch (IOException e) {
            System.out.println(e);
        }

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

        // limit the username characters to 15
        ((AbstractDocument) usernameTextField.getDocument()).setDocumentFilter(new DocumentFilter() {
            private static final int MAX_CHARACTERS = 15;
            private static final String VALID_CHARACTERS_REGEX = "^[a-zA-Z0-9]*$";
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (isValidInput(fb, string)) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (isValidInput(fb, text)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
            private boolean isValidInput(FilterBypass fb, String text) {
                return (fb.getDocument().getLength() + text.length() <= MAX_CHARACTERS)
                        && text.matches(VALID_CHARACTERS_REGEX);
            }
        });

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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // default background color
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (backgroundImage != null) {
            // this block scales and keeps the image aspect ratio according to the game
            // window size
            Graphics2D g2g = (Graphics2D) g;
            g2g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int imageWidth = backgroundImage.getWidth();
            int imageHeight = backgroundImage.getHeight();
            double panelAspectRatio = (double) panelWidth / panelHeight;
            double imageAspectRatio = (double) imageWidth / imageHeight;
            int drawWidth, drawHeight;
            if (panelAspectRatio > imageAspectRatio) {
                drawHeight = panelHeight;
                drawWidth = (int) (panelHeight * imageAspectRatio);
            } else {
                drawWidth = panelWidth;
                drawHeight = (int) (panelWidth / imageAspectRatio);
            }
            int x = (panelWidth - drawWidth) / 2;
            int y = (panelHeight - drawHeight) / 2;
            g2g.drawImage(backgroundImage, x, y, drawWidth, drawHeight, this);
        }
    }
}
