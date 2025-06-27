package compoents;

import javax.swing.*;
import java.awt.*;

public class Cell extends JButton {

    int row, col;
    boolean mined;
    boolean revealed;
    boolean visited;

    int surrounding;

    Font font = new Font("Consolas", Font.BOLD, 50);

    public Cell(int x, int y) {

        row = x;
        col = y;


        surrounding = 0;

        revealed = false;
        mined = false;
        visited = false;


        setFocusable(false);
        setSize(5, 5);
        setBackground(Color.GRAY);
        setForeground(Color.ORANGE);
        setFont(font);

    }

    public void changeColor(int code) {
        if (code == 1) {
            setBackground(Color.WHITE);
        } else if (code == 3) {
            setBackground(Color.RED);
        } else if (code == 2) {
            setBackground(Color.GRAY);
        }
    }

    public void setVisited() {
        visited = true;
        if (surrounding != 0)
        {
            setText(Integer.toString(surrounding));
            System.out.println("S" + surrounding);
        }
        changeColor(1);
        repaint();
    }

}
