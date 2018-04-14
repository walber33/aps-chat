/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatambiental;

import dao.LoginDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import view.ServerView;
import static view.ServerView.log;

/**
 *
 * @author jorge
 */
public class ServidorThread extends Thread {

    private String usuario, senha;
    private Socket novoCli = null;
    private BufferedReader in = null;
    private PrintStream out = null;
    private ServerSocket ss = null;
    private int porta;
    public static ArrayList<ClienteThread> users;
    public static boolean rodando = true;

    public ServidorThread(int porta) {
        this.porta = porta;
        users = new ArrayList<ClienteThread>();
    }

    public static void msgParaTodos(String mensagem) {
        for (ClienteThread cli : users) {
            cli.out.println(mensagem);
        }
    }

    public void dcTodosUsuarios() {
        for (ClienteThread cli : users) {
            try {
                cli.out.println("Desconectado");
                cli.in.close();
                cli.out.close();
                cli.sck.close();
                users.remove(cli);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Não conseguiu encerra a sessão com " + cli.usuario);
            }
        }
    }

    public void encerrarServidor() {
        dcTodosUsuarios();
        try {
            ss.close();
            in.close();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            ss = new ServerSocket(porta);
            while (true) {
                novoCli = ss.accept();
                log("", "", 0);

                in = new BufferedReader(new InputStreamReader(novoCli.getInputStream()));
                out = new PrintStream(novoCli.getOutputStream());
                usuario = in.readLine();
                senha = in.readLine();

                if (LoginDAO.verificarCredenciais(usuario, senha)) {
                    log(usuario, "", 1);
                    out.println("Conectado ao servidor");
                    Thread threadNovoCliente = new Thread(new ClienteThread(usuario, novoCli, in, out));
                    for (ClienteThread cliente : users) {
                        if (cliente.sck != novoCli) {
                            cliente.out.println("Usuario " + usuario + " se conectou");
                        }
                    }
                    threadNovoCliente.start();
                } else {
                    log("", "", 5);
                    out.println("Login recusado. Usuário ou senha inválidos.");

                    in.close();
                    out.close();
                    novoCli.close();
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getCause());
        } finally {
            try {
                in.close();
                out.close();
                novoCli.close();
                ss.close();
            } catch (IOException ex) {
                System.out.println(ex.getCause());
            }
        }
    }

}
