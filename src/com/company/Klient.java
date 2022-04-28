package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Klient {

    public static final int PORT = 5000;
    public static final String IP = "127.0.0.1";

    BufferedReader bufferedReader;
    String imie;


    public static void main(String[] args) {
        Klient k = new Klient();
        k.startKlient();

    }

    public void startKlient() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj imię: ");
        imie = sc.nextLine();

        try {
            Socket socket = new Socket(IP, PORT);
            System.out.println("Podłączono do " + socket);

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Thread t = new Thread(new Odbiorca());
            t.start();

            while (true) {
                System.out.println(">>> ");
                String str = sc.nextLine();
                if (!str.equals("q")) {
                    printWriter.println(imie + " : " + str);
                    printWriter.flush();
                } else {
                    printWriter.println(imie + "rozłączył się");
                    printWriter.flush();
                    printWriter.close();
                    sc.close();
                    socket.close();
                }
            }

        } catch (Exception e) {
        }
    }

    class Odbiorca implements Runnable {
        @Override
        public void run() {
            String wiadomosc;
            try {
                while ((wiadomosc = bufferedReader.readLine()) !=null) {
                    String subString[] = wiadomosc.split(" : ");
                    if (!subString[0].equals(imie)) {
                        System.out.println(wiadomosc);
                        System.out.println(">>> ");
                    }
                }
            } catch (Exception e) {
                System.out.println("Połączenie zakończone. ");
            }


        }
    }
}
