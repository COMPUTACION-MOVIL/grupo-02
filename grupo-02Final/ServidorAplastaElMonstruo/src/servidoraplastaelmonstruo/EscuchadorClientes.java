package servidoraplastaelmonstruo;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

// Clase que estara encargada de estar atenta de la solicitudes de conexion de los clientes moviles
public class EscuchadorClientes extends Thread
{
    // Variables que recibiran el socket servidor del hilo principal, la lista de clientes actualmente conectados y la lista de mensajes de los clientes
    ServerSocket serverSocket;
    ArrayList<Socket> listaClientes;
    ArrayList<String> listaMensajes;
    
    // Tambien creo una variable para controlar cuando detener este hilo
    boolean continuar;
    
    // Constructor de la clase que inicializa las variables de la misma
    public EscuchadorClientes(ServerSocket servidor, ArrayList<Socket> listaC, ArrayList<String> listaMsg)
    {
        serverSocket = servidor;
        listaClientes = listaC;
        listaMensajes = listaMsg;
        continuar = true;
    }
    
    // Metodo que generara el hilo o proceso que estara pendiente de las conexiones de los clientes
    @Override
    public void run()
    {
        // Ciclo infinito que solo se terminara cuando se temrine el hilo
        while(continuar)
        {
            // Variable tipo socket cliente, la cual inicializamos nula
            Socket clientSocket = null;
            
            // Ahora intentaremos que
            try
            {
                // Si algun cliente se comunico con el servidor y este es aceptado
                clientSocket = serverSocket.accept();
                
                // Entonces, se agrega este cliente a la lista de clientes activos
                listaClientes.add(clientSocket);
                
                // También se reserva un espacio para tal cliente en la lista de mensajes
                // NOTA: En si lista mensajes sera como un casillero en donde cada cliente ira depositando sus mensajes y el servidor los ira leyendo
                listaMensajes.add("");
                
                // Luego, se muestra por consola que se realizo una conexión
                System.out.println("Cliente " + listaClientes.size() + " conectado! " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                
                // Y antes de acabar se inicia o pone un marcha una clase que hara de vigilante ante cualquier mensaje del cliente recien creado
                ManejadorCliente centinelaCliente = new ManejadorCliente(listaClientes.size()-1 , listaMensajes, clientSocket);
                centinelaCliente.start();
            }
            catch(Exception e) // En caso de error
            {
                // Si el hilo todavia no ha sido detenenido por el programa principal, entonces es porque de verdad si paso algo y por lo cual
                if (continuar)
                {
                    // Se imprime un mensajito al respecto
                    System.out.println("Conexion con cliente fallo!!!");
                }
            }
        }
    }
    
    // Metodo que detendra la ejecución de este hilo
    public void detener()
    {
        continuar = false;
    }
}
