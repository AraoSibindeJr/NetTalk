package src;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Servidor implements ActionListener {

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton startButton;
    private JTextField portField;
    private JLabel statusLabel;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean serverRunning = false;
    private boolean clientConnected = false;

    public Servidor() {
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Servidor de Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        // Painel superior - configurações
        JPanel topPanel = new JPanel(new FlowLayout());
        portField = new JTextField("9001", 10);
        startButton = new JButton("Iniciar Servidor");
        startButton.addActionListener(this);
        statusLabel = new JLabel("Servidor não iniciado");

        topPanel.add(new JLabel("Porta:"));
        topPanel.add(portField);
        topPanel.add(startButton);
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

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            serverRunning = true;
            updateStatus("Servidor ouvindo na porta " + port + " - Aguardando cliente...");

            new Thread(() -> {
                try {
                    // Aceita conexão do cliente
                    clientSocket = serverSocket.accept();
                    clientConnected = true;

                    // Inicializa streams
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    // Lê o username do cliente
                    String username = in.readLine();
                    appendToChat("Cliente conectado: " + username);
                    updateStatus("Cliente " + username + " conectado");

                    // Habilita envio de mensagens
                    SwingUtilities.invokeLater(() -> {
                        messageField.setEnabled(true);
                        sendButton.setEnabled(true);
                    });

                    // Thread para receber mensagens do cliente
                    new Thread(() -> {
                        try {
                            String msg;
                            while ((msg = in.readLine()) != null && clientConnected) {
                                String finalMsg = msg;
                                appendToChat("[Cliente]: " + finalMsg);
                            }
                        } catch (IOException e) {
                            appendToChat("Cliente desconectado");
                        } finally {
                            clientConnected = false;
                            SwingUtilities.invokeLater(() -> {
                                messageField.setEnabled(false);
                                sendButton.setEnabled(false);
                            });
                            updateStatus("Aguardando cliente...");
                        }
                    }).start();

                } catch (IOException e) {
                    appendToChat("Erro ao aceitar conexão: " + e.getMessage());
                }
            }).start();

        } catch (IOException e) {
            appendToChat("Erro ao iniciar servidor: " + e.getMessage());
        }
    }

    private void stopServer() {
        serverRunning = false;
        clientConnected = false;
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        if (out != null && clientConnected) {
            String msg = messageField.getText().trim();
            if (!msg.isEmpty()) {
                out.println(msg);
                appendToChat("[Servidor]: " + msg);
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
        if (e.getSource() == startButton) {
            if (!serverRunning) {
                try {
                    int port = Integer.parseInt(portField.getText());
                    startServer(port);
                    startButton.setText("Parar Servidor");
                } catch (NumberFormatException ex) {
                    appendToChat("Porta inválida!");
                }
            } else {
                stopServer();
                startButton.setText("Iniciar Servidor");
                updateStatus("Servidor parado");
            }
        } else if (e.getSource() == sendButton || e.getSource() == messageField) {
            sendMessage();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Servidor());
    }
}