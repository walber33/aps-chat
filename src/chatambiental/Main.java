/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatambiental;

import dao.LoginDAO;
import javax.swing.JOptionPane;
import view.ServerView;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
/**
 *
 * @author jorge
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final int PORTA = (args.length > 0) ? Integer.parseInt(args[0]) : 1234;
        JTextField username = new JTextField();
        JTextField password = new JPasswordField();
        Object[] message = {
        "Username:", username,
        "Password:", password
        };
        int option = JOptionPane.OK_OPTION;
        
        while(option != JOptionPane.CANCEL_OPTION){
            option = JOptionPane.showConfirmDialog(null,message,"Login",JOptionPane.OK_CANCEL_OPTION);
            String login = username.getText();
            String senha = password.getText();

            if(option == JOptionPane.OK_OPTION)
                if(LoginDAO.verificarCredenciais(login, senha)){
                    option = JOptionPane.CANCEL_OPTION;
                    ServerView serverView = new ServerView();
                    serverView.setVisible(true);
                }
                else{
                    System.out.println("Login ou senha inválidos");
                }
            else{
                option = JOptionPane.CANCEL_OPTION;
            }
        }
    }
    
}
