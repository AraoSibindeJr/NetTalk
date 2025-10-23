package src;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Cliente implements ActionListener {

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton connectButton;
    private JTextField hostField;
    private JTextField portField;
    private JTextField userField;
    private JLabel statusLabel;

    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private boolean connected = false;

    public Cliente() {
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Cliente de Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        // Painel superior - configurações de conexão
        JPanel topPanel = new JPanel(new FlowLayout());
        hostField = new JTextField("localhost", 8);
        portField = new JTextField("9001", 5);
        userField = new JTextField("Usuário", 8);
        connectButton = new JButton("Conectar");
        connectButton.addActionListener(this);
        statusLabel = new JLabel("Desconectado");

        topPanel.add(new JLabel("Host:"));
        topPanel.add(hostField);
        topPanel.add(new JLabel("Porta:"));
        topPanel.add(portField);
        topPanel.add(new JLabel("Usuário:"));
        topPanel.add(userField);
        topPanel.add(connectButton);
        topPanel.add(statusLabel);

        // Área do chat
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        // Painel inferior - enviar mensagens
        JPanel bottomPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        messageField.setEnabled(false);
        messageField.addActionListener(this); // Enter para enviar
        sendButton = new JButton("Enviar");
        sendButton.setEnabled(false);
        sendButton.addActionListener(this);

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public void connect() {
        String host = hostField.getText().trim();
        String portStr = portField.getText().trim();
        String username = userField.getText().trim();

        if (host.isEmpty() || portStr.isEmpty() || username.isEmpty()) {
            appendToChat("ERRO: Preencha todos os campos!");
            return;
        }

        try {
            int port = Integer.parseInt(portStr);
            appendToChat("Conectando a " + host + ":" + port + "...");

            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Envia o username primeiro
            out.println(username);
            connected = true;

            // Atualiza interface
            SwingUtilities.invokeLater(() -> {
                messageField.setEnabled(true);
                sendButton.setEnabled(true);
                connectButton.setText("Desconectar");
                hostField.setEnabled(false);
                portField.setEnabled(false);
                userField.setEnabled(false);
            });

            appendToChat("Conectado como: " + username);
            updateStatus("Conectado como " + username);

            // Thread para receber mensagens do servidor
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null && connected) {
                        String finalMsg = msg;
                        appendToChat("[Servidor]: " + finalMsg);
                    }
                } catch (Exception e) {
                    if (connected) {
                        appendToChat("Conexão perdida com o servidor");
                    }
                } finally {
                    disconnect();
                }
            }).start();

        } catch (NumberFormatException e) {
            appendToChat("ERRO: Porta inválida!");
        } catch (Exception e) {
            appendToChat("ERRO: Não foi possível conectar ao servidor: " + e.getMessage());
        }
    }

    private void disconnect() {
        connected = false;
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            messageField.setEnabled(false);
            sendButton.setEnabled(false);
            connectButton.setText("Conectar");
            hostField.setEnabled(true);
            portField.setEnabled(true);
            userField.setEnabled(true);
            updateStatus("Desconectado");
        });
    }

    private void sendMessage() {
        if (out != null && connected) {
            String msg = messageField.getText().trim();
            if (!msg.isEmpty()) {
                out.println(msg);
                appendToChat("[Eu]: " + msg);
                messageField.setText("");
            }
        }
    }

    private void appendToChat(String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    private void updateStatus(String status) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(status);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connectButton) {
            if (!connected) {
                connect();
            } else {
                disconnect();
                appendToChat("Desconectado do servidor");
            }
        } else if (e.getSource() == sendButton || e.getSource() == messageField) {
            sendMessage();
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new Cliente());
    }
}