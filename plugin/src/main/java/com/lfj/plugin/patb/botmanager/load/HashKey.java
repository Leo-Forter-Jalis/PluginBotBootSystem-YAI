package com.lfj.plugin.patb.botmanager.load;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.stream.IntStream;

public class HashKey {
    public static String getHashFunc(){
        try {
            String code = codeGenerate();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(code.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte b : bytes){
                String hex = Integer.toHexString( 0xff & b);
                if(hex.length() == 1) sb.append("0");
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
    private static String codeGenerate(){
        IntStream stream = new SecureRandom().ints(999_999_999, 100_000_000, 1_000_000_000);
        return String.valueOf(stream);
    }
}
