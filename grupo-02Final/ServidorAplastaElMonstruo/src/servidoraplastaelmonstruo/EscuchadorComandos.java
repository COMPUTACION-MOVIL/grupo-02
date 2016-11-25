package servidoraplastaelmonstruo;

import java.util.Scanner;

// Clase que estara encargada de estar pendiente de las instrucciones de la consola, de modo que
// si el administrador del servidor considera que ya se han conectado suficientes usuarios
// entoces con tan solo ingresar el comando listo se procede a iniciar la partida de supervivencia.
// O si se ingresa salir se termina o cierra el servidor.
public class EscuchadorComandos extends Thread
{
    final Scanner lectorConsola = new Scanner(System.in); // Variable que nos permite leer la consola
    public String comando; // Variable auxiliar que nos permitira procesar lo que el usuario haya escrito
    private boolean iniciar, terminar; // Variables para saber que accion realizar en el programa principal
    
    @Override
    public void run()
    {
        // Se inicializan las varaibles de accion
        iniciar = false;
        terminar = false;
        
        // Mientras la consola este abierta este hilo seguira corriendo
        while(!terminar)
        {
            // Si el usuario presiono ENTER se toma lo que haya escrito
            comando = lectorConsola.nextLine();
            
            // Si lo que escribio el usuario corresponde con el comando LISTO entonces
            if (comando.equalsIgnoreCase("LISTO"))
            {
                iniciar = true; // Se vuelve verdadero la variable para iniciar la partida
            }
            
            // Si lo que escribio el usuario corresponde con el comando SALIR entonces
            if (comando.equalsIgnoreCase("SALIR"))
            {
                terminar = true; // Se vuelve verdadero la variable para terminar con el proceso servidor
            }
        }
    }

    // Los siguientes dos metodos son simplemente getterns
    public synchronized boolean iniciar()
    {
        return iniciar;
    }
    
    public synchronized boolean terminarPartida()
    {
        return terminar;
    }
}
