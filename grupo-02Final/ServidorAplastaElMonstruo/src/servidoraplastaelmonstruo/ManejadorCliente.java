package servidoraplastaelmonstruo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

// Clase encargada de vigilar los mensajes que envia el cliente asignado
public class ManejadorCliente extends Thread
{
    // Variables para almacenar el socket cliente, su numero y la lista de mensajes (casillero)
    int numeroCliente;
    ArrayList<String> listaMensajes;
    Socket clientSocket;
    
    // Constructor de la clase que inicializa las variables
    public ManejadorCliente(int numC, ArrayList<String> listaMsg, Socket cliente)
    {
        numeroCliente = numC;
        listaMensajes = listaMsg;
        clientSocket = cliente;
    }
    
    // Metodo que generara el hilo o proceso que estara pendiente de los mensajes del cliente especifico
    @Override
    public void run()
    {
        // Ahora, intentaremnos
        try
        {
            // Crear una variable de lectura del canal de comunicaciones del socket cliente
            BufferedReader entrada = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            // Despues creamos una variable para leer tal buffer
            String linea = "";
            
            // Miuentras existan mensajes por leer diferentes de nulo haga
            while ((linea = entrada.readLine()) != null)
            {
                // Poner en la posicion respectiva del cliente el mensaje que tiene para el servidor
                listaMensajes.set(numeroCliente, linea);
            }
            
            // Una vez se cierra terminana los mensajes o el canal se cerro entonces liberamos el buffer y el socket
            entrada.close();
            clientSocket.close();
        }
        catch (Exception e) // Si hay algun error se muestra al administrador
        {
            System.out.println ("ERROR en el manejador del cliente " + (numeroCliente + 1));
        }
    }
}
