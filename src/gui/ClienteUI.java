package src.gui;

import src.rede.Cliente;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClienteUI extends JFrame implements ActionListener {
    private JTextField campoHost, campoPorta, campoMensagem;
    private JButton btnConectar, btnDesconectar, btnEnviar;
    private JTextArea areaChat;
    private JScrollPane scrollPane;
    private Cliente cliente;
    private String nomeUsuario;

    public ClienteUI() {
        solicitarNome();
        inicializarComponentes();
        configurarJanela();
    }

    private void solicitarNome() {
        nomeUsuario = JOptionPane.showInputDialog(this,
                "Digite seu nome:", "Identificacao do Usuario",
                JOptionPane.PLAIN_MESSAGE);

        if (nomeUsuario == null || nomeUsuario.trim().isEmpty()) {
            nomeUsuario = "Usuario";
        }
    }

    private void inicializarComponentes() {
        setTitle("Chat Cliente - " + nomeUsuario);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelConexao = new JPanel(new FlowLayout());
        panelConexao.add(new JLabel("Servidor:"));
        campoHost = new JTextField("localhost", 10);
        panelConexao.add(campoHost);

        panelConexao.add(new JLabel("Porta:"));
        campoPorta = new JTextField("6666", 5);
        panelConexao.add(campoPorta);

        btnConectar = new JButton("Conectar");
        btnDesconectar = new JButton("Desconectar");
        btnDesconectar.setEnabled(false);

        btnConectar.addActionListener(this);
        btnDesconectar.addActionListener(this);

        panelConexao.add(btnConectar);
        panelConexao.add(btnDesconectar);

        areaChat = new JTextArea(15, 40);
        areaChat.setEditable(false);
        areaChat.setBackground(Color.WHITE);
        areaChat.setFont(new Font("Arial", Font.PLAIN, 12));
        scrollPane = new JScrollPane(areaChat);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chat"));

        JPanel panelEnvio = new JPanel(new BorderLayout());
        panelEnvio.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panelEnvio.add(new JLabel("Sua mensagem: "), BorderLayout.WEST);
        campoMensagem = new JTextField();
        campoMensagem.addActionListener(this);

        btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(this);
        btnEnviar.setEnabled(false);

        JPanel panelBotaoEnviar = new JPanel();
        panelBotaoEnviar.add(btnEnviar);

        panelEnvio.add(campoMensagem, BorderLayout.CENTER);
        panelEnvio.add(panelBotaoEnviar, BorderLayout.EAST);

        add(panelConexao, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelEnvio, BorderLayout.SOUTH);
    }

    private void configurarJanela() {
        pack();
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void adicionarMensagem(String mensagem) {
        SwingUtilities.invokeLater(() -> {
            areaChat.append(mensagem + "\n");
            areaChat.setCaretPosition(areaChat.getDocument().getLength());
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnConectar) {
            conectar();
        } else if (e.getSource() == btnDesconectar) {
            desconectar();
        } else if (e.getSource() == btnEnviar || e.getSource() == campoMensagem) {
            enviarMensagem();
        }
    }

    private void conectar() {
        try {
            String host = campoHost.getText();
            int porta = Integer.parseInt(campoPorta.getText());

            cliente = new Cliente();
            cliente.conectar(host, porta, this::adicionarMensagem);

            btnConectar.setEnabled(false);
            btnDesconectar.setEnabled(true);
            btnEnviar.setEnabled(true);
            campoHost.setEnabled(false);
            campoPorta.setEnabled(false);
            campoMensagem.requestFocus();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao conectar: " + ex.getMessage(),
                    "Erro de Conexao", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void desconectar() {
        if (cliente != null) {
            cliente.desconectar();
        }
        btnConectar.setEnabled(true);
        btnDesconectar.setEnabled(false);
        btnEnviar.setEnabled(false);
        campoHost.setEnabled(true);
        campoPorta.setEnabled(true);
        adicionarMensagem("=== VOCE DESCONECTOU DO CHAT ===");
    }

    private void enviarMensagem() {
        String mensagem = campoMensagem.getText().trim();
        if (!mensagem.isEmpty() && cliente != null && cliente.isConectado()) {
            cliente.enviarMensagem(mensagem);
            campoMensagem.setText("");
        }
    }
}