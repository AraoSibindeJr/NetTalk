package src.rede;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class Cliente {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean conectado = false;

    public void conectar(String host, int porta, Consumer<String> callback) throws IOException {
        socket = new Socket(host, porta);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        conectado = true;

        callback.accept("Conectado ao servidor " + host + ":" + porta);

        new Thread(() -> ouvirMensagens(callback)).start();
    }

    private void ouvirMensagens(Consumer<String> callback) {
        try {
            String mensagem;
            while (conectado && (mensagem = in.readLine()) != null) {
                callback.accept(mensagem);
            }
        } catch (IOException e) {
            if (conectado) {
                callback.accept("ERRO: Conexao perdida com o servidor");
            }
        } finally {
            conectado = false;
        }
    }

    public void enviarMensagem(String mensagem) {
        if (conectado && out != null && mensagem != null && !mensagem.trim().isEmpty()) {
            out.println(mensagem.trim());
        }
    }

    public void desconectar() {
        conectado = false;
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.out.println("Erro ao fechar conexao: " + e.getMessage());
        }
    }

    public boolean isConectado() {
        return conectado;
    }
}