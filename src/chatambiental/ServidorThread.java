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
public class ServidorThread extends Thread{

    private String usuario, senha;
    private Socket sck = null;
    private BufferedReader in = null;
    private PrintStream out = null;
    private ServerSocket ss = null;
    private ClienteInput novoCli = null;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private int porta;
    public static ArrayList<ClienteInput> users;
    public static boolean rodando = true;
    public ServidorThread(int porta) {
        this.oos = null;
        this.porta = porta;
        users = new ArrayList<ClienteInput>();
    }

    
    public static void msgParaTodos(String mensagem,byte[] bf,String f, ClienteInput sender) {
        for (ClienteInput cli : users) {
            if (cli != sender) {
                try {
                    
                    if(f == null)
                        cli.oos.writeObject(new Mensagem(mensagem));
                    else
                        cli.oos.writeObject(new Mensagem(bf,sender.usuario, f, f.substring(f.lastIndexOf("."))));
                    
                    cli.oos.reset();
                    //cli.oos.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    public static void msgParaTodos(String mensagem,byte[] bf,String f, ClienteInput sender, boolean o) {
        for (ClienteInput cli : users) {
            if (cli == sender) {
                try {
                    
                    if(f == null)
                        cli.oos.writeObject(new Mensagem(mensagem));
                    
                    cli.oos.reset();
                    //cli.oos.close();
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
        msgParaTodos("DC",null,null,null);
        try {
            ss.close();
            in.close();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void encerrarCliente(Socket pCli) {
        try {
           pCli.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void run() {
        try {
            ss = new ServerSocket(porta);
            
            while (true) {
                sck = ss.accept();
                ServerView.log("Nova conexão\n");

                /*in = new BufferedReader(new InputStreamReader(sck.getInputStream()));
                out = new PrintStream(sck.getOutputStream());
                ois = new ObjectInputStream(sck.getInputStream());
                oos = new ObjectOutputStream(sck.getOutputStream());*/
                new Thread(new ClienteInput(sck)).start();
                
            }
        } catch (IOException ex) {
            System.out.println("Erro no servidor");
            ex.printStackTrace();
        } finally {
            encerrarCliente(sck);
            encerrarServidor();
        }
    }
}
