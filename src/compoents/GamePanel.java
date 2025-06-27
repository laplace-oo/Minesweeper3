package compoents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel {

    boolean touched;

    int length, count, revealedCount, time;

    public static Cell[][] cells;

    public static JPanel mainPanel = new JPanel();
    public static JPanel statusBar = new JPanel();
    public static JLabel remainingMines = new JLabel();
    public static JLabel timerLabel = new JLabel();
    public static Timer timer;

    JLabel[] labels = new JLabel[2];

    int[] dx = {-1, 0, 1};
    int[] dy = {-1, 0, 1};

    public GamePanel(int length, int count) {

        time = 0;

        this.length = length;
        this.count = count;
        revealedCount = 0;
        touched = false;

        if(timer != null) {
            if(timer.isRunning()) {
                timer.stop();
            }
        }
        timer = new Timer(1000, _ -> {
            time++;
            timerLabel.setText(time + "seconds");
            System.out.println(time + "seconds");
        });
        timer.start();

        removeAll();

        mainPanel = new JPanel();
        statusBar = new JPanel();
        remainingMines = new JLabel();
        timerLabel = new JLabel();
        cells = new Cell[length][length];
        setLayout(new BorderLayout());

        setLabels();

        mainPanel.setLayout(new GridLayout(length, length));

        setStatusBar();

        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {

                cells[i][j] = new Cell(i, j);
                cells[i][j].addMouseListener(new CellMouseListener());

                mainPanel.add(cells[i][j]);
            }
        }

        add(mainPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        updateUI();
    }

    void setStatusBar() {

        statusBar.setLayout(new GridLayout(1, 2));
        remainingMines.setText("remaining flags: " + (count - revealedCount));

        statusBar.add(remainingMines);
        statusBar.add(timerLabel);

        repaint();
    }

    void setLabels() {
        labels[0] = new JLabel("You lose!", JLabel.CENTER);
        labels[1] = new JLabel("You win!", JLabel.CENTER);
    }

    Pair<Integer> unZip(int t) {
        return new Pair<>((t / length), t % length);
    }

    public int zip(Pair<Integer> pair) {
        return pair.getFirst() * length + pair.getSecond() - 1;
    }

    public void setMatrix(int x, int y) {
        Random rand = new Random();
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(zip(new Pair<>(x, y)));

        int temp;
        Pair<Integer> pair;
        while(arrayList.size() < count + 1) {

            temp = rand.nextInt(length * length);

            if (!arrayList.contains(temp)) {
                arrayList.add(temp);
            }
        }

        arrayList.removeFirst();

        System.out.println(arrayList.size());

        while(!arrayList.isEmpty()) {
            temp = arrayList.getFirst();
            System.out.println(temp+ "???");
            pair = unZip(temp);
            cells[pair.getFirst()][pair.getSecond()].mined = true;
            arrayList.removeFirst();
        }

        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                System.out.print(cells[i][j].mined + " ");
            }
            System.out.println();
        }

        int tx, ty;

        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {

                if (cells[i][j].mined) {
                    continue;
                }

                for (int k = 0; k < 3; ++k) {
                    for (int l = 0; l < 3; ++l) {

                        tx = i + dx[k];
                        ty = j + dy[l];
                        if (tx < 0 || tx >= length || ty < 0 || ty >= length) {
                            continue;
                        }

                        if (cells[tx][ty].mined) {
                            System.out.println(tx + " " + ty);
                            cells[i][j].surrounding++;
                        }

                    }
                }



            }
        }
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                System.out.print(cells[i][j].surrounding + " ");
            }
            System.out.println();
        }
    }

    void spread(int x, int y) {
        System.out.println(x + "??" + y + "??" + cells[x][y].surrounding);
        cells[x][y].setVisited();
        if (cells[x][y].surrounding != 0) {
            return;
        }
        int tx, ty;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {

                tx = x + dx[i];
                ty = y + dy[j];

                if (tx < 0 || tx >= length || ty < 0 || ty >= length || cells[tx][ty].visited) {
                    continue;
                }

                spread(tx, ty);
            }
        }
        updateUI();
    }

    boolean check() {
        int tempCount = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (!cells[i][j].mined && cells[i][j].visited) {
                    tempCount++;
                }
            }
        }
        return tempCount == length * length - count;
    }

    void endGame(int code) {

        labels[code].setText(labels[code].getText() + "!!! Time spent : " + time + "seconds");
        removeAll();
        setLayout(new BorderLayout());
        add(labels[code], BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private class CellMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            Cell source = (Cell) e.getSource();
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (!touched) {
                    setMatrix(source.row, source.col);
                    touched = true;
                }
                if (cells[source.row][source.col].mined) {
                    endGame(0);
                } else {
                    spread(source.row, source.col);
                    System.out.println("???fuck");
                }
            }

            if (e.getButton() == MouseEvent.BUTTON3) {
                if (!touched) {
                    JOptionPane.showMessageDialog(null, "Please left-click first", "消息提示", JOptionPane.INFORMATION_MESSAGE);
                } else if (!cells[source.row][source.col].visited) {
                    if (revealedCount == count) {
                        JOptionPane.showMessageDialog(null, "There are not enough flags", "消息提示", JOptionPane.INFORMATION_MESSAGE);
                    } else if (cells[source.row][source.col].revealed) {
                        if (cells[source.row][source.col].mined) {
                            revealedCount--;
                        }
                        cells[source.row][source.col].changeColor(2);
                    } else {
                        if (cells[source.row][source.col].mined) {
                            revealedCount++;
                        }
                        cells[source.row][source.col].changeColor(3);
                    }
                    cells[source.row][source.col].revealed = !cells[source.row][source.col].revealed;
                    remainingMines.setText("remaining flags: " + (count - revealedCount));
                    repaint();
                }
            }
            if (check()) {
                endGame(1);
            }
            updateUI();
        }
    }

}
