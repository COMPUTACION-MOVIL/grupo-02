package com.aplastaelmonstruo.Pantallas;

import com.aplastaelmonstruo.Escenarios.Historia;
import com.aplastaelmonstruo.MainGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

// Pantalla para seleccionar el nivel que se quiere jugar en el modo historia
public class PantallaSelectorNivel implements Screen
{
    private MainGame juego; // Se crea una variable privada que nos permitira obtener/almecenar siempre la informacion del juego.

    private Texture fondoPantallaSelector; // Se crea una variable para la imagen que sera de fondo de esta pantalla de seleccion de niveles
    private OrthographicCamera camara; // Se crea una camara que nos permitira enfocar solo el area del juego que nos interesa mostrar al jugador
    private FitViewport vistaSelector; // Se crea un puerto o marco de vista el cual no es más que un adapatador de la imagen del juego con el tamaño del Display del dispostivo que ejecuta el juego

    // Tambien se crean todos los elementos basicos para armar esta pantalla de seleccion de niveles
    Stage escenarioSelector;
    Table organizadorSelector;
    Skin skinSelector;
    TextButton btnNivel1;
    TextButton btnNivel2;
    TextButton btnNivel3;
    TextButton btnNivel4;
    TextButton btnNivel5;
    TextButton btnNivel6;
    TextButton btnNivel7;
    TextButton btnNivel8;
    TextButton btnNivel9;
    TextButton btnNivel10;
    TextureRegion volverRegion;
    TextureRegionDrawable volverDibujable;
    ImageButton botonVolver;

    // Y se crea una variable que nos permitira manipular un archivo que guarda la informacion del modo campaña
    Preferences prefs;

