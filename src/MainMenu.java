import java.util.Scanner;

public class MainMenu 
{
    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== RPG Battle Simulator ===");
        System.out.print("Enter your character name: ");
        String name = sc.nextLine();

        // New: Allow class selection
        System.out.println("Choose your class:");
        System.out.println("1. Warrior");
        System.out.println("2. Mage");
        System.out.println("3. Archer");
        System.out.print("Enter choice: ");
        int clsChoice = sc.nextInt();

        Character player;
        switch(clsChoice) 
        {
            case 1 -> player = new Warrior(name);
            case 2 -> player = new Mage(name);
            case 3 -> player = new Archer(name);
            default -> 
            {
                System.out.println("Invalid choice, defaulting to Warrior.");
                player = new Warrior(name);
            }
        }

        boolean running = true;
        while (running) 
        {
            System.out.println("\n1. Battle  2. Show Stats  3. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();

            switch(choice) 
            {
                case 1:
                    BattleSimulator battle = new BattleSimulator(player);
                    battle.startBattle(1); // enemy level can scale
                    break;
                case 2:
                    player.displayStats();
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }

        sc.close();
        System.out.println("Thanks for playing!");
    }
}
