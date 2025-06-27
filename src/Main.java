
import frames.MainFrame;
import com.formdev.flatlaf.*;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        FlatDarculaLaf.install();
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            System.err.println("Could not set look and feel: " + e);
        }

        new MainFrame("MineSweeper");
    }
}
