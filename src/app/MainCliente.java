package src.app;

import src.gui.ClienteUI;
import javax.swing.*;

public class MainCliente {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ClienteUI();
        });
    }
}