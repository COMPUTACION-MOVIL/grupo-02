package com.aplastaelmonstruo.Pantallas;

import com.aplastaelmonstruo.MainGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;

// Clase encargada del menu de configuraciones del juego
public class PantallaConfig implements Screen
{
    private MainGame juego; // Se crea un variable privada que nos permitira obtener/almecenar siempre la informacion del juego.

    private Texture fondoPantallaConf; // Se crea una variable para la imagen que sera de fondo para este menu de configuraciones
    private OrthographicCamera camara; // Se crea una camara que nos permitira enfocar solo el area del juego que nos interesa mostrar al jugador
    private FitViewport vistaConf; // Se crea un puerto o marco de vista el cual no es más que un adapatador de la imagen del juego con el tamaño del Display del dispostivo que ejecuta el juego

    // Tambien se crean todos los elementos basicos para armar el menu de modos de juego
    Stage escenarioConf;
    Table organizadorConf;
    Skin skinConf;
    Image tituloJuego;

    Label etiquetaConf;
    Label etiquetaNivelDificultad;
    Label etiquetaConexionServer;
    Label etiquetaPuertoComunicacion;
    Label etiquetaUsuario;

    SelectBox selectorDificultad;
    TextField campoConexion;
    TextField campoPuerto;
    TextField campoUsuario;

    TextureRegion volverRegion;
    TextureRegionDrawable volverDibujable;
    ImageButton botonVolver;