    public PantallaSelectorNivel(final MainGame juego) // Constructor de la pantalla seleccion de niveles
    {
        this.juego = juego; // Aqui solo obtenemos el juego y lo guardamos

        fondoPantallaSelector = juego.getAdminComponentes().get("Imagenes/backgroundMenus.png", Texture.class); // Ahora, cargamos el fondo para esta pantalla

        // Se inicializan los elementos basicos de la pantalla de seleccion de niveles
        escenarioSelector = new Stage();
        organizadorSelector = new Table();
        organizadorSelector.setFillParent(true);

        // Cargo el estilo basico de los botones
        skinSelector = new Skin(Gdx.files.internal("Skin/uiskin.json"));

        // Ahora, se inicializa la conexion con el archivo de informacion del modo campaña
        prefs = Gdx.app.getPreferences("InfoHistoria");

        // Luegp, creo los botones para cada uno de los niveles, junto con las acciones que cada boton debe realizar
        btnNivel1 = new TextButton("1", skinSelector);
        btnNivel1.addListener(new ClickListener()
        {
            @Override
            public  void clicked (InputEvent event, float x, float y)
            {
                if (juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).isPlaying())
                {
                    juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).stop();
                }

                juego.setScreen(new Historia(juego, 1));
            }
        });

        int aux = prefs.getInteger("N1", 0);
        if (aux == 0)
        {
            btnNivel2 = new TextButton("---", skinSelector);
        }
        else
        {
            btnNivel2 = new TextButton("2", skinSelector);
            btnNivel2.addListener(new ClickListener()
            {
                @Override
                public  void clicked (InputEvent event, float x, float y)
                {
                    if (juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).isPlaying())
                    {
                        juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).stop();
                    }

                    juego.setScreen(new Historia(juego, 2));
                }
            });
        }

        aux = prefs.getInteger("N2", 0);
        if (aux == 0)
        {
            btnNivel3 = new TextButton("---", skinSelector);
        }
        else
        {
            btnNivel3 = new TextButton("3", skinSelector);
            btnNivel3.addListener(new ClickListener()
            {
                @Override
                public  void clicked (InputEvent event, float x, float y)
                {
                    if (juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).isPlaying())
                    {
                        juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).stop();
                    }

                    juego.setScreen(new Historia(juego, 3));
                }
            });
        }

        aux = prefs.getInteger("N3", 0);
        if (aux == 0)
        {
            btnNivel4 = new TextButton("---", skinSelector);
        }
        else
        {
            btnNivel4 = new TextButton("4", skinSelector);
            btnNivel4.addListener(new ClickListener()
            {
                @Override
                public  void clicked (InputEvent event, float x, float y)
                {
                    if (juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).isPlaying())
                    {
                        juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).stop();
                    }

                    juego.setScreen(new Historia(juego, 4));
                }
            });
        }

        aux = prefs.getInteger("N4", 0);
        if (aux == 0)
        {
            btnNivel5 = new TextButton("---", skinSelector);
        }
        else
        {
            btnNivel5 = new TextButton("5", skinSelector);
            btnNivel5.addListener(new ClickListener()
            {
                @Override
                public  void clicked (InputEvent event, float x, float y)
                {
                    if (juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).isPlaying())
                    {
                        juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).stop();
                    }

                    juego.setScreen(new Historia(juego, 5));
                }
            });
        }

        aux = prefs.getInteger("N5", 0);
        if (aux == 0)
        {
            btnNivel6 = new TextButton("---", skinSelector);
        }
        else
        {
            btnNivel6 = new TextButton("6", skinSelector);
            btnNivel6.addListener(new ClickListener()
            {
                @Override
                public  void clicked (InputEvent event, float x, float y)
                {
                    if (juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).isPlaying())
                    {
                        juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).stop();
                    }

                    juego.setScreen(new Historia(juego, 6));
                }
            });
        }

        aux = prefs.getInteger("N6", 0);
        if (aux == 0)
        {
            btnNivel7 = new TextButton("---", skinSelector);
        }
        else
        {
            btnNivel7 = new TextButton("7", skinSelector);
            btnNivel7.addListener(new ClickListener()
            {
                @Override
                public  void clicked (InputEvent event, float x, float y)
                {
                    if (juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).isPlaying())
                    {
                        juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).stop();
                    }

                    juego.setScreen(new Historia(juego, 7));
                }
            });
        }

        aux = prefs.getInteger("N7", 0);
        if (aux == 0)
        {
            btnNivel8 = new TextButton("---", skinSelector);
        }
        else
        {
            btnNivel8 = new TextButton("8", skinSelector);
            btnNivel8.addListener(new ClickListener()
            {
                @Override
                public  void clicked (InputEvent event, float x, float y)
                {
                    if (juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).isPlaying())
                    {
                        juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).stop();
                    }

                    juego.setScreen(new Historia(juego, 8));
                }
            });
        }

        aux = prefs.getInteger("N8", 0);
        if (aux == 0)
        {
            btnNivel9 = new TextButton("---", skinSelector);
        }
        else
        {
            btnNivel9 = new TextButton("9", skinSelector);
            btnNivel9.addListener(new ClickListener()
            {
                @Override
                public  void clicked (InputEvent event, float x, float y)
                {
                    if (juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).isPlaying())
                    {
                        juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).stop();
                    }

                    juego.setScreen(new Historia(juego, 9));
                }
            });
        }

        aux = prefs.getInteger("N9", 0);
        if (aux == 0)
        {
            btnNivel10 = new TextButton("---", skinSelector);
        }
        else
        {
            btnNivel10 = new TextButton("10", skinSelector);
            btnNivel10.addListener(new ClickListener()
            {
                @Override
                public  void clicked (InputEvent event, float x, float y)
                {
                    if (juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).isPlaying())
                    {
                        juego.getAdminComponentes().get("Audios/intro.ogg", Music.class).stop();
                    }

                    juego.setScreen(new Historia(juego, 10));
                }
            });
        }

        btnNivel1.getLabel().setFontScale(2f,2f);
        btnNivel2.getLabel().setFontScale(2f,2f);
        btnNivel3.getLabel().setFontScale(2f,2f);
        btnNivel4.getLabel().setFontScale(2f,2f);
        btnNivel5.getLabel().setFontScale(2f,2f);
        btnNivel6.getLabel().setFontScale(2f,2f);
        btnNivel7.getLabel().setFontScale(2f,2f);
        btnNivel8.getLabel().setFontScale(2f,2f);
        btnNivel9.getLabel().setFontScale(2f,2f);
        btnNivel10.getLabel().setFontScale(2f,2f);

        volverRegion = new TextureRegion(juego.getAdminComponentes().get("Imagenes/back.png", Texture.class));
        volverDibujable = new TextureRegionDrawable(volverRegion);
        botonVolver = new ImageButton(volverDibujable);
        botonVolver.addListener(new ClickListener()
        {
            @Override
            public  void clicked (InputEvent event, float x, float y)
            {
                juego.setScreen(new PantallaModo(juego));
            }
        });

        // Ahora, procederemos a oraginzar todos los botones en una tabla lo cual me permitira mejorar las distribucion de todas las cosas
        organizadorSelector.defaults().pad(20.0f).fill();
        organizadorSelector.add(btnNivel1).expand().fill();
        organizadorSelector.add(btnNivel2).expand().fill();
        organizadorSelector.add(btnNivel3).expand().fill();
        organizadorSelector.add(btnNivel4).expand().fill();
        organizadorSelector.add(btnNivel5).expand().fill();
        organizadorSelector.row();
        organizadorSelector.add(btnNivel6).expand().fill();
        organizadorSelector.add(btnNivel7).expand().fill();
        organizadorSelector.add(btnNivel8).expand().fill();
        organizadorSelector.add(btnNivel9).expand().fill();
        organizadorSelector.add(btnNivel10).expand().fill();
        organizadorSelector.row();
        organizadorSelector.add(botonVolver).width(MainGame.ANCHO_VIRTUAL/14).height(MainGame.ALTO_VIRTUAL/8).bottom().left().expandX().colspan(5);

        // Ya con todos los elementos organizados procedemos a agregarlos al escenario, que es al fin y al cabo quien dirige la pantalla
        escenarioSelector.addActor(organizadorSelector);

        camara = new OrthographicCamera(); // Despues, se inicializa la camara
        camara.setToOrtho(false); // Con esto se le indica a la camara que el origen de coordenadas (0,0) esta en la esquina inferior izquierda de la pantalla del dispositivo
        camara.position.set(MainGame.ANCHO_VIRTUAL / 2, MainGame.ALTO_VIRTUAL / 2, 0); // Ahora, con esta instruccion se le indica a la camara y al render() que el origen de coordenadas (0,0) tambien es la esquina inferior izquierda. Esto es importante porque sino esta entonces cualquier cosa que se dibuje comenzara en el centro de la pantalla.
        vistaSelector = new FitViewport(MainGame.ANCHO_VIRTUAL, MainGame.ALTO_VIRTUAL, camara); // Por ultimo, se inicializa la vista con unas dimensiones tentativas de 1280x720.
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
        juego.dibujadorPantalla.draw(fondoPantallaSelector, 0, 0, MainGame.ANCHO_VIRTUAL, MainGame.ALTO_VIRTUAL); // Aqui se dibuja el fondo y se ajusta a las dimensiones dadas
        juego.dibujadorPantalla.end();

        // A continuacion se actualiza el escenario del menu
        escenarioSelector.act(delta);
        escenarioSelector.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        escenarioSelector.getViewport().update(width, height, true); // Con esta instruccion se ajusta el tamaño del escenario
        vistaSelector.update(width, height); // Esta por su parte permite actualizar las dimensiones de la camara y asi se ajuste el juego a la pantalla del dispositivo movil
        camara.update(); // De igual modo se actualiza la camara
    }

    @Override
    public void show()
    {
        // En el momento que la pantalla se muestra al usuario la siguiente instruccion le da al escenario el control de los eventos que haga el usuario (asi podremos detectar cuando toca la pantalla)
        Gdx.input.setInputProcessor(escenarioSelector);

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
        fondoPantallaSelector.dispose();
        escenarioSelector.dispose();
        skinSelector.dispose();
    }

    // Los siguientes metodos simplemente no se implementan
    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
