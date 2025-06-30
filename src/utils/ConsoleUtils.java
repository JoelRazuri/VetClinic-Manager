package utils;

public class ConsoleUtils {

    /**
     * Limpia la pantalla de la consola
     */
    public static void clearScreen() {
        try {
            // Para Windows
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            // Para Linux/Mac
            else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            // Si falla, simular limpieza con líneas en blanco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Pausa la ejecución y espera que el usuario presione Enter
     */
    public static void pressEnterToContinue() {
        System.out.print("\nPresiona Enter para continuar...");
        try {
            System.in.read();
        } catch (Exception e) {
            // Ignorar errores
        }
    }

    /**
     * Limpia pantalla y muestra un separador visual
     */
    public static void clearAndShowHeader(String title) {
        clearScreen();
        System.out.println("=".repeat(50));
        System.out.println("  " + title.toUpperCase());
        System.out.println("=".repeat(50));
        System.out.println();
    }

    /**
     * Muestra un separador visual simple
     */
    public static void showSeparator() {
        System.out.println("\n" + "-".repeat(40) + "\n");
    }
}