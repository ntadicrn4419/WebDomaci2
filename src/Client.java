import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client{

    public static final int PORT = 9000;//port na kome radi server;

    public static void main(String[] args) {

        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = new Socket("127.0.0.1", PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String userName = login(in, out);

        Thread writer = new Thread(new ClientWriter(out, userName));
        Thread reader = new Thread(new ClientReader(in));
        writer.start();
        reader.start();

        try {
            writer.join();
            reader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        closeCommunication(socket, in ,out);

    }
    public static void closeCommunication(Socket socket, BufferedReader in, PrintWriter out){
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            out.close();
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static String login(BufferedReader in, PrintWriter out){

        Scanner scanner = new Scanner(System.in);
        String response = "";
        String userName = "";

        while(true){
            System.out.println("Unesite korisnicko ime:");
            userName = scanner.nextLine();
            out.println(userName);
            try {
                response = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(response);
            if(response.equalsIgnoreCase("Uspesno ste se ulogovali.")){
                break;
            }
        }
        return userName;
    }
}
