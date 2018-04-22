/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author jorge/walber
 */
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mindrot.jbcrypt.BCrypt;
public class LoginDAO {
    private static final String JSON_END = "json\\login.json";
    public static String gerarHash(String senhaFraca){
        String senhaForte = BCrypt.hashpw(senhaFraca,BCrypt.gensalt(12));
        return senhaForte;
    }
     public static boolean checarHash(String senhaDigitada, String senhaBanco){
        boolean match = BCrypt.checkpw(senhaDigitada , senhaBanco);
        return match;
    }
    
    public static boolean verificarCredenciais(String pUsuario, String pSenha) {

        JSONParser parser = new JSONParser();
        String usuario = "";
        String senha = "";

        try {
            Object obj = parser.parse(new FileReader(JSON_END));
            JSONArray loginJSON = (JSONArray) obj;
            for (int i = 0; i < loginJSON.size(); i++) {
                JSONObject j = (JSONObject) loginJSON.get(i);
                usuario = j.get("login").toString();
                senha = j.get("senha").toString();
                if ( usuario.equals(pUsuario) && checarHash(pSenha,senha) ) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
        } catch (IOException e) {
            System.out.println(e.getCause());
        } catch (ParseException e) {
            System.out.println(e.getCause());
        }

        return false;

    }

}
