import java.util.Scanner;

public class Gamble {

    public static double gambleWin(double currentWin) {
        if (currentWin <= 0) {
            System.out.println("No winnings to gamble.");
            return currentWin;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nYou have won " + currentWin + " EUR.");
        System.out.print("Do you want to gamble to double your winnings? (yes/no): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (!choice.equals("yes")) {
            System.out.println("You chose not to gamble. Your winnings stay at " + currentWin + " EUR.");
            return currentWin;
        }

        boolean winGamble = Math.random() < 0.5;
        if (winGamble) {
            double doubled = currentWin * 2;
            System.out.println("Congratulations! You doubled your winnings to " + doubled + " EUR!");
            return doubled;
        } else {
            System.out.println("Sorry, you lost your winnings.");
            return 0;
        }
    }
}
