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
import static chatambiental.ServidorThread.users;
import static view.ServerView.log;

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
        System.out.println(this.toString());
        users.add(this);
    }

    public void run() {
        try {
            while ((mensagem = in.readLine()) != null && !sck.isClosed()) {
                for (ClienteThread cli : users) {
                    if (cli != this) {
                        cli.out.println(mensagem);
                    }
                }
                log(usuario, mensagem, 3);
            }
        } catch (IOException ex) {
            System.out.println("Não conseguiu ler do Socket\n");
        } finally {
            try {
                in.close();
                out.close();
                sck.close();
            } catch (IOException ex) {
                System.out.println("Não consgeuiu fechar a conexão\n");
            }
            
            ServidorThread.users.remove(this);
            ServerView.log(usuario, "", 2);
            ServidorThread.msgParaTodos("Usuario " + usuario + " desconectou\n");
        }
    }
}
