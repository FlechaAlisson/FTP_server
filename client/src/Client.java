import java.io.File;
import java.net.Socket;

public class Client {
    private Socket client;



    public void startClient(){
        ClientServer sc = new ClientServer(this);
        new File("./ftp_files").mkdir();
        new Thread(sc).start();
    }
}
