import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import java.util.Random;
import java.util.ArrayList;

class asciishooter extends JFrame implements KeyListener{
    public static boolean running = true;
    public static boolean leftKey;
    public static boolean upKey;
    public static boolean rightKey;
    public static boolean downKey;
    public static boolean spaceKey;
    public static char[][] pixels = new char[14][30];
    public static int[] player = {13, -13, 0, 5};
    public static int[] bullet = {0, 0, 0};
    public static ArrayList<Integer> enemies = new ArrayList<Integer>();
    public static Random random = new Random();
    public static int spawnTimer = 0;
    public static int enemyToSpawn = 0;
    public static int score = 0;
    
    public static char[][] rocket = {{' ', '^', ' '},
                                     {'|', '_', '|'},
                                     {'/', '_', '\\'}};

    public static char[][] rocket2 = {{'\\', '_', '/'},
                                      {'|', '_', '|'},
                                      {' ', 'v', ' '}};

    public static char[][] ufo = {{' ', '_', ' '},
                                  {'<', ' ', '>'},
                                  {' ', '-', ' '}};

    public asciishooter(){
        this.setTitle("asciishooter");
        this.setSize(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.setVisible(true);
        gameLoop();
    }

    static void gameLoop(){
        while (running == true){
            System.out.println("\033[H\033[2J");
            System.out.flush();

            for (int i = 0; i < 14; i++){
                for (int j = 0; j < 30; j++){
                    pixels[i][j] = ' ';
                }
            }
            
            //player
            if (player[3] > 0){
                if (leftKey && player[0] > 0){
                    player[0]--;
                }
                if (rightKey && player[0] + 2 < 29){
                    player[0]++;
                }
                if (downKey && player[1] > -13){
                    player[1]--;
                }
                if (upKey && player[1] + 2 < 0){
                    player[1]++;
                }

                if (spaceKey && bullet[2] == 0){
                    bullet[0] = player[0] + 1;
                    bullet[1] = player[1] + 1;
                    bullet[2] = 1;
                }

                if (player[2] > 0){
                    player[2]--;
                }
            }

            //bullet
            if (bullet[2] == 1){
                bullet[1]++;
                if (bullet[1] == 0){
                    bullet[2] = 0;
                }
            }

            //spawning enemies
            spawnTimer += random.nextInt(0, 2)+1;
            if (spawnTimer >= 50){
                if (random.nextInt(0, 2) == 0){
                    addEnemy(random.nextInt(0, 28), 0, 2, 0, 3, 0);
                }
                else if (random.nextInt(1, 2) == 1){
                    addEnemy(random.nextInt(0, 28), 1, 2, 0, 2, 1);
                }
                spawnTimer = 0;
            }

            //updating enemies
            for (int i = 0; i < enemies.size(); i += 6){
                if (enemies.get(i+5) == 0){
                    enemies.set(i+3, enemies.get(i+3) + 1);
                    if (enemies.get(i+3) < 20){
                        if (enemies.get(i+3) % 4 == 0){
                            enemies.set(i+2, enemies.get(i+2) - 1);
                        }
                    }
                    else{
                        if (enemies.get(i+3) == 30){
                            addEnemy(enemies.get(i) + 1, -1, enemies.get(i+2) + 1, 0, 1, 2);
                            addEnemy(enemies.get(i) + 1, 1, enemies.get(i+2) + 1, 0, 1, 2);
                        }
                        if (enemies.get(i+3) == 40){
                            enemies.set(i+3, 0);
                        }
                    }
                    if (enemies.get(i) + 2 >= bullet[0] && enemies.get(i) <= bullet[0] && enemies.get(i+2) + 2 >= bullet[1] && enemies.get(i+2) <= bullet[1] && bullet[2] == 1){
                        enemies.set(i+4, enemies.get(i+4) - 1);
                        bullet[2] = 0;
                    }
                    if (enemies.get(i) + 2 >= player[0] && enemies.get(i) <= player[0] + 2 && enemies.get(i+2) + 2 >= player[1] && enemies.get(i+2) <= player[1] + 2 && player[2] == 0){
                        player[3]--;
                        player[2] = 30;
                    }
                }
                else if (enemies.get(i+5) == 1){
                    enemies.set(i+3, enemies.get(i+3) + 1);
                    if (enemies.get(i+3) % 2 == 0){
                        enemies.set(i, enemies.get(i) + enemies.get(i+1));
                    }
                    if (enemies.get(i) == 0 || enemies.get(i) + 2 == 29){
                        enemies.set(i+1, enemies.get(i+1) * -1);
                        enemies.set(i, enemies.get(i) + enemies.get(i+1));
                    }
                    
                    if (enemies.get(i+3) % 4 == 0){
                        enemies.set(i+2, enemies.get(i+2) - 1);
                    }
                    if (enemies.get(i) + 2 >= bullet[0] && enemies.get(i) <= bullet[0] && enemies.get(i+2) + 2 >= bullet[1] && enemies.get(i+2) <= bullet[1] && bullet[2] == 1){
                        enemies.set(i+4, enemies.get(i+4) - 1);
                        bullet[2] = 0;
                    }
                }
                else if (enemies.get(i+5) == 2){
                    enemies.set(i, enemies.get(i) + enemies.get(i+1));
                    enemies.set(i+2, enemies.get(i+2) - 1);
                }
                if (enemies.get(i) + 2 >= player[0] && enemies.get(i) <= player[0] + 2 && enemies.get(i+2) + 2 >= player[1] && enemies.get(i+2) <= player[1] + 2 && player[2] == 0){
                    player[3]--;
                    player[2] = 30;
                }
            }

            //deleting enemies
            for (int i = enemies.size() - 6; i >= 0; i -= 6){
                if (enemies.get(i+4) == 0 || enemies.get(i+2) == -15){
                    if (enemies.get(i+5) != 2 && enemies.get(i+4) == 0){
                        score += 100;
                    }
                    enemies.remove(i+5);
                    enemies.remove(i+4);
                    enemies.remove(i+3);
                    enemies.remove(i+2);
                    enemies.remove(i+1);
                    enemies.remove(i);
                }
            }
            

            //rendering
            if (player[3] > 0){
                drawSprite(rocket, player[0], player[1], true);
            }
            
            if (bullet[2] == 1){
                setPixel(bullet[0], bullet[1], '^');
            }

            for (int i = 0; i < enemies.size(); i += 6){
                if (enemies.get(i+5) == 0){
                    drawSprite(rocket2, enemies.get(i), enemies.get(i+2), true);
                }
                else if (enemies.get(i+5) == 1){
                    drawSprite(ufo, enemies.get(i), enemies.get(i+2), true);
                }
                else{
                    setPixel(enemies.get(i), enemies.get(i+2), 'o');
                }
            }

            System.out.println("Score: " + score);
            System.out.println("Health: " + player[3]);
            for (int i = 0; i < 14; i++){
                for (int j = 0; j < 30; j++){
                    System.out.print(pixels[i][j]);
                }
                System.out.println();
            }

            try{
                Thread.sleep(50);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        new asciishooter();
    }

    static void setPixel(int x, int y, char pixel){
        if (x >= 0 && x <= 29 && y <= 0 && y >= -13){
            pixels[Math.abs(y)][x] = pixel;
        }
    }

    static void rect(int x, int y, int width, int height, char pixel){
        for (int i = x; i < x + width; i++){
            for (int j = y; j < y + height; j++){
                setPixel(i, j, pixel);
            }
        }
    }

    static void drawSprite(char[][] sprite, int x, int y, boolean vFlip){
        for (int i = y; i < y + sprite.length; i++){
            for (int j = x; j < x + sprite[0].length; j++){
                if (!vFlip){
                    if (sprite[i-y][j-x] != ' '){
                        setPixel(j, i, sprite[i-y][j-x]);
                    }
                }
                else{
                    if (sprite[Math.abs(i-(y+(sprite.length-1)))][j-x] != ' '){
                        setPixel(j, i, sprite[Math.abs(i-(y+(sprite.length-1)))][j-x]);
                    }
                }
            }
        }
    }

    static void addEnemy(int x, int xv, int y, int timer, int hp, int type){
        enemies.add(x);
        enemies.add(xv);
        enemies.add(y);
        enemies.add(timer);
        enemies.add(hp);
        enemies.add(type);
    }

    public void keyTyped(KeyEvent e){
       //no use
    }
    
    public void keyPressed(KeyEvent e){
       switch (e.getKeyCode()){
               case 37: leftKey = true;
               break;
               case 38: upKey = true;
               break;
               case 39: rightKey = true;
               break;
               case 40: downKey = true;
               break;
               case 32: spaceKey = true;
               break;
       }
    }
    
    public void keyReleased(KeyEvent e){
       switch (e.getKeyCode()){
               case 37: leftKey = false;
               break;
               case 38: upKey = false;
               break;
               case 39: rightKey = false;
               break;
               case 40: downKey = false;
               break;
               case 32: spaceKey = false;
               break;
       }
    }
}