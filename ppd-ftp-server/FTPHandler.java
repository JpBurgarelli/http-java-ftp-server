import java.io.*;
import java.net.*;

public class FTPHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private boolean authenticated;
    private static final String PASSWORD = "pass"; 

    public FTPHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            out.println("220 Bem-vindo ao Servidor FTP Simples");

            String command;
            while ((command = in.readLine()) != null) {
                processCommand(command);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processCommand(String command) throws IOException {
        String[] parts = command.split(" ");
        String cmd = parts[0].toUpperCase();

        switch (cmd) {
            case "USER":
                handleUserCommand(parts);
                break;
            case "PASS":
                handlePassCommand(parts);
                break;
            case "LIST":
                handleListCommand();
                break;
            case "QUIT":
                handleQuitCommand();
                break;
            default:
                out.println("500 Syntax error, command unrecognized");
                break;
        }
    }

    private void handleUserCommand(String[] parts) {
        if (parts.length == 2 && parts[1].equalsIgnoreCase("user")) {
            username = parts[1];
            out.println("331 Password required");
        } else {
            out.println("530 Invalid username");
        }
    }

    private void handlePassCommand(String[] parts) {
        if (authenticated) {
            out.println("230 User already logged in");
            return;
        }

        if (parts.length == 2 && parts[1].equals(PASSWORD)) {
            authenticated = true;
            out.println("230 User logged in");
        } else {
            out.println("530 Login incorrect");
        }
    }

    private void handleListCommand() throws IOException {
        if (!authenticated) {
            out.println("530 Not logged in");
            return;
        }

        try (ServerSocket dataSocket = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
            int dataPort = dataSocket.getLocalPort();
            String passiveModeResponse = "227 Entering Passive Mode (127,0,0,1," + (dataPort / 256) + "," + (dataPort % 256) + ")";
            out.println(passiveModeResponse);

            try (Socket dataConnection = dataSocket.accept();
                 PrintWriter dataOut = new PrintWriter(dataConnection.getOutputStream(), true)) {

                dataOut.println("le1.txt\r\nle2.txt\r\nle3.txt\r\n");
                out.println("226 Transfer complete");
            }
        }
    }

    private void handleQuitCommand() {
        out.println("221 Goodbye");
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
