import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class T extends Thread {
    private int id;
    private String algoritmoHash;
    private String codigoHash;
    private String sal;
    private String currentPass = "a";
    private String stringConcatenado;
    private String hashedString;

    public T(int id, String algoritmoHash, String codigoHash, String sal) {
        this.id = id;
        this.algoritmoHash = algoritmoHash;
        this.codigoHash = codigoHash;
        this.sal = sal;

    }

    @Override
    public void run() {
        System.out.println("Inicio de crackeo de contraseña por el thread " + id + " ...");
        long startTime = System.nanoTime();
        if (this.id == 1){ //Thread 1 empieza en 'zzzzzzz' y termina en 'a'
            currentPass = "zzzzzzz";
        }
        while (Main.isCracking()) {
            stringConcatenado = currentPass + sal;
            try {
                if (algoritmoHash.equals("SHA256")) {
                    hashedString = sha256(stringConcatenado);
                } else if (algoritmoHash.equals("SHA512")) {
                    hashedString = sha512(stringConcatenado);
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            if (hashedString.equals(codigoHash)) {
                System.out.println("Contraseña encontrada: " + currentPass + " por el thread " + id);
                Main.endCracking();
            } else { // No es la contraseña

                if (this.id == 0) { //Thread 0  va de 'a' hasta 'zzzzzzz'
                    // Increment current password
                    char[] charArray = currentPass.toCharArray();
                    int i = charArray.length - 1;
                    while (i >= 0 && charArray[i] == 'z') {
                        charArray[i] = 'a';
                        i--;
                    }
                    if (i < 0) { // Estamos en zzz (ultima posicion), toca un nuevo caracter
                        charArray = new char[charArray.length + 1];
                        Arrays.fill(charArray, 'a');
                    } else { // Incrementa el caracter (no ncesariamente el ultimo)
                        charArray[i]++;
                    }
                    currentPass = new String(charArray); // Actualiza la contraseña actual
                }
                
                else { //Thread 1 va de 'zzzzzzz' hasta 'a'
                    // Decrementa la contraseña
                    char[] charArray = currentPass.toCharArray();
                    int i = charArray.length - 1;
                    while (i >= 0 && charArray[i] == 'a') {
                        charArray[i] = 'z';
                        i--;
                    }
                    if (i < 0) { 
                        charArray = new char[charArray.length - 1];
                        Arrays.fill(charArray, 'z');
                    } else { 
                        charArray[i]--;
                    }
                    currentPass = new String(charArray); // Actualiza la contraseña actual
                }
            }

        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Tiempo de ejecución del thread " + id + ": " + duration / 1000000+ "ms");

    }

    // Funciones de hash para SHA256 y SHA512
    public static String sha256(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String sha512(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
