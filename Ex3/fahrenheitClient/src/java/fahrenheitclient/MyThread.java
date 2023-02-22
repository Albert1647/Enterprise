/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fahrenheitclient;

import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import mybean.fahrenheitBeanRemote;

/**
 *
 * @author user
 */
public class MyThread extends Thread {
    fahrenheitBeanRemote bean = lookupfahrenheitBeanRemote();
    private int num;
    public MyThread(int i) {
        num = i;
    }
    @Override
    public void run() {
        Random r = new Random();
        try {
            sleep(r.nextInt(10));
        } catch (InterruptedException ex) {
            Logger.getLogger(MyThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter Degree in Fahrenheit ");
        int input = scan.nextInt();
        System.out.println(input + " Fahrenheit = "  + bean.getFahrenheit(input)+ " Celcius" );
    }

    private fahrenheitBeanRemote lookupfahrenheitBeanRemote() {
        try {
            Context c = new InitialContext();
            return (fahrenheitBeanRemote) c.lookup("java:comp/env/fahrenheitBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
