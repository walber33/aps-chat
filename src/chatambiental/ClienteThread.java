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
import view.ServerView;
import chatambiental.ServidorThread;
import view.ServerView;

/**
 *
 * @author jorge
 */
public class ClienteThread implements Runnable {

    public String usuario, mensagem;
    public BufferedReader in = null;
    public PrintStream out = null;
    public Socket sck = null;

    public ClienteThread(String usuario, Socket sck, BufferedReader in, PrintStream out) {
        this.usuario = usuario;
        this.sck = sck;
        this.in = in;
        this.out = out;
        ServidorThread.users.add(this);
    }

    public void run() {
        try {
            while ((mensagem = this.in.readLine()) != null) {
                ServidorThread.msgParaTodos(mensagem, this);
                ServerView.log("Chat:" + this.usuario + ":" + this.mensagem + "\n");
            }
        } catch (IOException ex) {
            System.out.println("Não conseguiu ler do Socket do Cliente " + this.usuario);
        } finally {
            try {
                this.in.close();
                this.out.close();
                this.sck.close();
            } catch (IOException ex) {
                System.out.println("Não consgeuiu fechar a conexão\n");
            }

            ServidorThread.users.remove(this);
            ServerView.log("Usuario " + this.usuario + " desconectado\n");
            ServidorThread.msgParaTodos("Usuario " + usuario + " desconectou\n");
        }
    }
}
