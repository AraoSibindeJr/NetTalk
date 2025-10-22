package src.rede;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Servidor {
    private ServerSocket serverSocket;
    private Set<PrintWriter> clientes = Collections.synchronizedSet(new HashSet<>());
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private static final int porta = 6666;


    public Servidor() throws IOException {
        criarServerSocket();
    }

    private void criarServerSocket() throws IOException {
        serverSocket = new ServerSocket(porta);
        System.out.println("Servidor iniciado na porta " + porta);
    }


    public void start() {
        System.out.println("Aguardando conexões...");
        while (true) {
            try {
                Socket socket = esperarConexao();
                System.out.println("Cliente conectado: " + socket.getInetAddress());
                tratarConexao(socket);
            } catch (IOException e) {
                System.out.println("Erro ao aceitar conexão: " + e.getMessage());
            }
        }
    }

    private Socket esperarConexao() throws IOException {
        return serverSocket.accept();
    }

    private void tratarConexao(Socket socket) {
        Thread clienteThread = new Thread(() -> {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                clientes.add(out);

                String mensagem;
                while ((mensagem = in.readLine()) != null) {
                    String hora = sdf.format(new Date());
                    String mensagemComHora = "[" + hora + "] " + mensagem;
                    System.out.println(mensagemComHora);
                    broadcast(mensagemComHora);
                }

            } catch (IOException e) {
                System.out.println("Cliente desconectou: " + socket.getInetAddress());
            } finally {
                clientes.removeIf(out -> out.checkError());
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Erro ao fechar socket");
                }
            }
        });
        clienteThread.start();
    }

    private void broadcast(String mensagem) {
        synchronized (clientes) {
            for (PrintWriter writer : clientes) {
                writer.println(mensagem);
            }
        }
    }
}

    // Main para iniciar o servidor
    /*public static void main(String[] args) {
        try {
            Servidor server = new Servidor();
            server.start();
        } catch (IOException e) {
            System.out.println("Erro ao iniciar servidor: " + e.getMessage());
        }
    }
}*/
