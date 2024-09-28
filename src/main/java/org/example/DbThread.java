package org.example;

public class DbThread implements Runnable{

    @Override
    public void run() {
        try {
            Main.printRows();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
