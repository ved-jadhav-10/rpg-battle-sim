import java.util.Scanner;
import java.util.Random;

public class BattleSimulator 
{
    private Character player; // user
    private Character enemy; // system
    private Scanner sc = new Scanner(System.in);
    private StringBuilder battleLog = new StringBuilder(); // to record the battle log

    public BattleSimulator(Character player) 
    {
        this.player = player;
    }

    public void startBattle(int enemyLevel) 
    {
        enemy = Enemy.createEnemy(enemyLevel); // create random enemy of given level (based on your player level)
        System.out.println("\n=== Battle Start ===");
        System.out.println(player.getName() + " VS " + enemy.getName());

        /* Optional: multithreading enemy turn, since we already have an enemy attack file we don't need this here
        EnemyThread enemyThread = new EnemyThread(enemy, player, battleLog);
        enemyThread.start();
        */
        
        while (player.isAlive() && enemy.isAlive()) // to keep the battle running as long as both characters are alive
        {
            playerTurn();
        }

        // Stop enemy thread after battle
        enemyThread.stopThread();

        // XP rewards
        if (player.isAlive()) 
        {
            System.out.println("Victory!");
            player.gainXp(50); // winning reward XP
            log(player.getName() + " gained 50 XP for victory.");
        } else {
            System.out.println("You were defeated...");
            player.gainXp(20); // consolation XP
            log(player.getName() + " gained 20 XP for losing.");
        }

        showBattleLog();
    }

    private void playerTurn() // player choices
    {
        boolean validChoice = false;
        while (!validChoice) 
        {
            System.out.println("\nYour turn: 1.Attack  2.Special  3.Show Stats");
            System.out.print("Choose: ");
            int choice = sc.nextInt();

            switch(choice) 
            {
            case 1 -> 
            {
                player.attack(enemy);
                System.out.println(player.getName() + " attacks " + enemy.getName());
                log(player.getName() + " attacked " + enemy.getName() + " for " + player.getAttackPower() + " damage");
                validChoice = true;
            }
            case 2 -> 
            {
                player.specialAttack(enemy);
                System.out.println(player.getName() + " uses special attack on " + enemy.getName());
                log(player.getName() + " special attacked " + enemy.getName() + " for " + player.getAttackPower() + " damage");
                validChoice = true;
            }
            case 3 -> 
            {
                player.displayStats();
                enemy.displayStats();
            }
            default -> 
            {
                System.out.println("Invalid choice, turn skipped!");
            }
            }
        }
    }

    public void showBattleLog() 
    {
        System.out.println("\n--- Battle Log ---");
        System.out.println(battleLog.toString());
    }

    private void log(String action) 
    {
        battleLog.append(action).append("\n");
    }

    /* Inner class for enemy multithreading - this would be required if we chose not to have the EnemyAttack.java file, in that case we only need 3 files to run the program
    private class EnemyThread extends Thread 
    {
        private Character enemy;
        private Character player;
        private boolean running = true;
        private StringBuilder battleLog;
        private Random rnd = new Random();

        public EnemyThread(Character enemy, Character player, StringBuilder battleLog) 
        {
            this.enemy = enemy;
            this.player = player;
            this.battleLog = battleLog;
        }

        @Override
        public void run() 
        {
            while (running && player.isAlive() && enemy.isAlive()) 
            {
                try 
                {
                    Thread.sleep(5000); // enemy attacks every 5 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!player.isAlive() || !enemy.isAlive()) break;

                if (rnd.nextDouble() < 0.2) { // 20% chance special attack
                    enemy.specialAttack(player);
                    System.out.println(enemy.getName() + " uses special attack!");
                    log(enemy.getName() + " special attacked " + player.getName());
                } else {
                    enemy.attack(player);
                    System.out.println(enemy.getName() + " attacks!");
                    log(enemy.getName() + " attacked " + player.getName());
                }

                enemy.displayStats();
            }
        }

        public void stopThread() {
            running = false;
        }
        */
        private void log(String action) {
            synchronized (battleLog) { // prevent concurrent access issues
                battleLog.append(action).append("\n");
            }
        }
    }
}
