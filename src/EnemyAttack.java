import java.util.Random;

public class EnemyAttack extends Thread // uses multithreading concept so enemy continues attacking
{
    private Character enemy;
    private Character player;
    private boolean running = true;

    public EnemyAttack(Character enemy, Character player) 
    {
        this.enemy = enemy;
        this.player = player;
    }

    @Override
    public void run() 
    {
        Random rnd = new Random();
        while (running && player.isAlive() && enemy.isAlive()) 
        {
            try 
            {
                Thread.sleep(5000); // enemy attacks every 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!player.isAlive() || !enemy.isAlive()) break;

            if (rnd.nextDouble() < 0.2)  // 20% chance to use special attack
            {
                enemy.specialAttack(player);
                System.out.println(enemy.getName() + " uses special attack!");
            } else {
                enemy.attack(player);
                System.out.println(enemy.getName() + " attacks!");
            }

            enemy.displayStats();
        }
    }

    public void stopThread() 
    {
        running = false;
    }
}
