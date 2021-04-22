import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientServer implements Runnable {
    Client client;
    ServerSocket clientServer;

    public ClientServer(Client client){
        this.client = client;
        try {
            clientServer = new ServerSocket(2020);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run (){
        while (true){
            try {
                Socket client = clientServer.accept();
                if (client.isBound()){
                    receiveFile(client);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void receiveFile(Socket client) {
        try{
            int bytesRead;
            int atual = 0;

            InputStream in = client.getInputStream();
            DataInputStream ds = new DataInputStream((in));
            String fileName = ds.readUTF();

            long size = ds.readLong();
            long auxilarSize = size;
            long startTime = System.currentTimeMillis();

            //0L = zero do long
            if (size != 0L){

                OutputStream output = new FileOutputStream("./ftp_files\\" + fileName);
                byte[] buffer = new byte[1024];

                while (size > 0 && (bytesRead = ds.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1)
                {
                    output.write(buffer, 0, bytesRead);
                    size -= bytesRead;

                    System.out.println("Arquivo " + fileName + " " + (((auxilarSize-size)*100)/auxilarSize) + "% recebido ");
                }

                double estimatedTime = (System.currentTimeMillis() - startTime)/1000;

                for (int i = 0; i < 100; ++i) System.out.println();
                System.out.println("Arquivo " + fileName + " recebido!");
                System.out.println("Arquivos recebidos ficam na pasta ./ftp_received");

                if(estimatedTime!=0){
                    System.out.println("Taxa de transferencia: " + (((double)auxilarSize)/1000)/estimatedTime + "kbps");
                    System.out.println("Tamanho: " + (((double)auxilarSize)/1000) + "kb \nTempo: " + estimatedTime + " segundos");
                }else{
                    System.out.println("Taxa de transferencia: " + auxilarSize + "bps");
                    System.out.println("Tamanho: " + (auxilarSize) + "bytes \nTempo: 1 segundo");
                }


                output.close();
            }else{
                System.out.println(fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
