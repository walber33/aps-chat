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
import static chatambiental.ServidorThread.users;
import dao.LoginDAO;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import view.ServerView;

/**
 *
 * @author jorge
 */
public class ClienteInput implements Runnable {

    public String usuario, mensagem;
    private String senha;
    public BufferedReader in = null;
    public BufferedInputStream bis = null;
    public InputStream is = null;
    public PrintStream out = null;
    public Socket sck = null;
    public ObjectInputStream ois;
    public ObjectOutputStream oos;
    public ByteArrayOutputStream bos;
    public Mensagem input;
    private boolean logado = false;

    public ClienteInput(Socket sck) {
        try{
        this.in = new BufferedReader(new InputStreamReader(sck.getInputStream()));
        this.out = new PrintStream(sck.getOutputStream());
        this.ois = new ObjectInputStream(sck.getInputStream());
        this.oos = new ObjectOutputStream(sck.getOutputStream());
        }catch(Exception ex){
            ex.printStackTrace();
        }
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

    /*
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
                ServidorThread.msgParaTodos(this.usuario + ": " + mensagem, this);
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
     */
    public void run() {
        while (!logado) {
            try {
                input = (Mensagem) ois.readObject();
                //input.toString();
                String[] credenciais = new String[2];
                credenciais = input.mensagem.split(":");
                usuario = credenciais[0];
                senha = credenciais[1];
                if (LoginDAO.verificarCredenciais(usuario, senha)) {
                    if (!ServidorThread.estaLogado(this.usuario)) {
                        ServerView.log("Usuario " + usuario + " conectado\n");
                        oos.writeObject(new Mensagem("true"));
                        oos.writeObject(new Mensagem("Conectado ao servidor"));
                        msgParaTodos("Usuario " + usuario + " se conectou",null,null,this);
                        ServidorThread.users.add(this);
                        logado = true;
                    } else {
                        ServerView.log("Tentiva de login recusado: usuário já está logado\n");
                        out.println("false");
                        out.println("Usuario já está logado");
                    }
                } else {
                    oos.writeObject(new Mensagem("false"));
                    oos.writeObject(new Mensagem("Login ou senha incorretos"));

                    
                    ServerView.log("Tentiva de login recusado: usuário ou senha inválidos\n");
                    out.println("false");
                    out.println("Usuario ou senha inválidos");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                fecharCliente();
                return;
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        try {
            while ((this.input = (Mensagem) this.ois.readObject()) != null) {
                if(this.input.arquivo == null){
                    if(this.input.mensagem.startsWith("4")){
                        String onlines = "";
                        for(ClienteInput o : users){
                             onlines += o.usuario+":";
                        }
                        ServidorThread.msgParaTodos("353535:"+onlines,null,null, this,true);
                    }else{
                    ServidorThread.msgParaTodos(this.usuario + ": " + this.input.mensagem,null,null, this);
                    ServerView.log("Chat:" + this.usuario + ":" + this.input.mensagem + "\n");
                    }
                }else{
                    ServidorThread.msgParaTodos(null, this.input.arquivo,this.input.nomeArquivo, this);
                    ServerView.log(this.usuario + " Enviou o arquivo:" + this.input.nomeArquivo + "\n");
                }
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Não conseguiu ler do Socket do Cliente " + this.usuario);
            ex.printStackTrace();
        } finally {
            fecharCliente();
            ServidorThread.users.remove(this);
            ServerView.log("Usuario " + this.usuario + " desconectado\n");
            ServidorThread.msgParaTodos("Usuario " + this.usuario + " desconectou\n",null,null,this);
        }
    }
}
