package backupcc.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilitarios para checar a integridade de dados.
 * 
 * @author "Pedro Reis"
 * @since 29 de agosto de 2022
 * @version 1.0
 */
public final class Util {    
     
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Obtem e retorna o hash sha256 de uma string.
     * @param data
     * @return 
    */
    public static String sha256(final String data) {
           
        MessageDigest algorithm;
       
        try {
            
            algorithm = MessageDigest.getInstance("SHA-256");
            
        
            byte[] messageDigest = algorithm.digest(data.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();

            for (byte b : messageDigest) 
                hexString.append(String.format("%02X", 0xFF & b));

            return hexString.toString();

        } 
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            
            return null;
        
        }
        
    }//sha256()
   
    
}//classe Util
