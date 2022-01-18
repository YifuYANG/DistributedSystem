import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {
    private static ExecutorService pool = Executors.newFixedThreadPool(2);
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7788);
        while(true){
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler=new ClientHandler(socket);
            new Thread(clientHandler).start();
            //pool.execute(clientHandler);
        }
    }
}