package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;

public class Hash {

    public void hash() throws NoSuchAlgorithmException {
        String seed = "yzbqklnj";

        MessageDigest md = MessageDigest.getInstance("MD5");
        
        int result1 = 0, result2 = 0;
        int i = 0;
        StringBuilder result = new StringBuilder();
        while (i < Integer.MAX_VALUE) {
            result.setLength(0);
            byte[] digest = md.digest((seed + i).getBytes());
            for (byte aByte : digest) {
                result.append(String.format("%02x", aByte));
            }
            if (result.toString().startsWith("000000")) {
                result2 = i;
                if (result1 != 0) break;
            } else if (result.toString().startsWith("00000")) {
                if (result1 == 0) result1 = i;
                if (result2 != 0) break;
            }
            i++;
        }
        
        
        System.out.printf("Part 1: %d%n", result1);
        System.out.printf("Part 2: %d%n", result2);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Hash lava = new Hash();
        lava.hash();
    }

}
