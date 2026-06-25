package app;

import model.Cell;
import model.GameBoard;
import model.Position;
import ui.BoardPanel;
import ui.GameFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame("连连看", 815, 1050);
            frame.repaint();
        });
    }
}