    public PantallaConfig(final MainGame juego) // Constructor de la pantalla configuraciones
    {
        this.juego = juego; // Aqui solo obtenemos el juego y lo guardamos

        tituloJuego = new Image(juego.getAdminComponentes().get("Imagenes/title.png", Texture.class)); // Cargamos  la imagen que es titulo del juego
        tituloJuego.setScaling(Scaling.fit); // Aqui se escala la imagen de titulo dependiendo del tamaño del display
        fondoPantallaConf = juego.getAdminComponentes().get("Imagenes/backgroundMenus.png", Texture.class); // Ahora, cargamos el fondo para este menu

        // Se inicializan los elementos basicos del menu modos
        escenarioConf = new Stage();
        organizadorConf= new Table();
        organizadorConf.setFillParent(true);

        // Cargo el estilo basico de los botones y otros elementos
        skinConf = new Skin(Gdx.files.internal("Skin/uiskin.json"));

        // Creo las etiquetas que indicaran o señalaran que campo estoy editando
        etiquetaConf = new Label("CONFIGURACIONES", skinConf);
        etiquetaNivelDificultad = new Label("Dificultad: ", skinConf);
        etiquetaConexionServer = new Label("IP servidor: ", skinConf);
        etiquetaPuertoComunicacion = new Label("Puerto: ", skinConf);
        etiquetaUsuario = new Label("Usuario: ", skinConf);

        // Creo el selector de dificultad y los campos donde el usuario escribira sus configuraciones
        String[] listaNivelesDificultad = new String[4];
        listaNivelesDificultad[0] = "FACIL";
        listaNivelesDificultad[1] = "MODERADA";
        listaNivelesDificultad[2] = "DIFICIL";
        listaNivelesDificultad[3] = "HARDCORE";
        selectorDificultad = new SelectBox<String>(skinConf);
        selectorDificultad.setItems(listaNivelesDificultad);
        campoConexion = new TextField("", skinConf);
        campoConexion.setFocusTraversal(true);
        campoPuerto = new TextField("", skinConf);
        campoPuerto.setFocusTraversal(true);
        campoUsuario = new TextField("", skinConf);
        campoUsuario.setFocusTraversal(true);

        // Y creo el boton para volver al menu principal del juego
        volverRegion = new TextureRegion(juego.getAdminComponentes().get("Imagenes/back.png", Texture.class));
        volverDibujable = new TextureRegionDrawable(volverRegion);
        botonVolver = new ImageButton(volverDibujable);

        // Ahora, pongo todos los elementos en una tabla, lo cual me permitira mejorar las distribuciones de estos
        organizadorConf.defaults().pad(20.0f);
        organizadorConf.add(tituloJuego).colspan(2).top().center();
        organizadorConf.row();
        organizadorConf.add(etiquetaConf).colspan(2).top().center();
        organizadorConf.row();
        organizadorConf.add(etiquetaNivelDificultad);
        organizadorConf.add(selectorDificultad).width(juego.ANCHO_VIRTUAL/3).height(juego.ALTO_VIRTUAL/12);
        organizadorConf.row();
        organizadorConf.add(etiquetaConexionServer);
        organizadorConf.add(campoConexion).width(juego.ANCHO_VIRTUAL/3).height(juego.ALTO_VIRTUAL/12);
        organizadorConf.row();
        organizadorConf.add(etiquetaPuertoComunicacion);
        organizadorConf.add(campoPuerto).width(juego.ANCHO_VIRTUAL/3).height(juego.ALTO_VIRTUAL/12);
        organizadorConf.row();
        organizadorConf.add(etiquetaUsuario);
        organizadorConf.add(campoUsuario).width(juego.ANCHO_VIRTUAL/3).height(juego.ALTO_VIRTUAL/12);
        organizadorConf.row();
        organizadorConf.add(botonVolver).width(juego.ANCHO_VIRTUAL/14).height(juego.ALTO_VIRTUAL/8).colspan(2).bottom().left().expandX();

        botonVolver.addListener(new ClickListener(){
            @Override
            public  void clicked (InputEvent event, float x, float y)
            {
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        // Ya con todos los elementos organizados procedemos a agregarlos al escenario, que es al fin y al cabo quien dirige la pantalla
        escenarioConf.addActor(organizadorConf);

        camara = new OrthographicCamera(); // Despues, se inicializa la camara
        camara.setToOrtho(false); // Con esto se le indica a la camara que el origen de coordenadas (0,0) esta en la esquina inferior izquierda de la pantalla del dispositivo
        camara.position.set(juego.ANCHO_VIRTUAL / 2, juego.ALTO_VIRTUAL / 2, 0); // Ahora, con esta instruccion se le indica a la camara y al render() que el origen de coordenadas (0,0) tambien es la esquina inferior izquierda. Esto es importante porque sino esta entonces cualquier cosa que se dibuje comenzara en el centro de la pantalla.
        vistaConf= new FitViewport(juego.ANCHO_VIRTUAL, juego.ALTO_VIRTUAL, camara); // Por ultimo, se inicializa la vista con unas dimensiones tentativas de 1280x720.
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
        juego.dibujadorPantalla.draw(fondoPantallaConf, 0, 0,juego.ANCHO_VIRTUAL, juego.ALTO_VIRTUAL); // Aqui se dibuja el fondo y se ajusta a las dimensiones dadas
        juego.dibujadorPantalla.end();

        // A continuacion se actualiza el escenario del menu configuraciones
        escenarioConf.act(delta);
        escenarioConf.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        escenarioConf.getViewport().update(width, height, true); // Con esta instruccion se ajusta el tamaño del escenario
        vistaConf.update(width, height); // Esta por su parte permite actualizar las dimensiones de la camara y asi se ajuste el juego a la pantalla del dispositivo movil
        camara.update(); // De igual modo se actualiza la camara
    }

    @Override
    public void show()
    {
        // En el momento que la pantalla se muestra al usuario la siguiente instruccion le da al escenario el control de los eventos que haga el usuario (asi podremos detectar cuando toca la pantalla)
        Gdx.input.setInputProcessor(escenarioConf);

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
        fondoPantallaConf.dispose();
        escenarioConf.dispose();
        skinConf.dispose();
    }

    // Los siguientes metodos simplemente no se implementan
    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
