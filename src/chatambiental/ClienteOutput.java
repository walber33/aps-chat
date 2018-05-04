/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatambiental;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import static chatambiental.ServidorThread.msgParaTodos;
import dao.LoginDAO;
import view.ServerView;

/**
 *
 * @author jorge
 */
public class ClienteOutput implements Runnable {

    public String usuario, mensagem;
    private String senha;
    public BufferedReader in = null;
    public PrintStream out = null;
    public Socket sck = null;
    private boolean logado = false;

    public ClienteOutput(Socket sck, BufferedReader in, PrintStream out) {
        this.sck = sck;
        this.in = in;
        this.out = out;
    }

    private void fecharCliente() {
        try {
            this.in.close();
            this.out.close();
            this.sck.close();
        } catch (IOException ex) {
            System.out.println("Não consgeuiu fechar a conexão\n");
            ex.printStackTrace();
        }
    }

    public void run() {
        while (!logado) {
            try {
                usuario = in.readLine();
                senha = in.readLine();
                if (LoginDAO.verificarCredenciais(usuario, senha)) {
                    if (!ServidorThread.estaLogado(usuario)) {
                        ServerView.log("Usuario " + usuario + " conectado\n");
                        out.println("true");
                        out.println("Conectado ao servidor");
                        msgParaTodos("Usuario " + usuario + " se conectou", this);
                        ServidorThread.users.add(this);
                        logado = true;
                    } else {
                        ServerView.log("Tentiva de login recusado: usuário já está logado\n");
                        out.println("false");
                        out.println("Usuario já está logado");
                    }
                } else {
                    ServerView.log("Tentiva de login recusado: usuário ou senha inválidos\n");
                    out.println("false");
                    out.println("Usuario ou senha inválidos");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                fecharCliente();
                return;
            }
        }

        try {
            while ((mensagem = this.in.readLine()) != null) {
                ServidorThread.msgParaTodos(this.usuario+": "+mensagem, this);
                ServerView.log("Chat:" + this.usuario + ":" + this.mensagem + "\n");
            }
        } catch (IOException ex) {
            System.out.println("Não conseguiu ler do Socket do Cliente " + this.usuario);
        } finally {
            fecharCliente();
            ServidorThread.users.remove(this);
            ServerView.log("Usuario " + this.usuario + " desconectado\n");
            ServidorThread.msgParaTodos("Usuario " + usuario + " desconectou\n");
        }
    }
}
