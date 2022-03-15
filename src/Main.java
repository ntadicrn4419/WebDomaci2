import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class Main {

    public static final int PORT = 9000;

    public static Set<String> clients = new CopyOnWriteArraySet<>();
    public static List<String> messages = new CopyOnWriteArrayList<>();

    public static List<Socket> clientSockets = new ArrayList<>();
    public static List<PrintWriter> clientOutputStreams = new ArrayList<>();

    private static List<String> censoredWords = new ArrayList<>();

    public static void main(String[] args) {

        censoredWords.add("lopta");
        censoredWords.add("papagaj");
        censoredWords.add("zmija");

        try {
            ServerSocket serverSocket = new ServerSocket(Main.PORT);
            while (true) {
                System.out.println("Server ocekuje konekciju");
                Socket socket = serverSocket.accept();
                System.out.println("Server primio konekciju");

                clientSockets.add(socket);
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                clientOutputStreams.add(out);

                Thread serverThread = new Thread(new Server(socket, censoredWords));
                serverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static synchronized void notifyClients(String userName, Socket socket) throws IOException {
        for(int i = 0; i < clientOutputStreams.size(); i++){
            if(!clientSockets.get(i).equals(socket)){ // ako to nije ovaj klijent koji se konektovao, obavesti ga
                clientOutputStreams.get(i).println("Konektovao se klijent " + userName);
            }
        }
    }
    public static synchronized void sendToClients(String message){
        for(PrintWriter out: clientOutputStreams){
            out.println(message);
        }
    }
}
