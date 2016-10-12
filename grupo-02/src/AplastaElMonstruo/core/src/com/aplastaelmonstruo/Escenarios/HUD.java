package com.aplastaelmonstruo.Escenarios;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

// Clase encargada del marco o recuadro de cada mundo de juego
public class HUD implements Disposable
{
    // Se crea un escenario y una vista independiente para este elemento porque la idea es que el juego se pueda mover pero este recuadro quedarse estatico
    public Stage molduraJuego;
    private Viewport vistaHUD;

    // Luego, creamos algunas variables con las cuales llevares el tiempo, puntaje y otros datos que influyen en el juego
    private Integer tiempoMundo;
    private float contaTiempo;
    private Integer numEnemigos;
    private Integer puntaje;
    private Integer modoHUD;
    private Integer jugadores;
    private boolean tiempoTerminado;

    // De igual modo, creo unas etiquetas que es donde se mostraran dichos datos
    private Label lTiempoRestante;
    private Label lNumEnemigos;
    private Label lPuntos;
    private Label lConta;

    // Constructor
    // El constructor requiere como parametros:
    //          dibujador = Es el elemento que dibuja los componentes del juego en pantalla
    //          tm = Es el tiempo maximo que tiene el jugador para terminar con el nivel que esta jugando
    //          modo = Si es 1: modo campaña, 2: modo multijugador y 3: modo supervivencia
    //          mundo = Este parametro solo es valido si modo tiene valor 1 y el cual indica el nivel o mundo que se esta jugando de la campaña
    public HUD(SpriteBatch dibujador, int tm, int modo, int mundo)
    {
        // Inicializamos las variables del HUD
        tiempoMundo = tm;
        contaTiempo = 0f;
        numEnemigos = 0;
        puntaje = 0;
        modoHUD = modo;
        jugadores = 0;
        tiempoTerminado = false;

        // Se inicializa el escenario y la vista del HUD
        vistaHUD = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        molduraJuego = new Stage(vistaHUD, dibujador);

        // Se establecen las estiquetas iniciales del HUD
        lTiempoRestante = new Label("TIEMPO: " + tiempoMundo.toString(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        lNumEnemigos = new Label("ENEMIGOS: " + numEnemigos.toString(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        lPuntos = new Label("PUNTAJE: " + puntaje.toString(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        if (modo == 1)
        {
            lConta = new Label("NIVEL: " + mundo, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        }
        else
        {
            lConta = new Label("JUGADORES: " + jugadores.toString(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        }

        // Se crea un color de fondo para las barras del HUD. De manera arbitraria decidimos que sean negras
        Pixmap colorFondoHUD = new Pixmap(1, 1, Pixmap.Format.RGB565);
        colorFondoHUD.setColor(Color.BLACK);
        colorFondoHUD.fill();

        // Despues, se crean tanto la barra superior, la cual contendra el tiempo y el numero de enemigos eliminados
        Table barraSuperior = new Table();
        barraSuperior.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(colorFondoHUD))));
        barraSuperior.add(lTiempoRestante).expandX().left().pad(10);
        barraSuperior.add(lNumEnemigos).expandX().right().pad(10);

        // Y la barra inferior tendra el puntaje y el nivel del modo campaña o la cantidad de jugadores que estan en linea
        Table barraInferior = new Table();
        barraInferior.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(colorFondoHUD))));
        barraInferior.add(lPuntos).expandX().left().pad(10);
        barraInferior.add(lConta).expandX().right().pad(10);

        // A continuacion, despelagamos las barras en dos tablas como sifueran filas, esto es solo por organizacion de los elemtos del HUD
        Table organizadorHUD1 = new Table();
        organizadorHUD1.setFillParent(true);
        organizadorHUD1.top();
        organizadorHUD1.row().expandX();
        organizadorHUD1.add(barraSuperior).fillX().top();

        Table organizadorHUD2 = new Table();
        organizadorHUD2.setFillParent(true);
        organizadorHUD2.bottom();
        organizadorHUD2.row().expandX();
        organizadorHUD2.add(barraInferior).fillX().bottom();

        // Y finalmente, se ponen dichas barras como actores del escenario
        molduraJuego.addActor(organizadorHUD1);
        molduraJuego.addActor(organizadorHUD2);
    }

    // Metodo que permite actualizar el tiempo de la sesion de juego
    public void actualizarTiempo(float delta)
    {
        // La estrategia es que en contaTiempo se van sumando fracciones de tiempo hasta que se llega a uno o se supere un segundo
        contaTiempo = contaTiempo + delta;

        if (contaTiempo >= 1) // Una vez ha trancurrido una unidad de tiempo entonces
        {
            // Preguntamos si el contador general de tiempo todavia no se ha agotado
            if (tiempoMundo > 0)
            {
                tiempoMundo = tiempoMundo - 1; // Si en verdad no se ha agotado, entonces le restamos un segundo
            }
            else
            {
                tiempoTerminado = true; // Pero si el contador general ya llego a cero entonces volvemos verdadero la variable tiempoTerminado
            }

            // Independiente de si se acabo el tiempo o no, actualizamos la etiqueta de tiempo que ve el usuario
            lTiempoRestante.setText("TIEMPO: " + tiempoMundo.toString());
            // Y volvemos a cero contaTiempo para que se pueda volver a contar otro segundo
            contaTiempo = 0;
        }
    }

    // Metodo para agregar o aumentar la cantidad de enemigos que se han eliminado
    public void  anotarEnemigo()
    {
        numEnemigos = numEnemigos + 1;
        lNumEnemigos.setText("ENEMIGOS: " + numEnemigos.toString());
    }

    // Metodo para sumar un determinado puntaje al puntaje general de la partida
    public void agregarPuntaje(int valor)
    {
        puntaje = puntaje + valor;
        lPuntos.setText("PUNTAJE: " + puntaje.toString());
    }

    // Metodo para recuperar el puntaje que se lleva acumulado
    public int getPuntaje()
    {
        return puntaje;
    }

    // Metodo que establece cual es el numero de jugadores en los modos multijugador o supervivencia
    public void actualizarNumJugadores(int valor)
    {
        if (modoHUD != 1)
        {
            jugadores = valor;
            lConta.setText("JUGADORES: " + jugadores.toString());
        }
    }

    // Metodo que retorna el numero de jugadores vigentes en la partida
    public int getNumJugadores() { return jugadores; }

    // Metodo que nos permite saber si se temrino el tiempo o no
    public boolean tiempoAgotado()
    {
        return tiempoTerminado;
    }

    // Metodo con el cual se liberan los recursos del HUD en memoria una vez se sale de la pantalla de juego
    @Override
    public void dispose()
    {
        molduraJuego.dispose();
    }
}
