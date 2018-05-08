/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatambiental;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private ClienteInput novoCli = null;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private int porta;
    public static ArrayList<ClienteInput> users;
    public static boolean rodando = true;
    

    public ServidorThread(int porta) {
        this.porta = porta;
        users = new ArrayList<ClienteInput>();
    }

    public static void msgParaTodos(String mensagem) {
        for (ClienteInput cli : users) {
            cli.out.println(mensagem);
        }
    }

    public static void msgParaTodos(String mensagem, ClienteInput sender) {
        for (ClienteInput cli : users) {
            if (cli != sender) {
                try {
                    Mensagem msg = new Mensagem(mensagem);
                    cli.oos.writeObject(msg);
                    cli.oos.flush();
                    cli.oos.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static boolean estaLogado(String pUsuario) {
        boolean ret = false;
        for (ClienteInput cli : users) {
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
                ois = new ObjectInputStream(sck.getInputStream());
                oos = new ObjectOutputStream(sck.getOutputStream());
                
                novoCli = new ClienteInput(sck, in, out, ois, oos);
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
