package src.rede;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Servidor {
    private ServerSocket serverSocket;
    private Socket cliente1, cliente2;
    private PrintWriter out1, out2;
    private SimpleDateFormat sdf;
    private static final int PORTA = 6666;

    public Servidor() throws IOException {
        this.sdf = new SimpleDateFormat("HH:mm:ss");
        iniciarServidor();
    }

    private void iniciarServidor() throws IOException {
        serverSocket = new ServerSocket(PORTA);
        System.out.println("Servidor iniciado na porta " + PORTA);

        System.out.println("Aguardando primeiro cliente...");
        cliente1 = serverSocket.accept();
        out1 = new PrintWriter(cliente1.getOutputStream(), true);
        System.out.println("Primeiro cliente conectado: " + cliente1.getInetAddress());
        out1.println("SERVIDOR: Voce e o Usuario 1. Aguardando segundo usuario...");

        System.out.println("Aguardando segundo cliente...");
        cliente2 = serverSocket.accept();
        out2 = new PrintWriter(cliente2.getOutputStream(), true);
        System.out.println("Segundo cliente conectado: " + cliente2.getInetAddress());
        out2.println("SERVIDOR: Voce e o Usuario 2. Chat iniciado!");

        out1.println("SERVIDOR: Segundo usuarios conectado! Chat iniciado.");
        broadcast("SERVIDOR: Chat entre dois usuarios iniciado!");

        new Thread(new ClienteHandler(cliente1, cliente2, "Usuario 1", "Usuario 2")).start();
        new Thread(new ClienteHandler(cliente2, cliente1, "Usuario 2", "Usuario 1")).start();
    }

    private void broadcast(String mensagem) {
        String hora = sdf.format(new Date());
        String mensagemComHora = "[" + hora + "] " + mensagem;

        if (out1 != null) out1.println(mensagemComHora);
        if (out2 != null) out2.println(mensagemComHora);
    }

    private class ClienteHandler implements Runnable {
        private Socket socketFrom, socketTo;
        private String nomeFrom, nomeTo;
        private BufferedReader in;
        private PrintWriter out;

        public ClienteHandler(Socket from, Socket to, String nomeFrom, String nomeTo) {
            this.socketFrom = from;
            this.socketTo = to;
            this.nomeFrom = nomeFrom;
            this.nomeTo = nomeTo;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socketFrom.getInputStream()));
                out = new PrintWriter(socketTo.getOutputStream(), true);

                String mensagem;
                while ((mensagem = in.readLine()) != null) {
                    String hora = sdf.format(new Date());
                    String mensagemFormatada = "[" + hora + "] " + nomeFrom + ": " + mensagem;

                    System.out.println(mensagemFormatada);
                    out.println(mensagemFormatada);
                }
            } catch (IOException e) {
                System.out.println(nomeFrom + " desconectou: " + e.getMessage());
                enviarMensagemDesconexao(nomeFrom);
            } finally {
                try {
                    if (socketFrom != null) socketFrom.close();
                } catch (IOException e) {
                    System.out.println("Erro ao fechar socket: " + e.getMessage());
                }
            }
        }

        private void enviarMensagemDesconexao(String usuario) {
            String hora = sdf.format(new Date());
            String mensagemDesconexao = "[" + hora + "] SERVIDOR: " + usuario + " saiu do chat.";
            if (out != null) out.println(mensagemDesconexao);
        }
    }

    public void pararServidor() {
        try {
            if (cliente1 != null) cliente1.close();
            if (cliente2 != null) cliente2.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.out.println("Erro ao parar servidor: " + e.getMessage());
        }
    }
}