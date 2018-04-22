/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatambiental;

import dao.LoginDAO;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import view.ServerView;

/**
 *
 * @author jorge
 */
public class ServidorThread extends Thread {

    private String usuario, senha;
    private Socket sck = null;
    private BufferedReader in = null;
    private PrintStream out = null;
    private ServerSocket ss = null;
    private ClienteThread novoCli = null;
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

    public static void msgParaTodos(String mensagem, ClienteThread sender) {
        for (ClienteThread cli : users) {
            if (cli != sender) {
                cli.out.println(mensagem);
            }
        }
    }

    private boolean estaLogado(String pUsuario) {
        boolean ret = false;
        for (ClienteThread cli : users) {
            if (cli.usuario.equals(pUsuario)) {
                ret = true;
            }
        }
        System.out.println(ret);
        return ret;
    }

    public void encerrarServidor() {
        msgParaTodos("DC");
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
                sck = ss.accept();
                ServerView.log("Nova conexão\n");

                in = new BufferedReader(new InputStreamReader(sck.getInputStream()));
                out = new PrintStream(sck.getOutputStream());
                usuario = in.readLine();
                senha = in.readLine();

                if (LoginDAO.verificarCredenciais(usuario, senha)) {
                    if (!estaLogado(usuario)) {
                        ServerView.log("Usuario " + usuario + " conectado\n");
                        out.println("Conectado ao servidor");
                        novoCli = new ClienteThread(usuario, sck, in, out);
                        new Thread(novoCli).start();
                        msgParaTodos("Usuario " + usuario + " se conectou", novoCli);
                    } else {
                        ServerView.log("Tentiva de login recusado: usuário já está logado\n");
                        out.println("Usuario já está logado");
                    }
                } else {
                    ServerView.log("Tentiva de login recusado: usuário ou senha inválidos\n");
                    out.println("Usuario ou senha inválidos");
                }
            }
        } catch (IOException ex) {
            System.out.println("Erro no servidor");
            ex.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                sck.close();
                ss.close();
            } catch (IOException ex) {
                System.out.println("Erro ao finalizar o servidor");
                ex.printStackTrace();
            }
        }
    }

}
