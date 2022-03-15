import java.io.*;
import java.net.Socket;

public class ClientReader implements Runnable{

    private BufferedReader in;

    public ClientReader(BufferedReader in){
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String receivedMessage = "";
            while (true) {
                receivedMessage = in.readLine();
                if(receivedMessage != "" && receivedMessage != null){
                    System.out.println(receivedMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
