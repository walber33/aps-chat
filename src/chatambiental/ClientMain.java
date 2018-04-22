/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatambiental;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author walber
 */


public class ClientMain {
    static String usuario;
    static String senha;
    static Socket sk;
    static BufferedReader in;
    static BufferedReader inputline;
    static PrintStream out;
    
    public static void main(String[] args) throws IOException{
        try{
            usuario = "chataps";
            senha = "chataps";
            sk = new Socket("localhost",1234);
            in = new BufferedReader(new InputStreamReader(sk.getInputStream()));
            inputline = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintStream(sk.getOutputStream());
            out.println(usuario);
            out.println(senha);
            
           new Thread(new resposta()).start(); 
            
            while(!sk.isClosed()){
                if(in.readLine().equals("DC")){
                    sk.close();
                    in.close();
                    out.close();
                    inputline.close();
                }
                else{
                    out.println(inputline.readLine());
                }
            }
        }catch(Exception ex){
            System.err.println(ex);
        }
        
    }
    public static class resposta implements Runnable{
        String response;
        public void run(){
            try {
                while(!sk.isClosed()){
                    if((response = inputline.readLine()) != null)
                        System.out.println(response);
                }
            } catch (IOException ex) {
                System.out.print("erro\n"+ ex);
            }
        
        }
        
    
    }
}
