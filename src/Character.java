import java.util.Random; // for picking random enemy

public abstract class Character // abstract base class for all characters, subclasses will extend this
{
    String name;
    int maxHealth;
    int health;
    int attackPower;
    int defense;
    int level = 1;
    int xp = 0;
    int xpToNextLevel = 100; // every 100 XP levels up

    public Character(String name, int maxHealth, int attackPower, int defense) 
    {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;   // start at full HP
        this.attackPower = attackPower;
        this.defense = defense;
    }

    int getLevel() { return level; }
    int getXp() { return xp; }

        public void gainXp (int amount) 
        {
            xp += amount;
            System.out.println(name + " gains " + amount + " XP!");
            while (xp >= xpToNextLevel) 
            {
                levelUp();
                xp -= xpToNextLevel;
            }
        }

        public void levelUp()
        {
            level++;
            maxHealth += maxHealth/10; // increase max health by 10%
            attackPower += attackPower/10; // increase attack power by 10%
            defense += defense/10; // increase defense by 10%
            health = maxHealth; // heal on level up

            System.out.println(name + " leveled up! Now level " + level);
        }

    // Basic getters
    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getAttackPower() { return attackPower; }
    public int getDefense() { return defense; }

    // State queries
    public boolean isAlive() { return health > 0; }

    // Damage handling: reduce health by (dmg - defense), but never negative
    public void takeDamage(int dmg) {
        int net = dmg - defense;
        if (net < 0) net = 0;
        health -= net;
        if (health < 0) health = 0;
    }

    // Heal up to maxHealth
    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) health = maxHealth;
    }

    // Normal attack: can be used as-is or overridden
    public void attack(Character opponent) {
        opponent.takeDamage(this.attackPower);
    }

    // Each concrete subclass must implement its own special attack
    public abstract void specialAttack(Character opponent);

    // Print a compact status line
    public void displayStats() {
        System.out.printf("%s — HP: %d/%d  ATK: %d  DEF: %d%n",
            name, health, maxHealth, attackPower, defense);
    }
}

// first subclass of Character
class Warrior extends Character 
{
    public Warrior(String name) 
    {
        // super() calls the parent (Character) constructor
        super(name, 120, 20, 10);
    }

    @Override // to signify we are overriding the abstract method
    public void specialAttack(Character opponent) 
    {
        System.out.println(name + " unleashes Power Strike!");
        opponent.takeDamage(attackPower * 2);  // double damage special attack
    }
}

// second subclass of Character
class Mage extends Character 
{
    public Mage(String name) 
    {
        super(name, 90, 35, 15);
    }

    @Override
    public void specialAttack(Character opponent) 
    {
        System.out.println(name + " casts Fireball!");
        opponent.takeDamage(attackPower + 20);  // extra damage special attack
    }
}

// third subclass of Character
class Archer extends Character 
{
    public Archer(String name) 
    {
        super(name, 100, 20, 10);
    }

    @Override
    public void specialAttack(Character opponent) 
    {
        System.out.println(name + " uses Triple Shot!");
        opponent.takeDamage(attackPower * 3);  // additional fixed damage
    }
}

// Enemy class

class Enemy 
{
    private static Random rnd = new Random();

    public static Character createEnemy(int level) 
    {
        // Randomly pick type
        int type = rnd.nextInt(3); // 0=Warrior, 1=Mage, 2=Archer

        String name = switch(type)
        {
            case 0 -> "Enemy Warrior Lvl " + level;
            case 1 -> "Enemy Mage Lvl " + level;
            case 2 -> "Enemy Archer Lvl " + level;
            default -> "Unknown Enemy";
        };

        Character enemy;
        if (type == 0) enemy = new Warrior(name);
        else if (type == 1) enemy = new Mage(name);
        else enemy = new Archer(name);

        // Scale stats by level
        enemy.maxHealth += level * 10;
        enemy.health = enemy.maxHealth;
        enemy.attackPower += level * 2;
        enemy.defense += level;

        return enemy;
    }
}
