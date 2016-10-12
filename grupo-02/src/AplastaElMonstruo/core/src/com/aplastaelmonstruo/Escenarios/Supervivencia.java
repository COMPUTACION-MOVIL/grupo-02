package com.aplastaelmonstruo.Escenarios;

import com.aplastaelmonstruo.Actores.Monstruo;
import com.aplastaelmonstruo.MainGame;
import com.aplastaelmonstruo.Pantallas.PantallaGameOver;
import com.aplastaelmonstruo.Pantallas.PantallaLogro;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

// Clase que controlara la modalidad de juego supervivencia
public class Supervivencia implements Screen
{
    private MainGame juego; // Se crea una variable privada que nos permitira obtener/almecenar siempre la informacion del juego.

    private Texture fondoPantallaJuego; // Se crea una variable para la imagen que sera de fondo
    private OrthographicCamera camara; // Se crea una camara que nos permitira enfocar solo el area del juego que nos interesa mostrar al jugador
    private FitViewport vistaJuego; // Se crea un puerto o marco de vista el cual no es más que un adapatador de la imagen del juego con el tamaño del Display del dispostivo que ejecuta el juego.

    private HUD recuadroInfoJuego; // Se crea un HUD que es donde se mostrara el tiempo, puntaje, y otras cosas de interes para el jugador.
    private ArrayList<Monstruo> enemigos; // Se crea una lista o vector redimensionable en el cual se agregaran o eliminaran los enemigos

    // Se crea una variable que nos permitira leer un archivo de configuraciones
    Preferences prefs;

    // Se crea una variable para la conexion con el servidor
    Socket socketCliente;

    // Y se crean algunas variables: la 1era para contar el tiempo, la 2da que indicara cada cuanto iran apareciendo enemigos, la 3era y la 4ta que son vitales para la conexion en red y la 5ta para identificarse en la red
    private float contaTiempo;
    private float tiempoParaGeneracion;
    String ip;
    String puerto;
    String nombre;

    // Tambien creo una variable que me permitira determinar si el jugador oponente ya perdio
    public boolean gano;
    public int enemigoTransferido;

