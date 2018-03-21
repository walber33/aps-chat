/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author jorge
 */
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
public class LoginDAO {
    
    private static final String JSON_END = "json\\login.json";
    
    public static boolean verificarCredenciais(String pUsuario, String pSenha) throws org.json.simple.parser.ParseException {

        JSONParser parser = new JSONParser();
        String usuario = "";
        String senha = "";
        
        try {
            Object obj = parser.parse(new FileReader(JSON_END));
            JSONObject loginJSON = (JSONObject) obj;
            
            usuario = (String) loginJSON.get("usuario");
            senha = (String) loginJSON.get("senha");
        }
        catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
        
        return usuario.equals(pUsuario) && senha.equals(pSenha);
        
    }
    
}
