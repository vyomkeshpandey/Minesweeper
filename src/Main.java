public class Main implements Runnable{
    GUI gui = new GUI();
    public static void main(String[] args) {
        //System.out.println("Hello world!");
        new Thread (new Main()).start();

    }

    @Override
    public void run() {
        while(true){
            gui.repaint();
            if (!gui.resetter){
                gui.checkWinStatus();   //continously checks - won or not.
                //fystem.out.println("Victory: "+gui.victory+" Defeat: "+gui.defeat+" life: "+gui.life);
            }
        }
    }
}