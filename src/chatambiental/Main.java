/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatambiental;

import dao.LoginDAO;
import org.json.simple.parser.ParseException;

/**
 *
 * @author jorge
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {
        // TODO code application logic here
        boolean ret = LoginDAO.verificarCredenciais("chataps", "chataps");
        System.out.println(ret);
    }
    
}
