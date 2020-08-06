package lt.vikoeif.lzlatkus.opticrecieverslave;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class SocketSlave {

    private Optic optic = null;
    private String dir = "C:\\Users\\s033860\\Desktop\\temp\\";
    private StringBuilder textToFlush = new StringBuilder();

    public SocketSlave(int port) throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Slave started. ");
        } catch (IOException ex) {
            System.out.println("Slave is already started. ");
        }

        while (true) {
            Socket socket = null;
            OutputStream out = null;

            try {
                socket = serverSocket.accept();
                System.out.println("Accepted. ");
            } catch (IOException ex) {
                System.out.println("Can't accept master connection. ");
            }

            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            DataInputStream dis = new DataInputStream(bis);

            int filesCount = dis.readInt();
            File[] files = new File[filesCount];

            for (int i = 0; i < filesCount; i++) {
                long fileLength = dis.readLong();
                String fileName = dis.readUTF();

                files[i] = new File(dir + "\\" + fileName);

                FileOutputStream fos = new FileOutputStream(files[i]);
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                for(int j = 0; j < fileLength; j++) {
                    bos.write(bis.read());
                }

                bos.close();
            }

            socket.shutdownInput();

            out = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(out);

            optic = new Optic();

            for (File x : files) {
                textToFlush.append(x.getName()+"@@@");
            }

            for (File x : files) {
                textToFlush.append(optic.textExtractor(x));
            }


            dataOutputStream.writeUTF(textToFlush.toString());
            dataOutputStream.flush();

            socket.shutdownOutput();

            textToFlush.setLength(0);

            System.out.println("Deleting files");
            for(File x : files) {
                x.delete();
            }
        }

        // nereikia kolkas --> serverSocket.close();
    }

}
