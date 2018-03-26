/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author jorge
 */
public class Check {

    public static boolean checarTamanhoPorta(String txtFieldPorta) {
        return txtFieldPorta.length() == 4;
    }
    
    public static boolean checarCaracteres(String txtFieldPorta) {
        String regex = "\\w";
        return txtFieldPorta.matches(regex);
    }
}
