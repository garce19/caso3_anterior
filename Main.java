import java.util.Scanner;


public class Main {
    private static boolean cracking = true;

    public static boolean isCracking() {
        return cracking;
    }

    public static void endCracking() {
        Main.cracking = false;
    }
    public static void main(String[] args) throws Exception {
        
        Scanner inputs = new Scanner(System.in);
        System.out.println("Ingrese el algoritmo de hash (H) - Unicamente 'SHA256' y 'SHA512': ");
        String algoritmoHash = inputs.nextLine();
        System.out.println("Ingrese el código criptográfico de hash de una contraseña (C) : ");
        String codigoHash = inputs.nextLine();

        System.out.println("Ingrese la sal (S) - 2 caracteres: ");
        String sal = inputs.nextLine();

        System.out.println("Ingrese numero de threads (1 o 2): ");
        int numThreads = inputs.nextInt();

        inputs.close();

        System.out.println("Algoritmo: " + algoritmoHash);
        System.out.println("Código criptográfico: " + codigoHash);
        System.out.println("Sal: " + sal);
        for (int i = 0; i < numThreads; i++) {
            T t = new T(i, algoritmoHash, codigoHash, sal);
            t.start();
        }

    }

}
