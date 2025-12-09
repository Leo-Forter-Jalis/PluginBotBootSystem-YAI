package com.lfj.plugin.patb.keygenerate;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TempKeyManager {
    private static volatile TempKeyManager instance;
    private final SecureRandom random = new SecureRandom();
    private final Map<String, TempKey> tempKeyMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private final long expireTimeKey = 5 * 60 * 1000;
    private static class TempKey{
        String playerName;
        String encryptedKey;
        long createTime;
        TempKey(String playerName, String encryptedKey){
            this.playerName = playerName;
            this.encryptedKey = encryptedKey;
            this.createTime = System.currentTimeMillis();
        }
        boolean isExpired(){
            return System.currentTimeMillis() - createTime > TempKeyManager.getInstance().expireTimeKey;
        }
    }
    public static TempKeyManager getInstance(){
        if(instance == null){
            synchronized (TempKeyManager.class) {
                if(instance == null) return new TempKeyManager();
            }
        }
        return instance;
    }
    private TempKeyManager(){
        service.scheduleAtFixedRate(this::cleanTempKey, 1, 1, TimeUnit.MINUTES);
    }
    public void generateKey(String playerName){
        int code = 100000 + random.nextInt(900000);
        String codeS = Integer.toString(code);

    }
    private String encryptKey(String code) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = digest.digest(code.getBytes());
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }
    public boolean validate(String playerName, String key){
        if(this.tempKeyMap.containsKey(playerName)) return this.tempKeyMap.get(playerName).encryptedKey.equals(key);
        return false;
    }
    private void cleanTempKey(){
        int initialSize = this.tempKeyMap.size();
        this.tempKeyMap.entrySet().removeIf(entry -> entry.getValue().isExpired());
        int removedCount = initialSize - this.tempKeyMap.size();
        if(removedCount > 0) System.out.printf("Removed > %d keys\n", removedCount);
    }
}
