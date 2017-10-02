/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 *
 * @author Sindre
 */
public class PasswordHash {

    public PasswordHash() {

    }

    public StringBuffer hashPassword(String n) throws Exception {
        String password = n;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
//    	System.out.println("Hex format : " + hexString.toString());
        return hexString;

    }

    public String generateSalt() {
        StringBuilder buf = new StringBuilder();
        SecureRandom sr = new SecureRandom();
        for (int i = 0; i < 6; i++) {// log2(52^6)=34.20... so, this is about 32bit strong.
            boolean upper = sr.nextBoolean();
            char ch = (char) (sr.nextInt(26) + 'a');
            if (upper) {
                ch = Character.toUpperCase(ch);
            }
            buf.append(ch);
        }
        return buf.toString();
    }
}
