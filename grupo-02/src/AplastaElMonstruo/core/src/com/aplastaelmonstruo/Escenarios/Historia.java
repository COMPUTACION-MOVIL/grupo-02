package com.aplastaelmonstruo.Escenarios;

import com.aplastaelmonstruo.Actores.Monstruo;
import com.aplastaelmonstruo.MainGame;
import com.aplastaelmonstruo.Pantallas.PantallaGameOver;
import com.aplastaelmonstruo.Pantallas.PantallaLogro;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

// Clase que controlara la modalidad de juego campaña o historia
public class Historia  implements Screen
{
    private MainGame juego; // Se crea una variable privada que nos permitira obtener/almecenar siempre la informacion del juego.

    private Texture fondoPantallaJuego; // Se crea una variable para la imagen que sera de fondo
    private OrthographicCamera camara; // Se crea una camara que nos permitira enfocar solo el area del juego que nos interesa mostrar al jugador
    private FitViewport vistaJuego; // Se crea un puerto o marco de vista el cual no es más que un adapatador de la imagen del juego con el tamaño del Display del dispostivo que ejecuta el juego.

    private HUD recuadroInfoJuego; // Se crea un HUD que es donde se mostrara el tiempo, puntaje, y otras cosas de interes para el jugador.
    private ArrayList<Monstruo> enemigos; // Se crea una lista o vector redimensionable en el cual se agregaran o eliminaran los enemigos

    // Se crea una variable que nos permitira leer y escribir un archivo de informacion sobre el desempeño del jugador
    Preferences prefs;

    // Y se crean algunas variables. La 1era para contar el tiempo, la 2da para saber si ya gano el jugador y la tercera para saber que nivel se esta jugando
    private float contaTiempo;
    private boolean gano;
    private int nivel;

