import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
public class GUI extends JFrame {
    int life = 3;
    public boolean resetter = false;
    Date startDate = new Date();
    int spacing = 4;    //optimal space is four.
    public int mX = -100;
    public int mY = -100;
    int[][] mines = new int[16][9];
    int[][] neighbours = new int[16][9];
    boolean[][] revealed = new boolean[16][9];
    boolean[][] flagged = new boolean[16][9];
    Random random = new Random();
    int neigs = 0;
    int smileX = 427;
    int smileY = 0;
    int smileyCentX = smileX +25+3;
    int smileyCentY = smileY + 25+30;
    boolean flagger = false;
    int flaggerX = 300;
    int flaggerY = 5;
    int flaggerCentX = flaggerX + 28;
    int flaggerCentY = flaggerY +50 ;
    int timeX = 785;
    int timeY = 5;
    int vicMesX = 505;
    int vicMesY = -50;
    String vicMes = "Nothing yet!";
    Date endDate;
    int sec = 0;
    public boolean happiness = true;
    public boolean victory = false;
    public boolean defeat = false;

    public GUI(){
        this.setTitle("Minesweeper by VYOM");
        this.setSize(916,605);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        //initialization arrays
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 9; j++){
                if(random.nextInt(100)<20)mines[i][j] = 1;  //mines regulator
                else mines[i][j] = 0;
                revealed[i][j] = false;
                flagged[i][j] = false;
            }
        }
        //loop to count mines in neighbourhood
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 9; j++){
                neigs = 0;
                for (int m = 0; m < 16; m++){
                    for (int n = 0; n < 9; n++){
                        if (!(m==i && n==j)) {  //won't count itself in neighbours.
                            if (isN(i, j, m, n))
                                neigs++;
                        }
                    }
                }
                neighbours[i][j] = neigs;
            }
        }

        Board board = new Board();
        this.setContentPane(board);     //here we are relating(associating) our board to GUI class.

        Move move = new Move();
        this.addMouseMotionListener(move);

        Click click = new Click();
        this.addMouseListener(click);


    }
    public class Board extends JPanel{
        //paintComponent() will be used to display whatever we want to show in the frame we see.
        public void paintComponent (Graphics g){
            g.setColor(Color.darkGray);
            g.fillRect(0, 0, 910, 610);

            //for loop to make grid
            for (int i = 0; i < 16; i++){
                for (int j = 0; j < 9; j++){
                    g.setColor(Color.GRAY);
                    if (mines[i][j] == 1)g.setColor(Color.orange);
                    if (revealed[i][j]){    //if clicked change color accordingly
                        g.setColor(Color.white);
                        if (mines[i][j]==1){
                            g.setColor(Color.red);
                        }
                    }

                    //here we have added 8 & 30(below in X & Y axis) in the end because 8, 30 pixels are occupied by the title bar of window.
                    if (mX >= spacing+i*56+8 && mX < spacing+i*56+56-spacing+8 && mY >= spacing+j*56+56+30 && mY < spacing+j*56+56+56-spacing+30){
                        g.setColor(Color.lightGray);
                    }
                    g.fillRect(spacing+i*56, spacing+j*56+56, 56-spacing, 56-spacing);
                    if (revealed[i][j]){
                        g.setColor(Color.black);
                        if (mines[i][j]==0 && neighbours[i][j]!=0){   //if it is not a mine go ahead
                            //if (mines[i][j]==0 ){//if it is not a mine go ahead
                            switch (neighbours[i][j]) {
                                case 1 -> g.setColor(Color.blue);
                                case 2 -> g.setColor(Color.green);
                                case 3 -> g.setColor(Color.red);
                                case 4 -> g.setColor(new Color(0, 0, 128));   //navy
                                case 5 -> g.setColor(new Color(178, 34, 34));    //firebrick
                                case 6 -> g.setColor(new Color(72, 209, 204)); //medium torquoise
                                case 7 -> g.setColor(Color.black);
                                case 8 -> g.setColor(Color.DARK_GRAY);
                                default -> g.setColor(Color.white);
                            }
                            g.setFont(new Font("Tahoma",Font.BOLD,38));
                            g.drawString(Integer.toString(neighbours[i][j]), i*56+18, j*56+56+45);
                        }else if (mines[i][j]==1){  //paint mine
                            g.fillRect(i*56+10+10,j*56+56+10,20,40);
                            g.fillRect(i*56+10,j*56+56+10+10,40,20);
                            g.fillRect(i*56+5+10,j*56+56+5+10,30,30);
                        }
                    }
                    //placing flags
                    if (flagged[i][j]){
                        //flagger painting - i*56, j*56+56
                        g.setColor(Color.black);
                        g.fillRect(i*56+ 27+2,j*56+56+5+4, 4,42);//pole
                        g.fillRect(i*56+16+2, j*56+56+35+4, 25, 12);//base
                        g.setColor(Color.red);
                        g.fillRect(i*56+12+2, j*56+56+10+4, 17, 12);//cloth
                        g.setColor(Color.black);
                        g.drawRect(i*56+12+2, j*56+56+10+4, 17, 12);//cloth border
                    }
                }
            }
            //creating a smiley button
            g.setColor(Color.yellow);
            g.fillOval(smileX, smileY, 50, 50);
            g.setColor(Color.black);
            g.fillOval(smileX+8,smileY+15, 10, 10);
            g.fillOval(smileX+32,smileY+15, 10, 10);
            if (happiness){
                g.fillRect(smileX+16, smileY+37, 18, 4);
                g.fillRect(smileX+12, smileY+35, 4, 4);
                g.fillRect(smileX+34, smileY+35, 4, 4);
            }else{
                g.fillRect(smileX+16, smileY+33, 18, 4);
                g.fillRect(smileX+12, smileY+36, 4, 4);
                g.fillRect(smileX+34, smileY+36, 4, 4);
            }

            //flagger painting button
            g.setColor(Color.black);
            g.fillRect(flaggerX+ 27,flaggerY+5, 4,42);//pole
            g.fillRect(flaggerX+16, flaggerY+35, 25, 12);//base
            g.setColor(Color.red);
            g.fillRect(flaggerX+12, flaggerY+10, 17, 12);//cloth
            g.setColor(Color.black);
            g.drawRect(flaggerX+12, flaggerY+10, 17, 12);//cloth border


            if (flagger){
                g.setColor(Color.red);
            }
            g.drawOval(flaggerX, flaggerY-2, 55,55);

            //painting Time Counter and box painting
            g.setColor(Color.black);
            g.fillRect(timeX, timeY, 95, 50);
            //3 lives below
            g.fillRect(6, 5, 195, 50);  //life box
            g.setColor(Color.WHITE);
            g.setFont(new Font("Tahoma", Font.PLAIN,47));
            g.drawString("Life: "+ life, 20, 48);

            if (!defeat && !victory && life!=0){   //condition will stop time from updatin in case of defeat
                sec = (int) ((new Date().getTime()-startDate.getTime())/1000);
            }
            if (sec>999)sec = 999;
            g.setColor(Color.white);
            if (victory){
                g.setColor(Color.green);
            }else if(defeat){
                g.setColor(Color.red);
            }
            g.setFont(new Font("Tohama", Font.PLAIN,50));
            if (sec<10){
                g.drawString("00"+Integer.toString(sec),timeX, timeY+43);
            }else if (sec<100){
                g.drawString("0"+Integer.toString(sec),timeX, timeY+43);
            }else {
                g.drawString(Integer.toString(sec),timeX, timeY+43);
            }

            //victory messege painting
            if (victory){
                g.setColor(Color.GREEN);
                vicMes = "YOU WIN";
            }
            else{
                g.setColor(Color.red);
                vicMes = "YOU LOSE";
            }
            if (victory || defeat){
                vicMesY = -50 + (int) (new Date().getTime()-endDate.getTime())/10;
                if (vicMesY > 48)vicMesY= 48;
                g.setFont(new Font("Tahoma", Font.PLAIN,47));
                g.drawString(vicMes,vicMesX, vicMesY);
            }
        }
    }
    public class Move implements MouseMotionListener{

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //System.out.println("The mouse was moved!");
            mX = e.getX();
            mY = e.getY();
            //System.out.println("X: "+mX+" Y: "+mY);
        }
    }
    public class Click implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            mX = e.getX();
            mY = e.getY();

            //if inside the box
            //if (inBoxX() != -1 && inBoxY() != -1)revealed[inBoxX()][inBoxY()] = true;
            if (inBoxX() != -1 && inBoxY() != -1){  //if in some box
                System.out.println("The mouse is clicked in box : ["+(1+inBoxX())+", "+(1+inBoxY())+"]");
                if (flagger && !revealed[inBoxX()][inBoxY()]){
                    if (!flagged[inBoxX()][inBoxY()]){
                        flagged[inBoxX()][inBoxY()]=true;
                    }else {
                        flagged[inBoxX()][inBoxY()]=false;
                    }//flagger mode ends
                }else{

                    System.out.println("click class"+life);
                    if (!flagged[inBoxX()][inBoxY()]){
                        revealed[inBoxX()][inBoxY()] = true;
                    }
                }
            }
            else System.out.println("The pointer is not inside any box!");

            if (inSmiley())
                resetAll();


            if (inFlagger()){
                if (!flagger){
                    flagger = true;
                    System.out.println("In flag == true!");
                }else flagger = false;
            }
            //3 lives
            if (mines[inBoxX()][inBoxY()]==1 && revealed[inBoxX()][inBoxY()])life--;
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
    public boolean inSmiley(){
        //position of smiley
        System.out.println("inSmiley() invoked");
        int diff = (int) Math.sqrt(Math.abs(mX-smileyCentX)*Math.abs(mX-smileyCentX)+ Math.abs(mY-smileyCentY)*Math.abs(mY-smileyCentY));
        if (diff < 30)return true;
        return false;
    }
    public boolean inFlagger(){
        int diff = (int) Math.sqrt(Math.abs(mX-flaggerCentX)*Math.abs(mX-flaggerCentX)+ Math.abs(mY-flaggerCentY)*Math.abs(mY-flaggerCentY));
        if (diff < 30)return true;
        return false;
    }

    public void checkWinStatus(){
        if (defeat == false) {
            int total = 0;
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 9; j++) {
                    if (revealed[i][j] && mines[i][j] == 1 && life == 0 ) {
                        //if (){
                            System.out.println("Checkwin-if life = 0");
                            happiness = false;
                            defeat = true;  //if (life<1)
                            endDate = new Date();
                        //}//else life--;
                    }
                }
            }
        }//checks if all the box have been revealed
        if (boxRevealed()>=144-(totalMines()+life-3)&& victory ==false){//3 lives
            victory = true;
            endDate = new Date();
        }
    }
    public int totalMines(){
        int total = 0;
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 9; j++){
                if (mines[i][j]==1)
                    total++;
            }
        }
        return total;
    }
    public int boxRevealed(){
        int total = 0;
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 9; j++){
                if (revealed[i][j]==true)
                    total++;
            }
        }
        return total;
    }
    public void resetAll(){
        life = 3;
        flagger = false;
        vicMesY = -50;
        resetter = true;
        startDate = new Date(); //reset timer
        happiness = true;
        victory = false;
        defeat = false;
        //initialization arrays
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 9; j++){
                if(random.nextInt(100)<20)mines[i][j] = 1;  //mines regulator
                else mines[i][j] = 0;
                revealed[i][j] = false;
                flagged[i][j] = false;
            }
        }
        //loop to count mines in neighbourhood
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 9; j++){
                neigs = 0;
                for (int m = 0; m < 16; m++){
                    for (int n = 0; n < 9; n++){
                        if (!(m==i && n==j)) {  //won't count itself in neighbours.
                            if (isN(i, j, m, n))
                                neigs++;
                        }
                    }
                }
                neighbours[i][j] = neigs;
            }
        }
        resetter = false;
    }
    public int inBoxX(){    //return row position
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 9; j++){
                if (mX >= spacing+i*56+8 && mX < spacing+i*56+56-spacing+8 && mY >= spacing+j*56+56+30 && mY < spacing+j*56+56+56-spacing+30){
                    return i;
                }
            }
        }
        return -1;
    }
    public int inBoxY(){    //return column position
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 9; j++){
                if (mX >= spacing+i*56+8 && mX < spacing+i*56+56-spacing+8 && mY >= spacing+j*56+56+30 && mY < spacing+j*56+56+56-spacing+30){
                    return j;
                }
            }
        }
        return -1;
    }
    public boolean isN(int X, int Y ,int cX, int cY){
        return X - cX < 2 && X - cX > -2 && Y - cY < 2 && Y - cY > -2 && mines[cX][cY] == 1;
    }
}




