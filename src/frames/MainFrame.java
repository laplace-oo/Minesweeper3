package frames;

import compoents.GamePanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    int length, count;
    boolean changed;


    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    JMenu startMenu = new JMenu("New Game");

    JMenuItem resetMenuItem = new JMenuItem("Reset Game");
    JMenuItem exitMenuItem = new JMenuItem("Exit");
    JMenuItem easyMenuItem = new JMenuItem("Easy");
    JMenuItem intermediateMenuItem = new JMenuItem("Intermediate");
    JMenuItem difficultMenuItem = new JMenuItem("Difficult");
    GamePanel gamePanel;

    public MainFrame(String text) {

        super(text);

        changed = false;

        setLayout(new BorderLayout());

        setEasyMenuItem();

        setIntermediateMenuItem();

        setDifficultMenuItem();

        setStartMenu();

        setResetMenuItem();

        setExitMenuItem();

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        setBounds(10, 10, 500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void changeDifficulty(int length, int count) {
        this.length = length;
        this.count = count;
        changed = true;

    }

    public void start() {
        if (isAncestorOf(gamePanel)) {
            remove(gamePanel);
        }
        gamePanel = new GamePanel(length, count);
        add(gamePanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void setEasyMenuItem() {
        easyMenuItem.addActionListener(_ -> {
            changeDifficulty(5,  3);
            start();
        });
        startMenu.add(easyMenuItem);
    }

    public void setIntermediateMenuItem() {
        intermediateMenuItem.addActionListener(_ -> {
            changeDifficulty(7,  10);
            start();
        });
        startMenu.add(intermediateMenuItem);
    }

    public void setDifficultMenuItem() {
        difficultMenuItem.addActionListener(_ -> {
            changeDifficulty(10,  20);
            start();
        });
        startMenu.add(difficultMenuItem);
    }

    public void setStartMenu() {
        fileMenu.add(startMenu);
    }

    public void setResetMenuItem() {
        resetMenuItem.addActionListener(_ -> {
            if (!changed) {
                JOptionPane.showMessageDialog(null, "The game has not been played before", "消息提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                start();
            }
        });
        fileMenu.add(resetMenuItem);
    }
    public void setExitMenuItem() {
        exitMenuItem.addActionListener(_ -> {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dispose();
        });
        fileMenu.add(exitMenuItem);
    }
}
