package com.aplastaelmonstruo.Pantallas;

import com.aplastaelmonstruo.MainGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;

// Clase encargada del menu principal del juego.
public class PantallaMenu implements Screen
{
    private MainGame juego; // Se crea una variable privada que nos permitira obtener/almecenar siempre la informacion del juego.

    private Texture fondoPantallaMenus; // Se crea una variable para la imagen que sera de fondo para este menu
    private OrthographicCamera camara; // Se crea una camara que nos permitira enfocar solo el area del juego que nos interesa mostrar al jugador
    private FitViewport vistaMenu; // Se crea un puerto o marco de vista el cual no es más que un adapatador de la imagen del juego con el tamaño del Display del dispostivo que ejecuta el juego

    // Y por ultimo se crean todos los elementos basicos para armar el menu
    Stage escenarioMenu;
    Table organizadorMenu;
    Skin skinMenu;
    Image tituloJuego;
    TextButton botonJugar;
    TextButton botonOpciones;
    TextButton botonSalir;

    public PantallaMenu(final MainGame juego) // Constructor de la pantalla menu
    {
        this.juego = juego; // Aqui solo obtenemos el juego y lo guardamos

        tituloJuego = new Image(juego.getAdminComponentes().get("Imagenes/title.png", Texture.class)); // Cargamos  la imagen que es titulo del juego
        tituloJuego.setScaling(Scaling.fit); // Aqui se escala la imagen de titulo dependiendo del tamaño del display
        fondoPantallaMenus = juego.getAdminComponentes().get("Imagenes/backgroundMenus.png", Texture.class); // Ahora, cargamos el fondo para este menu

        // Se inicializan los elementos basicos del menu
        escenarioMenu = new Stage();
        organizadorMenu = new Table();
        organizadorMenu.setFillParent(true);

        // Cargo el estilo basico de los botones
        skinMenu = new Skin(Gdx.files.internal("Skin/uiskin.json"));

        // Creo los botones con sus respectivas etiquetas
        botonJugar = new TextButton("JUGAR", skinMenu);
        botonOpciones = new TextButton("OPCIONES", skinMenu);
        botonSalir = new TextButton("SALIR", skinMenu);

        // Ahora, pongo el titulo y los botones de este menu en una tabla, lo cual me permitira mejorar las distribuciones de los elementos
        organizadorMenu.defaults().pad(20.0f);
        organizadorMenu.add(tituloJuego).top();
        organizadorMenu.row();
        organizadorMenu.add(botonJugar).width(MainGame.ANCHO_VIRTUAL/4).height(MainGame.ALTO_VIRTUAL/8);
        organizadorMenu.row();
        organizadorMenu.add(botonOpciones).width(MainGame.ANCHO_VIRTUAL/4).height(MainGame.ALTO_VIRTUAL/8);
        organizadorMenu.row();
        organizadorMenu.add(botonSalir).width(MainGame.ANCHO_VIRTUAL/4).height(MainGame.ALTO_VIRTUAL/8);
        organizadorMenu.center();

        // A continuacion se procede a registrar las acciones que cada boton debe realizar
        botonJugar.addListener(new ClickListener(){
            @Override
            public  void clicked (InputEvent event, float x, float y)
            {
                juego.setScreen(new PantallaModo(juego));
            }
        });

        botonOpciones.addListener(new ClickListener(){
            @Override
            public  void clicked (InputEvent event, float x, float y)
            {
                juego.setScreen(new PantallaConfig(juego));
            }
        });

        botonSalir.addListener(new ClickListener(){
            @Override
            public  void clicked (InputEvent event, float x, float y)
            {
                if (juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).stop();
                }
                Gdx.app.exit(); // Esta instruccion en especial termina de manera adecuda la apliacion
            }
        });

        // Ya con todos los elementos organizados procedemos a agregarlos al escenario, que es al fin y al cabo quien dirige la pantalla
        escenarioMenu.addActor(organizadorMenu);

        camara = new OrthographicCamera(); // Despues, se inicializa la camara
        camara.setToOrtho(false); // Con esto se le indica a la camara que el origen de coordenadas (0,0) esta en la esquina inferior izquierda de la pantalla del dispositivo
        camara.position.set(MainGame.ANCHO_VIRTUAL / 2, MainGame.ALTO_VIRTUAL / 2, 0); // Ahora, con esta instruccion se le indica a la camara y al render() que el origen de coordenadas (0,0) tambien es la esquina inferior izquierda. Esto es importante porque sino esta entonces cualquier cosa que se dibuje comenzara en el centro de la pantalla.
        vistaMenu = new FitViewport(MainGame.ANCHO_VIRTUAL, MainGame.ALTO_VIRTUAL, camara); // Por ultimo, se inicializa la vista con unas dimensiones tentativas de 1280x720.
    }

    @Override
    public void render(float delta)
    {
        // Limpiamos la pantalla antes de dibujar cualquier cosa
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Ahora, con la siguiente instruccion se procede a decirle al juego en donde debe apuntar la camara segun la vista que se establecio
        camara.update();
        juego.dibujadorPantalla.setProjectionMatrix(camara.combined);

        // Finalmente, procedemos a dibujar el menu
        juego.dibujadorPantalla.begin();
        juego.dibujadorPantalla.draw(fondoPantallaMenus, 0, 0, MainGame.ANCHO_VIRTUAL, MainGame.ALTO_VIRTUAL); // Aqui se dibuja el fondo y se ajusta a las dimensiones dadas
        juego.dibujadorPantalla.end();

        // A continuacion se actualiza el escenario del menu
        escenarioMenu.act(delta);
        escenarioMenu.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        escenarioMenu.getViewport().update(width, height, true); // Con esta instruccion se ajusta el tamaño del escenario
        vistaMenu.update(width, height); // Esta por su parte permite actualizar las dimensiones de la camara y asi se ajuste el juego a la pantalla del dispositivo movil
        camara.update(); // De igual modo se actualiza la camara
    }

    @Override
    public void show()
    {
        // En el momento que la pantalla se muestra al usuario la siguiente instruccion le da al escenario el control de los eventos que haga el usuario (asi podremos detectar cuando toca la pantalla)
        Gdx.input.setInputProcessor(escenarioMenu);

        // Y ponemos en marcha una melodia
        if (!juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).isPlaying())
        {
            juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).play();
            juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).setLooping(true);
        }
    }

    @Override
    public void hide()
    {
        // En caso que la actividad (pantalla) pierda el foco entonces se le quita al escenario el control de eventos, esto es importante porque sino se hace a veces pueden aparecer comportamientos extraños
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() // Cuando termina esta pantalla pues liberamos los recursos que ya no necesitamos
    {
        skinMenu.dispose();
        fondoPantallaMenus.dispose();
        escenarioMenu.dispose();
    }

    // Los siguientes metodos simplemente no se implementan
    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
