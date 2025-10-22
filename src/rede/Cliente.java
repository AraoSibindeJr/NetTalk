package rede;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class Cliente {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    // conectar ao servidor
    public Cliente(String host, int porta) throws IOException {
        socket = new Socket("local host", 6666);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    // manda mensagem ao servidor
    public void enviarMensagem(String mensagem) {
        out.println(mensagem);
    }

    // Inicia thread para ouvir mensagens do servidor
    public void ouvirMensagens(Consumer<String> callback) {
        new Thread(() -> {
            String msg;
            try {
                while ((msg = in.readLine()) != null) {
                    callback.accept(msg);
                }
            } catch (IOException e) {
                callback.accept("Conexao encerrada.");
            }
        }).start();
    }

    // Fechar  conexao com o servidor
    public void desconectar() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Erro ao fechar conexao: " + e.getMessage());
        }
    }
}
