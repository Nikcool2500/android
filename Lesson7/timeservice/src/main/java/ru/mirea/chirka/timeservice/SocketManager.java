package ru.mirea.chirka.timeservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketManager {
    public static String fetchTime() throws IOException {
        Socket socket = new Socket("time.nist.gov", 13);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        socket.close();
        return response.toString();
    }
}