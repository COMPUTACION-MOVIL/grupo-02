package com.aplastaelmonstruo.Pantallas;

import com.aplastaelmonstruo.MainGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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

// Clase para la pantalla de juego terminado
public class PantallaGameOver implements Screen
{
    private MainGame juego; // Se crea una variable privada que nos permitira obtener/almecenar siempre la informacion del juego.

    private Texture fondoPantallaGameOver; // Se crea una variable para la imagen que sera de fondo para esta pantalla
    private OrthographicCamera camara; // Se crea una camara que nos permitira enfocar solo el area del juego que nos interesa mostrar al jugador
    private FitViewport vistaGameOver; // Se crea un puerto o marco de vista el cual no es más que un adapatador de la imagen del juego con el tamaño del Display del dispostivo que ejecuta el juego

    // Tambien se crean todos los elementos basicos para armar la pantalla de GAME OVER.
    Stage escenarioGO;
    Table organizadorGO;
    Skin skinGO;
    Image tituloGO;
    TextButton botonMenu;
    TextButton botonReintentar;

    public PantallaGameOver(final MainGame juego) // Constructor de la pantalla GAME OVER
    {
        this.juego = juego; // Aqui solo obtenemos el juego y lo guardamos

        tituloGO = new Image(juego.getAdminComponentes().get("Imagenes/gameover.png", Texture.class)); // Cargamos el GAME OVER
        tituloGO.setScaling(Scaling.fit); // Aqui se escala la imagen dependiendo del tamaño del display
        fondoPantallaGameOver = juego.getAdminComponentes().get("Imagenes/backgroundMenus.png", Texture.class); // Ahora, cargamos el fondo para pantalla

        // Se inicializan los elementos basicos de esta pantalla
        escenarioGO = new Stage();
        organizadorGO = new Table();
        organizadorGO.setFillParent(true);

        // Cargo el estilo basico de los botones
        skinGO = new Skin(Gdx.files.internal("Skin/uiskin.json"));

        // Creo los botones con sus respectivas etiquetas
        botonMenu = new TextButton("Volver al menu", skinGO);
        botonReintentar = new TextButton("Intentar de nuevo", skinGO);

        // Ahora, pongo el GAME OVER y los botones en una tabla, lo cual me permitira mejorar las distribuciones de los elementos
        organizadorGO.defaults().pad(20.0f);
        organizadorGO.add(tituloGO).colspan(2).center();
        organizadorGO.row();
        organizadorGO.add(botonMenu).width(juego.ANCHO_VIRTUAL/4).height(juego.ALTO_VIRTUAL/5).center();
        organizadorGO.add(botonReintentar).width(juego.ANCHO_VIRTUAL/4).height(juego.ALTO_VIRTUAL/5).center();

        // A continuacion se procede a registrar las acciones que cada boton debe realizar
        botonMenu.addListener(new ClickListener(){
            @Override
            public  void clicked (InputEvent event, float x, float y)
            {
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        botonReintentar.addListener(new ClickListener(){
            @Override
            public  void clicked (InputEvent event, float x, float y)
            {
                //
                // Aqui se escribiria la logica de volver a intentar el nivel
                //
            }
        });

        // Ya con todos los elementos organizados procedemos a agregarlos al escenario, que es al fin y al cabo quien dirige la pantalla
        escenarioGO.addActor(organizadorGO);

        camara = new OrthographicCamera(); // Despues, se inicializa la camara
        camara.setToOrtho(false); // Con esto se le indica a la camara que el origen de coordenadas (0,0) esta en la esquina inferior izquierda de la pantalla del dispositivo
        camara.position.set(juego.ANCHO_VIRTUAL / 2, juego.ALTO_VIRTUAL / 2, 0); // Ahora, con esta instruccion se le indica a la camara y al render() que el origen de coordenadas (0,0) tambien es la esquina inferior izquierda. Esto es importante porque sino esta entonces cualquier cosa que se dibuje comenzara en el centro de la pantalla.
        vistaGameOver = new FitViewport(juego.ANCHO_VIRTUAL, juego.ALTO_VIRTUAL, camara); // Por ultimo, se inicializa la vista con unas dimensiones tentativas de 1280x720.
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

        // Finalmente, procedemos a dibujar la pantalla
        juego.dibujadorPantalla.begin();
        juego.dibujadorPantalla.draw(fondoPantallaGameOver, 0, 0,juego.ANCHO_VIRTUAL, juego.ALTO_VIRTUAL); // Aqui se dibuja el fondo y se ajusta a las dimensiones dadas
        juego.dibujadorPantalla.end();

        // A continuacion se actualiza el escenario del menu
        escenarioGO.act(delta);
        escenarioGO.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        escenarioGO.getViewport().update(width, height, true); // Con esta instruccion se ajusta el tamaño del escenario
        vistaGameOver.update(width, height); // Esta por su parte permite actualizar las dimensiones de la camara y asi se ajuste el juego a la pantalla del dispositivo movil
        camara.update(); // De igual modo se actualiza la camara
    }

    @Override
    public void show()
    {
        // En el momento que la pantalla se muestra al usuario la siguiente instruccion le da al escenario el control de los eventos que haga el usuario (asi podremos detectar cuando toca la pantalla)
        Gdx.input.setInputProcessor(escenarioGO);

        // Y ponemos un sonido de burla inidcando que perdio
        juego.getAdminComponentes().get("Audios/sad-trombone.ogg", Sound.class).play();
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
        fondoPantallaGameOver.dispose();
        escenarioGO.dispose();
        skinGO.dispose();
    }

    // Los siguientes metodos simplemente no se implementan
    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
