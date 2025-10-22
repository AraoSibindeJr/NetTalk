package src.app;

import src.gui.ServidorUI;
import javax.swing.*;

public class MainServidor {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ServidorUI();
        });
    }
}