package servidoraplastaelmonstruo;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

// Clase encargada de controlar las conexiones entre los diferentes dispositivos
public class ServidorAplastaElMonstruo 
{    
    public static void main(String[] args) throws SocketException, InterruptedException, IOException
    {
        // En primer lugar, se pregunta cual es el modo de juego que se quiere administrar
        String modoJuego;
        System.out.println("Escriba \"S\" si quiere empezar el modo supervivencia o \"M\" si quiere empezar el modo multijugador:");
        BufferedReader lectorModo = new BufferedReader(new InputStreamReader(System.in));
        modoJuego = lectorModo.readLine();
        
        // Si el modo elegido es S o M entonces
        if ((modoJuego.equalsIgnoreCase("S")) | (modoJuego.equalsIgnoreCase("M")))
        {
            // Se crea y pone en marcha una clase que estara atenta de la consola de comandos
            EscuchadorComandos escuchadorComandos = new EscuchadorComandos();
            escuchadorComandos.start();

            // También, creamos el puerto y variable controladora del socket del servidor
            int puerto;
            ServerSocket serverSocket;

            // Posteriormente, ponemos en marcha el socket del servidor
            try
            { 
                serverSocket = new ServerSocket(9999); 
                puerto = serverSocket.getLocalPort();
                System.out.println("Servidor... Escuchando por el puerto:" + puerto);
            }
            catch (IOException e) // En caso de error, se lo hacemos saber al administrador y terminamos el programa
            {
                System.err.println("Hay un problema por el puerto 9999");
                return;
            }

            // Ahora, creamos dos listas redimensionables
            // La primera lista es donde almacenaremos cada uno de los sockets de los clientes que se conectan al servidor
            // Y la segunda lista es para saber en todo momento que mensajes han enviado estos clientes
            ArrayList<Socket> listaClientes = new ArrayList();
            ArrayList<String> listaMensajes = new ArrayList();

            // Despues, ponemos en marcha una clase o hilo el cual estara atento de que clientes se conectan y así agregarlos a la lista sin que este programa principal tenga que parar
            EscuchadorClientes escuchadorClientes = new EscuchadorClientes(serverSocket, listaClientes, listaMensajes);
            escuchadorClientes.start();

            // Ahora, se crean algunas variables para realizar de manera coherente el seguimiento del proceso de juego
            boolean partidaEnProceso = false;
            int contaJugadores = 0;
            String auxMsg;
            
            // A continuacon, se pasara a un ciclo infinito que contiene la logica del servidor y cuya tarea es gestionar las acciones de todos en sincronia
            for (;;)
            {
                // Entonces, si estamos jugando al supervivencia haremos lo siguiente
                if (modoJuego.equalsIgnoreCase("S"))
                {
                    // Si ya se tienen dos jugadores entonces
                    if ((listaMensajes.size() > 1))
                    {
                        // Si el admin dio listo Y todavia no ha comenzado la partida entonces
                        if ((escuchadorComandos.iniciar()) & (!partidaEnProceso))
                        {
                            // Entonces enviamos un mensaje a los dos jugadores de que ya pueden comenzar
                            for (int i=0; i < 2; i++)
                            {
                                listaClientes.get(i).getOutputStream().write("Comenzar partida\n".getBytes());
                            }
                            
                            // Y marcamos la banderilla de que hay una partida en proceso
                            partidaEnProceso = true;
                        }
                        
                        // Una vez que la partida esta en proceso entonces 
                        if (partidaEnProceso)
                        {
                            // Creamos un ciclo para revisar las acciones de cada cliente
                            for (int i=0; i < 2; i++)
                            {
                                // Entonces, si el jugador i ha enviado un mensaje
                                auxMsg = listaMensajes.get(i);

                                // Y ese mensaje es diferente de nada entonces
                                if (!auxMsg.equals(""))
                                {
                                    // Primero, escribimos por consola que fue lo que dijo
                                    System.out.println("Cliente " + (i+1) + " dijo: " + auxMsg);

                                    // Luego, analizamos el mensaje por lo que:

                                    // Tokenizamos el mensaje
                                    String[] tokens = auxMsg.split(" ");

                                    // Si el mensaje posee dos elementos entonces
                                    if (tokens.length == 2)
                                    {
                                        // Si el 2do token/elemento es la palabra: "perdio!" entonces
                                        if (tokens[1].equals("perdio!"))
                                        {
                                            // Le enviamos al otro que gano
                                            Socket oponente = listaClientes.get((i+1)%2);
                                            oponente.getOutputStream().write("Ganaste!\n".getBytes());
                                        }
                                    }

                                    // Por otro lado, si el mensaje posee tres elementos entonces
                                    if (tokens.length == 3)
                                    {
                                        // Retransmitimos el enemigo al oponente
                                        Socket oponente = listaClientes.get((i+1)%2);
                                        auxMsg = auxMsg + "\n";
                                        oponente.getOutputStream().write(auxMsg.getBytes());
                                    }

                                    // Por ultimo, limpiamos el espacio (buzon) de mensajes de tal cliente
                                    listaMensajes.set(i, "");
                                }
                            }
                        }
                    }
                }
                
                // Pero si estamos jugando al multijugador se hara en cambio lo siguiente
                if (modoJuego.equalsIgnoreCase("M"))
                {
                    // Se revisa que si exista al menos un usuario conectado, sino entonces
                    if ((listaMensajes.isEmpty()))
                    {
                        // Pasamos a la siguiente iteración
                        continue;
                    }
                    
                    // Si el admin dio listo Y todavia no ha comenzado la partida entonces
                    if ((escuchadorComandos.iniciar()) & (!partidaEnProceso))
                    {
                        // Contamos cuantos son los jugadores conectados
                        contaJugadores = listaClientes.size();

                        // Luego enviamos un mensaje a los jugadores de que ya pueden comenzar
                        for (int i=0; i < contaJugadores; i++)
                        {
                            // Y se indica cuantos hay conectados
                            String aux = "jugadores " + contaJugadores + "\n";
                            listaClientes.get(i).getOutputStream().write(aux.getBytes());
                        }

                        // Y marcamos la banderilla de que hay una partida en proceso
                        partidaEnProceso = true;
                    }
                    
                    // Una vez que la partida esta en proceso entonces 
                    if (partidaEnProceso)
                    {
                        // Creamos una variable para revisar si todos los jugadores han termindao sus partidas
                        boolean terminaronTodos = true;
                        
                        // Revisamos cada una de las puntuaciones por lo que 
                        for (int i=0; i < contaJugadores; i++)
                        {
                            // Si el jugador i ha enviado un mensaje
                            auxMsg = listaMensajes.get(i);

                            // Y este es igual a nada es porque todavia falta su puntaje por lo que entonces
                            if (auxMsg.equals(""))
                            {
                                // Bajamos la baderilla de terminacion y rompemos esta ciclo
                                terminaronTodos = false;
                                break;
                            }
                        }
                        
                        // Si de verdad todos han terminado procedemos a
                        if (terminaronTodos)
                        {
                            // Obtener el primer puntaje
                            auxMsg = listaMensajes.get(0);
                            
                            // El cual guardaremos juento con su numero o posicion
                            int maxPuntaje = Integer.parseInt(auxMsg);
                            int ganador = 0;
                            
                            // Despues, recorreremos todos los puntajes comparando cual es el mayor de todos
                            for (int i=1; i < contaJugadores; i++)
                            {
                                auxMsg = listaMensajes.get(i);
                                
                                if (maxPuntaje < Integer.parseInt(auxMsg))
                                {
                                    maxPuntaje = Integer.parseInt(auxMsg);
                                    ganador = i;
                                }
                            }
                            
                            // Apenas pasamos el ciclo anterior ya tenemos quien fue el ganador de todos 
                            // por lo que recorremos una vez más todos los clientes enviadoles que 
                            for (int i=0; i < contaJugadores; i++)
                            {
                                // Si fue el ganador entonces
                                if (i == ganador)
                                {
                                    // Le enviamos al cliente i que es el ganador
                                    Socket clienteGanador = listaClientes.get(i);
                                    clienteGanador.getOutputStream().write("Ganaste!\n".getBytes());
                                }
                                else // sino
                                {
                                    // Le enviamos al cliente i que es un perdedor
                                    Socket clientePerdedor = listaClientes.get(i);
                                    clientePerdedor.getOutputStream().write("Perdiste!\n".getBytes());
                                }
                            }
                            
                            // Por ultimo y para evitar problemas vamos a borrar la puntuacion de algun jugador para que no vuelva a entrar aqui
                            listaMensajes.set(0, "");
                        }
                    }
                }
                
                // Si el admin del servidor (en cualquier momento) decide terminar con este proceso entonces
                if (escuchadorComandos.terminarPartida())
                {
                    // Rompo el ciclo infinito
                    break;
                }
            }
            
            // Una vez que termina el ciclo infinito entonces:::
        
            // Muestro un mensaje
            System.out.println("Cerrando el servidor...");

            // Termino apropiadamente los hilos que escuchaban los comandos y los clientes
            escuchadorComandos.interrupt();
            escuchadorClientes.detener();

            // Y cerramos el puerto del servidor
            serverSocket.close();
        }
        else // Sino
        {
            // Muestro un mensaje
            System.out.println("Elija S o M. Cerrando el servidor...");
        }
    }
}