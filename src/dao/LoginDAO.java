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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LoginDAO {

    private static final String JSON_END = "json\\login.json";

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
                if (usuario.equals(pUsuario) && senha.equals(pSenha)) {
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
