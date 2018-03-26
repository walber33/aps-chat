/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatambiental;

import dao.LoginDAO;
import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
import view.ServerView;
/**
 *
 * @author jorge
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        final int PORTA = Integer.parseInt(args[0]);
        
        ServerView serverView = new ServerView();
        serverView.setVisible(true);
        
    }
    
}
