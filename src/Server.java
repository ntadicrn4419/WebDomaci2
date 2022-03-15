import java.io.*;
import java.net.Socket;
import java.util.Iterator;

public class Server implements Runnable{
    private Socket socket;

    public Server(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            String clientUserName;
            String message;

            //logovanje klijenta
            clientUserName = this.userLogin(in, out);

            //obavastavanje ostalih klijenata da se novi prikljucio
            Main.notifyClients(clientUserName, this.socket);

            //prikazivanje istorije poruka novom klijentu
            synchronized (this){
                Iterator<String> history = Main.messages.iterator();
                StringBuilder sb = new StringBuilder();
                while(history.hasNext()){
                    sb.append(history.next() + "\n");
                }
                out.println(sb.toString());
            }

            //citanje i obradjivanje poruka klijenta
            while (true){
                message = in.readLine();
                if(message != "" && message != null){
                    if(Main.messages.size() == 100){
                        Main.messages.remove(0);
                    }
                    message = checkMessage(message);
                    Main.messages.add(message);
                    Main.sendToClients(message);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.closeCommunication(in, out);
        }
    }
    public String userLogin(BufferedReader in, PrintWriter out) throws IOException {
        String clientUserName = "";
        while (true) {
            clientUserName = in.readLine();
            if(Main.clients.add(clientUserName)){//ako taj clientUserName ne postoji vec u clients, dodace ga i vratice true, u suprotnom vratice false
                out.println("Uspesno ste se ulogovali.");
                break;
            }else{
                out.println("To korisnicko ime je vec zauzeto. Molimo izaberite drugo ime.");
                continue;
            }
        }
        return clientUserName;
    }
    public void closeCommunication(BufferedReader in, PrintWriter out){
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

        if (this.socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String checkMessage(String msg){
        String[] words = msg.split(" ");
        StringBuilder checked = new StringBuilder();
        char[] ch;
        char first;
        char last;
        for(String word: words){
            if(Main.censoredWords.contains(word)){
                ch = word.toCharArray();
                for(int i = 1; i < ch.length-1; i++){
                    ch[i] = '*';
                }
                word = String.valueOf(ch);
            }
            checked.append(word + " ");
        }
        return checked.toString();
    }
}
