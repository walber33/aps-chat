package chatambiental;


import java.io.*;
import java.net.*;

class ClientMain {

    static Socket cli;
    static PrintStream out;
    static BufferedReader in;
    static BufferedReader inputline;

    public static void main(String args[]) {
        try {
            cli = new Socket("localhost", 2222);
            out = new PrintStream(cli.getOutputStream());
            in = new BufferedReader(new InputStreamReader(cli.getInputStream()));
            inputline = new BufferedReader(new InputStreamReader(System.in));

            new Thread(new resposta()).start();

            while (true) {
                out.println(inputline.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class resposta implements Runnable {

        String response;

        public void run() {
            try {
                while ((response = in.readLine()) != null) {
                    if (response.equalsIgnoreCase("dc")) {
                        out.close();
                        in.close();
                        inputline.close();
                        cli.close();
                    } else {
                        System.out.println(response);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
