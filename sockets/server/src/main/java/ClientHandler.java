import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    public ClientHandler(Socket client) throws IOException {
        this.client=client;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(),true);
    }
    @Override
    public void run() {
    try {
        String message = in.readLine();
        out.println("Server Received!!!");
        System.out.println("RECEIVED"+" "+message);
        out.close();
        in.close();
        client.close();
    } catch (IOException e){
        System.out.println("error!!!"+" "+e.getMessage());
    }
    }
}