    public Historia (final MainGame juego, int nivel) // Constructor de la pantalla de juego
    {
        this.juego = juego; // Aqui solo obtenemos el juego y lo guardamos
        this.nivel = nivel; // Hacemos lo mismo para el mundo/nivel

        // Ahora y dependiendo del nivel elegido por el usuario cargamos un fondo de pantalla
        switch (nivel)
        {
            case(1):
                fondoPantallaJuego = juego.getAdminComponentes().get("Imagenes/background01.jpg", Texture.class);
                break;
            case(2):
                fondoPantallaJuego = juego.getAdminComponentes().get("Imagenes/background02.jpg", Texture.class);
                break;
            case(3):
                fondoPantallaJuego = juego.getAdminComponentes().get("Imagenes/background03.jpg", Texture.class);
                break;
            case(4):
                fondoPantallaJuego = juego.getAdminComponentes().get("Imagenes/background04.jpg", Texture.class);
                break;
            case(5):
                fondoPantallaJuego = juego.getAdminComponentes().get("Imagenes/background05.jpg", Texture.class);
                break;
            case(6):
                fondoPantallaJuego = juego.getAdminComponentes().get("Imagenes/background06.jpg", Texture.class);
                break;
            case(7):
                fondoPantallaJuego = juego.getAdminComponentes().get("Imagenes/background08.jpg", Texture.class);
                break;
            case(8):
                fondoPantallaJuego = juego.getAdminComponentes().get("Imagenes/background09.jpg", Texture.class);
                break;
            case(9):
            case(10):
                fondoPantallaJuego = juego.getAdminComponentes().get("Imagenes/background10.jpg", Texture.class);
                break;
            default:
                fondoPantallaJuego = juego.getAdminComponentes().get("Imagenes/backgroundMenus.png", Texture.class);
        }

        camara = new OrthographicCamera(); // Despues, se inicializa la camara
        camara.setToOrtho(false); // Con esto se le indica a la camara que el origen de coordenadas (0,0) esta en la esquina inferior izquierda de la pantalla del dispositivo
        camara.position.set(MainGame.ANCHO_VIRTUAL / 2, MainGame.ALTO_VIRTUAL / 2, 0); // Ahora, con esta instruccion se le indica a la camara y al render() que el origen de coordenadas (0,0) tambien es la esquina inferior izquierda. Esto es importante porque sino esta entonces cualquier cosa que se dibuje comenzara en el centro de la pantalla.
        vistaJuego = new FitViewport(MainGame.ANCHO_VIRTUAL, MainGame.ALTO_VIRTUAL, camara); // Por ultimo, se inicializa la vista con unas dimensiones tentativas de 1280x720.

        // Tambien, se inicializa la lista de enemigos como vacia
        enemigos = new ArrayList<Monstruo>();

        // y luego se crean los enemigos del nivel (estos se generaran simpre de forma aleatoria)
        switch (nivel)
        {
            case(1):
            case(2):
            case(3):
                for (int i = 0; i < nivel*10; i++)
                {
                    enemigos.add(new Monstruo(MathUtils.random(0, MainGame.ANCHO_VIRTUAL), MathUtils.random(0, MainGame.ALTO_VIRTUAL), MathUtils.random(3,5)));
                }
                break;
            case(4):
            case(5):
            case(6):
            case(7):
                for (int i = 0; i < nivel*10; i++)
                {
                    enemigos.add(new Monstruo(MathUtils.random(0, MainGame.ANCHO_VIRTUAL), MathUtils.random(0, MainGame.ALTO_VIRTUAL), MathUtils.random(2,5)));
                }
                break;
            case(8):
            case(9):
            case(10):
                for (int i = 0; i < nivel*10; i++)
                {
                    enemigos.add(new Monstruo(MathUtils.random(0, MainGame.ANCHO_VIRTUAL), MathUtils.random(0, MainGame.ALTO_VIRTUAL), MathUtils.random(1,5)));
                }
                break;
        }

        // Despues se inicializa un controlador de tiempo y la variable que nos indicara si hemos ganado
        contaTiempo = 0f;
        gano = false;

        // Ahora, se inicializa la conexion con el archivo de informacion del modo campaña
        prefs = Gdx.app.getPreferences("InfoHistoria");

        // Por ultimo, se inicializa el HUD
        int tiempoMundo;

        switch (nivel)
        {
            case(1):
                tiempoMundo = 60;
                break;
            case(2):
                tiempoMundo = 90;
                break;
            case(3):
                tiempoMundo = 120;
                break;
            case(4):
                tiempoMundo = 160;
                break;
            case(5):
                tiempoMundo = 200;
                break;
            case(6):
                tiempoMundo = 250;
                break;
            case(7):
                tiempoMundo = 300;
                break;
            case(8):
                tiempoMundo = 360;
                break;
            case(9):
            case(10):
                tiempoMundo = 480;
                break;
            default:
                tiempoMundo = 999;
        }

        recuadroInfoJuego = new HUD(juego.dibujadorPantalla, tiempoMundo, 1, nivel);
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
            // Se escribe cual ha sido el puntaje obtenido para este nivel
            switch (nivel)
            {
                case(1):
                    prefs.putInteger("N1", recuadroInfoJuego.getPuntaje());
                    break;
                case(2):
                    prefs.putInteger("N2", recuadroInfoJuego.getPuntaje());
                    break;
                case(3):
                    prefs.putInteger("N3", recuadroInfoJuego.getPuntaje());
                    break;
                case(4):
                    prefs.putInteger("N4", recuadroInfoJuego.getPuntaje());
                    break;
                case(5):
                    prefs.putInteger("N5", recuadroInfoJuego.getPuntaje());
                    break;
                case(6):
                    prefs.putInteger("N6", recuadroInfoJuego.getPuntaje());
                    break;
                case(7):
                    prefs.putInteger("N7", recuadroInfoJuego.getPuntaje());
                    break;
                case(8):
                    prefs.putInteger("N8", recuadroInfoJuego.getPuntaje());
                    break;
                case(9):
                    prefs.putInteger("N9", recuadroInfoJuego.getPuntaje());
                    break;
                case(10):
                    prefs.putInteger("N10", recuadroInfoJuego.getPuntaje());
                    break;
            }

            prefs.flush(); // Esto confirma o escribe los cambios en disco

            // En caso que este sea el ultimo nivel del juego entonces
            if (nivel == 10)
            {
                // Se despliega la ventana de felicitaciones sin la opcion del siguiente nivel
                juego.setScreen(new PantallaLogro(juego, false, nivel));
            }
            else // sino
            {
                // Se despliega la ventana de felicitaciones con la opcion del siguiente nivel
                juego.setScreen(new PantallaLogro(juego, true, nivel));
            }

            dispose();
        }

