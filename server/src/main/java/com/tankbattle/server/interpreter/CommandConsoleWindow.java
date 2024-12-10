package com.tankbattle.server.interpreter;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.tankbattle.server.controllers.GameController;

public class CommandConsoleWindow extends JFrame {
    private final JTextArea outputArea;
    private final JTextField inputField;
    private final CommandInterpreter interpreter;
    private final GameController gameController;
    private final ArrayList<String> commandLog;
    private int currentCommandLogIndex = 0;

    public CommandConsoleWindow(GameController gameController) {
        super("Tank Battle Command Console");
        this.gameController = gameController;
        this.interpreter = new CommandInterpreter();
        this.commandLog = new ArrayList<>();

        // window set up
        setSize(600, 400);
        setLayout(new BorderLayout());
        setResizable(true);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel promptLabel = new JLabel(" > ");
        inputField = new JTextField();

        // submit key listener
        inputField.addActionListener(e -> processCommand());

        // clearing the console shortuct
        inputField.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "clear");

        inputField.getActionMap().put("clear", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearConsole();
            }
        });
        // scroll through command log
        inputField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");

        inputField.getActionMap().put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentCommandLogIndex > commandLog.size() - 1) {
                    reset();
                }

                currentCommandLogIndex++;
                inputField.setText(commandLog.get(commandLog.size() - currentCommandLogIndex));
            }
        });

        inputField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");

        inputField.getActionMap().put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentCommandLogIndex - 1 <= 0) {
                    currentCommandLogIndex = commandLog.size() + 1;
                }

                currentCommandLogIndex--;
                inputField.setText(commandLog.get(commandLog.size() - currentCommandLogIndex));
            }
        });
        

        inputPanel.add(promptLabel, BorderLayout.WEST);
        inputPanel.add(inputField, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        setVisible(true);
        printInitialMessage();
        inputField.grabFocus();
    }

    private void processCommand() {
        String command = inputField.getText().trim();
        commandLog.add(command);
        reset();
        if (!command.isEmpty()) {
            printToConsole("> " + command);

            if (command.equals("clear")) {
                clearConsole();
            } else {
                try {
                    interpreter.interpret(command, gameController);

                } catch (Exception e) {
                    printToConsole("Error: " + e.getMessage());
                }
            }
        }

        inputField.setText("");

        System.out.println("command log size: " + commandLog.size());

        if (commandLog.size() > 20) {
            commandLog.remove(commandLog.size() - 1);
        }
    }

    public void printInitialMessage() {
        printToConsole("Tank Battle Command Console");
        printToConsole("Type 'help' for available commands");
        printToConsole("-----------------------------------------");
    }

    public void printToConsole(String message) {
        outputArea.append(message + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    private void clearConsole() {
        reset();
        outputArea.setText("");
        printInitialMessage();
    }

    public void showHelp() {
        printToConsole("Available commands:");
        List<String> allCommands = interpreter.getCommandsList();
        for (String command : allCommands) {
            printToConsole("  " + command);
        }
    }

    private void reset() {
        currentCommandLogIndex = 0;
    }
}