    public Supervivencia(final MainGame juego) // Constructor de la pantalla de juego
    {
        this.juego = juego; // Aqui solo obtenemos el juego y lo guardamos

        fondoPantallaJuego = juego.getAdminComponentes().get("Imagenes/background07.jpg", Texture.class); // Ahora, cargamos el fondo para partida

        camara = new OrthographicCamera(); // Despues, se inicializa la camara
        camara.setToOrtho(false); // Con esto se le indica a la camara que el origen de coordenadas (0,0) esta en la esquina inferior izquierda de la pantalla del dispositivo
        camara.position.set(MainGame.ANCHO_VIRTUAL / 2, MainGame.ALTO_VIRTUAL / 2, 0); // Ahora, con esta instruccion se le indica a la camara y al render() que el origen de coordenadas (0,0) tambien es la esquina inferior izquierda. Esto es importante porque sino esta entonces cualquier cosa que se dibuje comenzara en el centro de la pantalla.
        vistaJuego = new FitViewport(MainGame.ANCHO_VIRTUAL, MainGame.ALTO_VIRTUAL, camara); // Por ultimo, se inicializa la vista con unas dimensiones tentativas de 1280x720.

        // Tambien, se inicializa la lista de enemigos
        enemigos = new ArrayList<Monstruo>();

        // Se crean algunos enemigos iniciales
        enemigos.add(new Monstruo(100, 100, Monstruo.CACHOS));
        enemigos.add(new Monstruo(200, 200, Monstruo.PINCHOS));
        enemigos.add(new Monstruo(400, 400, Monstruo.AMEBA));
        enemigos.add(new Monstruo(700, 500, Monstruo.PELUZA));
        enemigos.add(new Monstruo(1000, 600, Monstruo.ZOMBIE));

        // Se inician controladoras de los tiempos de generacion de monstruos y la variable que nos indicara si hemos ganado
        contaTiempo = 0f;
        tiempoParaGeneracion = 10f;
        gano = false;
        enemigoTransferido = -1;

        // Ahora, se inicializa la conexion con el archivo de configuraciones
        prefs = Gdx.app.getPreferences("ConfigGame");

        // y despues se leen la ip y el puerto de conexion con el servidor, como tambien el nombre de este usuario
        ip = prefs.getString("ipServer", "");
        puerto = prefs.getString("portServer", "");
        nombre = prefs.getString("userName", "");

                // Seguidamente, creamos un hilo nuevo en el cual se escuchara continuamente al servidor
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                // 1ero - Creamos un puerto de conexion el cual este escuchando de manera concurrente el puerto definido en las configuraciones
                ServerSocketHints socketSeverParam = new ServerSocketHints();
                socketSeverParam.acceptTimeout = 0;
                ServerSocket socketServer = Gdx.net.newServerSocket(Net.Protocol.TCP, Integer.parseInt(puerto), socketSeverParam);

                // 2do - Establecemos la conexion con el cliente, osea el otro jugador.
                Socket client = socketServer.accept(null);

                // 3ero - Creamos una variable centinela la cual nos permitira revisar constantemente que nos ha enviado el otro jugador
                boolean centinela = true;

                // 4to - Mientras el otro jugador no pierda se procede con:
                while (centinela)
                {
                    try
                    {
                        // Se crea un lector para el puerto y se revisa/lee que hay en el mismo
                        BufferedReader buffer = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        String mensaje = buffer.readLine();

                        // Luego, trataremos de segmentar el mensaje que se recibio
                        String[] tokens = mensaje.split(" ");

                        // Si el mensaje posee dos elementos entonces
                        if (tokens.length == 2)
                        {
                            // Si el 2do token/elemento es la palabra: "perdio!" entonces
                            if (tokens[1].equals("perdio!"))
                            {
                                // Se levanta/marca la variable de victoria para este jugador
                                gano = true;

                                // Y se procede a terminar este ciclo, el cual a su vez terminara el hilo
                                centinela = false;
                            }
                        }

                        // Por otro lado, si el mensaje posee tres elementos entonces
                        if (tokens.length == 3)
                        {
                            // Es porque el otro jugador acabo de eliminar un enemigo, y ahora me corresponde a mi eliminarlo (pasarselo a el de nuevo), por lo que leo cual era el tipo y espero que el generador lo cree
                            enemigoTransferido = Integer.parseInt(tokens[2]);
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

        // Tmabien, se inicializa la conexion cliente.
        // Si todos los datos de conexion al servidor son correctos entonces
        if (!(ip.equals("") || puerto.equals("") || nombre.equals("")))
        {
            // Se crea una variable de parametros de conexion
            SocketHints socketParam = new SocketHints();
            socketParam.connectTimeout = 4000;
            socketParam.trafficClass = 0x10 | 0x08 | 0x02;
            socketParam.performancePrefLatency = 9999;

            // Y se genera la conexion
            socketCliente = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, Integer.parseInt(puerto), socketParam);
        }

        // Por ultimo, se inicializa el HUD
        recuadroInfoJuego = new HUD(juego.dibujadorPantalla, 999, 3, 0);
        recuadroInfoJuego.actualizarNumJugadores(2);
    }

    @Override
    public void render(float delta)
    {
        // En primer lugar, realizamos toda la logica interna del juego
        actualizarPantallaJuego(delta);

        // Luego, limpiamos la pantalla antes de dibujar cualquier cosa
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Ahora, con la siguiente instruccion se procede a decirle al juego en donde debe apuntar la camara segun la vista que se establecio
        camara.update();
        juego.dibujadorPantalla.setProjectionMatrix(camara.combined);

        // A continuacion, procedemos a dibujar
        juego.dibujadorPantalla.begin();

        // Se dibuja el fondo
        juego.dibujadorPantalla.draw(fondoPantallaJuego, 0, 0, MainGame.ANCHO_VIRTUAL, MainGame.ALTO_VIRTUAL); // Aqui se dibuja el fondo y se ajusta a las dimensiones dadas

        // Se dibujan los enemigos
        for (int i=0; i < enemigos.size(); i++)
        {
            enemigos.get(i).dibujarMonstruo(juego.dibujadorPantalla);
        }

        juego.dibujadorPantalla.end();

        // Se dibuja el HUD
        juego.dibujadorPantalla.setProjectionMatrix(recuadroInfoJuego.molduraJuego.getCamera().combined);
        recuadroInfoJuego.molduraJuego.draw();

        // Si el usuario ha tocado la pantalla entonces
        if (Gdx.input.justTouched())
        {
            // Se revisa si ha golpeado un monstruo
            manejadorEntradas(Gdx.input.getX(), MainGame.ALTO_VIRTUAL - Gdx.input.getY());
            // NOTA: La coordenada Y debe ser la resta del alto del dispositivo menos la coordenada señalada por el usuario porque LibGDX entiende en sentido contrario dicha coordena
        }

        // Antes de revisar si perdimos se procede a revisar si ganamos y en caso que si hayamos ganado entonces
        if (gano)
        {
            // Se despliega la ventana de felicitaciones
            juego.setScreen(new PantallaLogro(juego, false, 0));
            dispose();
        }

        // Por ultimo, en caso que el jugador pierda entonces
        if(gameOver())
        {
            try
            {
                // Intentamos enviar al servidor el mensaje que este jugador perdio
                String aux = nombre + " perdio!";
                socketCliente.getOutputStream().write(aux.getBytes());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            // Seguidamente, se muestra la pantalla de GameOver
            juego.setScreen(new PantallaGameOver(juego, false, 0));
            dispose();
        }
    }

    // Metodo que separa la logica del juego y la logica del rederizado de libGDX
    public void actualizarPantallaJuego(float delta)
    {
        //recuadroInfoJuego.actualizarTiempo(delta); // Con esto actualizamos el tiempo del HUD

        // Con el siguiente ciclo actualizamos el movimiento o posicion de los monstruos
        for (int i=0; i < enemigos.size(); i++)
        {
            // Metodo encargado de renovar las posiciones dado un diferencial de tiempo
            enemigos.get(i).actualizarPosicion(delta);

            // En caso que el enemigo haya muerto entonces
            if (enemigos.get(i).haMuerto())
            {
                // Se lo pasamos al jugador contrario de modo que
                try
                {
                    // Le enviamos un mensaje sobre que tipo de enemigo debe generar de manera aleatoria
                    String aux = "Transferir enemigo: " + enemigos.get(i).getTipo() + "\n";
                    socketCliente.getOutputStream().write(aux.getBytes());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                // Se suma su valor en puntaje al puntaje general obtenido por el usuario
                recuadroInfoJuego.agregarPuntaje(enemigos.get(i).getPuntaje());

                // Luego, se suma uno al numero total de enemigos eliminados por el jugador
                recuadroInfoJuego.anotarEnemigo();

                // Y se elimina el monstruo de la lista de enemigos
                enemigos.remove(i);
                i = i - 1;
            }
        }

        // Si ya se cumplio el tiempo para generar otro monstruo entonces
        if (contaTiempo >= tiempoParaGeneracion)
        {
            // Agregamos un nuevo enemigo aleatorio en coordenas generadas tambien de manera aleatoria
            enemigos.add(new Monstruo(MathUtils.random(0, MainGame.ANCHO_VIRTUAL), MathUtils.random(0, MainGame.ALTO_VIRTUAL), MathUtils.random(1,5)));

            // Y reseteamos el contador de tiempo
            contaTiempo = 0;
        }

        // O si se detecto que me pasaron un monstruo entonces
        if (enemigoTransferido != -1)
        {
            // Genero el tipo de monstruo especificado en coordenadas aleatorias
            enemigos.add(new Monstruo(MathUtils.random(0, MainGame.ANCHO_VIRTUAL), MathUtils.random(0, MainGame.ALTO_VIRTUAL), enemigoTransferido));

            // Y reinicio el dectector de monstruos tranferidos
            enemigoTransferido = -1;
        }

        contaTiempo = contaTiempo + delta; // Por ultimo se cuenta el tiempo
    }

    // Metodo que revisara en el render si se ha tocado un actor
    public void manejadorEntradas(float x, float y)
    {
        for (int i=0; i < enemigos.size(); i++)
        {
            // Y en caso de que si se haya impactado de manera correcta entonces
            if (enemigos.get(i).impactado(x, y))
            {
                // segun la vida del mosntruo se emitira un sonido
                switch (enemigos.get(i).getVida())
                {
                    case(0):
                        juego.getAdminComponentes().get("Audios/finalHit.ogg", Sound.class).play();
                        break;
                    case(1):
                        juego.getAdminComponentes().get("Audios/hit1.ogg", Sound.class).play();
                        break;
                    case(2):
                        juego.getAdminComponentes().get("Audios/hit2.ogg", Sound.class).play();
                        break;
                    default:
                        juego.getAdminComponentes().get("Audios/hit3.ogg", Sound.class).play();
                        break;
                }
            }
        }
    }

    // Metodo que esta revisando constatemente si el jugador ha perdido. Las condicion para perder es: Que se hayan acumulado 10 monstruos
    public boolean gameOver()
    {
        return enemigos.size() == 10;
    }

    // Metodo que se ejecuta cada vez que se redimensiona la pantalla
    @Override
    public void resize(int width, int height)
    {
        vistaJuego.update(width, height); // Esta por su parte permite actualizar las dimensiones de la camara y asi se ajuste el juego a la pantalla del dispositivo movil
        camara.update(); // De igual modo se actualiza la camara
    }

    // Metodo que se ejecuta cuando la aplicacion recien se muestra al usuario
    @Override
    public void show()
    {
        // De inmediato, ponemos en marcha una melodia
        if (!juego.getAdminComponentes().get("Audios/bgmusic06.ogg", Music.class).isPlaying())
        {
            juego.getAdminComponentes().get("Audios/bgmusic06.ogg", Music.class).play();
            juego.getAdminComponentes().get("Audios/bgmusic06.ogg", Music.class).setLooping(true);
        }
    }

    @Override
    public void hide() // Si la pantalla se oculta se liberan los recursos
    {
        dispose();
    }

    @Override
    public void dispose() // Cuando termina esta pantalla pues liberamos los recursos que ya no necesitamos
    {
        if (juego.getAdminComponentes().get("Audios/bgmusic06.ogg", Music.class).isPlaying())
        {
            juego.getAdminComponentes().get("Audios/bgmusic06.ogg", Music.class).stop();
        }

        recuadroInfoJuego.dispose();
        socketCliente.dispose();
        recuadroInfoJuego.dispose();
    }

    // Los siguientes metodos simplemente no se implementan
    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
