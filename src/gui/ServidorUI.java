package src.gui;

import src.rede.Servidor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ServidorUI extends JFrame implements ActionListener {
    private JButton btnIniciar, btnParar;
    private Servidor servidor;

    public ServidorUI() {
        inicializarComponentes();
        configurarJanela();
    }

    private void inicializarComponentes() {
        setTitle("Servidor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelBotoes = new JPanel();
        btnIniciar = new JButton("Iniciar Servidor");
        btnParar = new JButton("Parar Servidor");
        btnParar.setEnabled(false);

        btnIniciar.addActionListener(this);
        btnParar.addActionListener(this);

        panelBotoes.add(btnIniciar);
        panelBotoes.add(btnParar);

        JLabel labelInfo = new JLabel("Servidor de Chat - Aguardando conexoes", JLabel.CENTER);
        labelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(panelBotoes, BorderLayout.CENTER);
        add(labelInfo, BorderLayout.SOUTH);
    }

    private void configurarJanela() {
        setSize(300, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnIniciar) {
            iniciarServidor();
        } else if (e.getSource() == btnParar) {
            pararServidor();
        }
    }

    private void iniciarServidor() {
        try {
            servidor = new Servidor();
            btnIniciar.setEnabled(false);
            btnParar.setEnabled(true);

            JOptionPane.showMessageDialog(this,
                    "Servidor iniciado!\nAguardando 2 usuarios se conectarem...",
                    "Servidor Ativo",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao iniciar servidor: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pararServidor() {
        if (servidor != null) {
            servidor.pararServidor();
        }
        btnIniciar.setEnabled(true);
        btnParar.setEnabled(false);

        JOptionPane.showMessageDialog(this,
                "Servidor parado!",
                "Servidor Parado",
                JOptionPane.INFORMATION_MESSAGE);
    }
}