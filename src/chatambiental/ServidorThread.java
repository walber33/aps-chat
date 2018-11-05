/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatambiental;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import view.ServerView;


public class ServidorThread extends Thread {

    private String usuario, senha;
    private Socket sck = null;
    private BufferedReader in = null;
    private PrintStream out = null;
    private ServerSocket ss = null;
    private ClienteOutput novoCli = null;
    private int porta;
    public static ArrayList<ClienteOutput> users;
    public static boolean rodando = true;
    

    public ServidorThread(int porta) {
        this.porta = porta;
        users = new ArrayList<ClientOutput>();
    }

    public static void msgParaTodos(String mensagem) {
        for (ClienteOutput cli : users) {
            cli.out.println(mensagem);
        }
    }

    public static void msgParaTodos(String mensagem, ClienteOutput sender) {
        for (ClienteOutput cli : users) {
            if (cli != sender) {
                cli.out.println(mensagem);
            }
        }
    }

    public static boolean estaLogado(String pUsuario) {
        boolean ret = false;
        for (ClienteOutput cli : users) {
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

    private void encerrarCliente(Socket pCli, BufferedReader pIn, PrintStream pOut) {
        try {
            pIn.close();
            pOut.close();
            pCli.close();
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
                
                novoCli = new ClienteOutput(sck, in, out);
                new Thread(novoCli).start();
            }
        } catch (IOException ex) {
            System.out.println("Erro no servidor");
            ex.printStackTrace();
        } finally {
            encerrarCliente(sck, in, out);
            encerrarServidor();
        }
    }

}
