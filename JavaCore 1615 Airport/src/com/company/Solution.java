package com.company;

/*

1615 Airport
1. Understand what the program does.
2. Fix the takingOff (takeoff) method - now it works sooo long. Takeoff should take 100 milliseconds.
3. Implement the waiting method by analogy with the takingOff method. Waiting time should not exceed take-off time.

Requirements:
1. The takingOff method should work for about 100ms.
2. The waiting method should work approximately 100 ms.
3. In the main method, 3 planes should be created.
4. The Solution class must have a Plane (aircraft) nested.
5. The Runway class must be nested in the Solution class.


 */

import java.util.ArrayList;
import java.util.List;


public class Solution {
    public static volatile Runway RUNWAY = new Runway();   //1 взлетная полоса

    public static void main(String[] args) throws InterruptedException {
        Plane plane1 = new Plane("Самолет #1");
        Plane plane2 = new Plane("Самолет #2");
        Plane plane3 = new Plane("Самолет #3");
    }

    private static void waiting() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }

    private static void takingOff() {
        //fix this method - исправь этот метод
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }

    public static class Plane extends Thread {
        public Plane(String name) {
            super(name);
            start();
        }

        public void run() {
            boolean isAlreadyTakenOff = false;
            while (!isAlreadyTakenOff) {
                if (RUNWAY.trySetTakingOffPlane(this)) {    //если взлетная полоса свободна, занимаем ее
                    System.out.println(getName() + " взлетает");
                    takingOff();//взлетает
                    System.out.println(getName() + " уже в небе");
                    isAlreadyTakenOff = true;
                    RUNWAY.setTakingOffPlane(null);
                } else if (!this.equals(RUNWAY.getTakingOffPlane())) {  //если взлетная полоса занята самолетом
                    System.out.println(getName() + " ожидает");
                    waiting(); //ожидает
                }
            }
        }
    }

    public static class Runway { //взлетная полоса
        private Thread t;

        public Thread getTakingOffPlane() {
            return t;
        }

        public void setTakingOffPlane(Thread t) {
            synchronized (this) {
                this.t = t;
            }
        }

        public boolean trySetTakingOffPlane(Thread t) {
            synchronized (this) {
                if (this.t == null) {
                    this.t = t;
                    return true;
                }
                return false;
            }
        }
    }
}


