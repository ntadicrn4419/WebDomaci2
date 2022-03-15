import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ClientWriter implements Runnable{

    private PrintWriter out;
    private String userName;

    public ClientWriter(PrintWriter out, String userName){
        this.out = out;
        this.userName = userName;
    }
    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        String msg;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now;
        while(true){
            msg = sc.nextLine();
            now = LocalDateTime.now();
            out.println(dtf.format(now) + " - klijent " + userName + ": " + msg);
        }
    }
}
