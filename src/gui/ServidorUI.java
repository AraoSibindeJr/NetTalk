package src.gui;

import src.rede.Servidor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ServidorUI extends JFrame implements ActionListener {
    private JButton btn;
    private JPanel panel;

    ServidorUI(){
        inicializar();
        accao();
        add();
        setVisible(true);

    }

    void inicializar(){
        setTitle("Servidor");
        setSize(500,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel = new JPanel();
        btn = new JButton("Iniciar Servidor");
        btn.setFocusable(false);
    }

    void add(){
        add(panel);
        panel.add(btn);
    }

    public static void main(String[] args) {
        new ServidorUI();
    }

    void accao(){
        btn.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn){
            try {
                new Servidor();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}