        // Por ultimo, en caso que el jugador pierda entonces
        if(gameOver())
        {
            // Se muestra la pantalla de GameOver
            juego.setScreen(new PantallaGameOver(juego, true, nivel));
            dispose();
        }
    }

    // Metodo que separa la logica del juego y la logica del rederizado de libGDX
    public void actualizarPantallaJuego(float delta)
    {
        recuadroInfoJuego.actualizarTiempo(delta); // Con esto actualizamos el tiempo del HUD

        // Con el siguiente ciclo actualizamos el movimiento o posicion de los monstruos
        for (int i=0; i < enemigos.size(); i++)
        {
            // Metodo encargado de renovar las posiciones dado un diferencial de tiempo
            enemigos.get(i).actualizarPosicion(delta);

            // En caso que el enemigo haya muerto entonces
            if (enemigos.get(i).haMuerto())
            {
                // Se suma su valor en puntaje al puntaje general obtenido por el usuario
                recuadroInfoJuego.agregarPuntaje(enemigos.get(i).getPuntaje());

                // Luego, se suma uno al numero total de enemigos eliminados por el jugador
                recuadroInfoJuego.anotarEnemigo();

                // Y se elimina el monstruo de la lista de enemigos
                enemigos.remove(i);
                i = i - 1;
            }
        }






        // En caso que se hayan eliminado todos los enemigos de la pantalla antes de acabarse el tiempo entonces
        if (enemigos.size() == 0)
        {
            gano = true; // El jugador ha ganado el nivel
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

    // Metodo que esta revisando constatemente si el jugador ha perdido. La condicion para perder es: El tiempo del nivel llego a cero y aun no se han eliminado todos los monstruos
    public boolean gameOver()
    {
        return recuadroInfoJuego.tiempoAgotado();
    }

    // Metodo que se ejecuta cada vez que se redimensiona la pantalla
    @Override
    public void resize(int width, int height)
    {
        vistaJuego.update(width, height); // Esta instruccion permite actualizar las dimensiones de la camara y asi ajustar el juego a la pantalla del dispositivo movil
        camara.update(); // De igual modo se actualiza la camara
    }

    // Metodo que se ejecuta cuando la aplicacion recien se muestra al usuario
    @Override
    public void show()
    {
        // De inmediato, ponemos en marcha una melodia
        switch (nivel)
        {
            case(1):
            case(2):
                if (!juego.getAdminComponentes().get("Audios/bgmusic01.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/bgmusic01.ogg", Music.class).play();
                    juego.getAdminComponentes().get("Audios/bgmusic01.ogg", Music.class).setLooping(true);
                }
                break;
            case(3):
            case(4):
                if (!juego.getAdminComponentes().get("Audios/bgmusic02.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/bgmusic02.ogg", Music.class).play();
                    juego.getAdminComponentes().get("Audios/bgmusic02.ogg", Music.class).setLooping(true);
                }
                break;
            case(5):
                if (!juego.getAdminComponentes().get("Audios/bgmusic03.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/bgmusic03.ogg", Music.class).play();
                    juego.getAdminComponentes().get("Audios/bgmusic03.ogg", Music.class).setLooping(true);
                }
                break;
            case(6):
                if (!juego.getAdminComponentes().get("Audios/bgmusic04.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/bgmusic04.ogg", Music.class).play();
                    juego.getAdminComponentes().get("Audios/bgmusic04.ogg", Music.class).setLooping(true);
                }
                break;
            case(7):
                if (!juego.getAdminComponentes().get("Audios/bgmusic05.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/bgmusic05.ogg", Music.class).play();
                    juego.getAdminComponentes().get("Audios/bgmusic05.ogg", Music.class).setLooping(true);
                }
                break;
            case(8):
                if (!juego.getAdminComponentes().get("Audios/bgmusic06.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/bgmusic06.ogg", Music.class).play();
                    juego.getAdminComponentes().get("Audios/bgmusic06.ogg", Music.class).setLooping(true);
                }
                break;
            case(9):
            case(10):
                if (!juego.getAdminComponentes().get("Audios/bgmusic07.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/bgmusic07.ogg", Music.class).play();
                    juego.getAdminComponentes().get("Audios/bgmusic07.ogg", Music.class).setLooping(true);
                }
                break;
            default:
                if (!juego.getAdminComponentes().get("Audios/bgmusic08.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/bgmusic08.ogg", Music.class).play();
                    juego.getAdminComponentes().get("Audios/bgmusic08.ogg", Music.class).setLooping(true);
                }
        }
    }

    @Override
    public void dispose() // Cuando termina esta pantalla pues liberamos los recursos que ya no necesitamos
    {
        switch (nivel)
        {
            case(1):
            case(2):
                if (juego.getAdminComponentes().get("Audios/bgmusic01.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/bgmusic01.ogg", Music.class).stop();
                }
                break;
            case(3):
            case(4):
                if (juego.getAdminComponentes().get("Audios/bgmusic02.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/bgmusic02.ogg", Music.class).stop();
                }
                break;
            case(5):
                if (juego.getAdminComponentes().get("Audios/bgmusic03.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/bgmusic03.ogg", Music.class).stop();
                }
                break;
            case(6):
                if (juego.getAdminComponentes().get("Audios/bgmusic04.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/bgmusic04.ogg", Music.class).stop();
                }
                break;
            case(7):
                if (juego.getAdminComponentes().get("Audios/bgmusic05.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/bgmusic05.ogg", Music.class).stop();
                }
                break;
            case(8):
                if (juego.getAdminComponentes().get("Audios/bgmusic06.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/bgmusic06.ogg", Music.class).stop();
                }
                break;
            case(9):
            case(10):
                if (juego.getAdminComponentes().get("Audios/bgmusic07.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/bgmusic07.ogg", Music.class).stop();
                }
                break;
            default:
                if (juego.getAdminComponentes().get("Audios/bgmusic08.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/bgmusic08.ogg", Music.class).stop();
                }
        }

        recuadroInfoJuego.dispose();
        recuadroInfoJuego.dispose();
    }

    // Los siguientes metodos simplemente no se implementan
    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
