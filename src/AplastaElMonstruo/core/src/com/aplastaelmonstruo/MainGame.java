package com.aplastaelmonstruo;

import com.aplastaelmonstruo.Pantallas.PantallaCarga;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Como esta va a ser la clase Juego, obviamente heredara de la clase Game y como tal sera la intermediara o manejadora de todas las pantallas y recursos del juego
public class MainGame extends Game
{
	// Para empezar manejaremos unas dimensiones unicas para el juego y con ayuda de glViewport() ajustaremos estas dimensiones al tamaño de la pantalla del dispositivo
	public int ANCHO_VIRTUAL = 1280;
	public int ALTO_VIRTUAL = 720;

	public SpriteBatch dibujadorPantalla; // Luego, este objeto sera el encargado de dibujar todas los elementos del juego en la pantalla.

	private AssetManager adminComponentes; // Por otra parte, este sera el el objeto que cargara y manejara los elementos del juego (Imagenes/Sonido/Musica).

	// El siguiente metodo es como el constructor del juego, en donde debemos inicializar lo que necesitemos y abrir la pantalla de carga.
	@Override
	public void create()
	{
		dibujadorPantalla = new SpriteBatch(); // Se inicializa el dibujador

		adminComponentes = new AssetManager(); // Se inicializa el controlador de elementos
		// Cargamos las imagenes
		adminComponentes.load("Imagenes/backgroundMenus.png", Texture.class);
		adminComponentes.load("Imagenes/background01.jpg", Texture.class);
		adminComponentes.load("Imagenes/background02.jpg", Texture.class);
		adminComponentes.load("Imagenes/background03.jpg", Texture.class);
		adminComponentes.load("Imagenes/background04.jpg", Texture.class);
		adminComponentes.load("Imagenes/background05.jpg", Texture.class);
		adminComponentes.load("Imagenes/background06.jpg", Texture.class);
		adminComponentes.load("Imagenes/background07.jpg", Texture.class);
		adminComponentes.load("Imagenes/background08.jpg", Texture.class);
		adminComponentes.load("Imagenes/background09.jpg", Texture.class);
		adminComponentes.load("Imagenes/background10.jpg", Texture.class);
		adminComponentes.load("Imagenes/enemy01-A.png", Texture.class);
		adminComponentes.load("Imagenes/enemy01-B.png", Texture.class);
		adminComponentes.load("Imagenes/enemy02-A.png", Texture.class);
		adminComponentes.load("Imagenes/enemy02-B.png", Texture.class);
		adminComponentes.load("Imagenes/enemy03-A.png", Texture.class);
		adminComponentes.load("Imagenes/enemy03-B.png", Texture.class);
		adminComponentes.load("Imagenes/enemy04-A.png", Texture.class);
		adminComponentes.load("Imagenes/enemy04-B.png", Texture.class);
		adminComponentes.load("Imagenes/enemy05-A.png", Texture.class);
		adminComponentes.load("Imagenes/enemy05-B.png", Texture.class);
		adminComponentes.load("Imagenes/gameover.png", Texture.class);
		adminComponentes.load("Imagenes/goodjob.png", Texture.class);
		adminComponentes.load("Imagenes/title.png", Texture.class);
		adminComponentes.load("Imagenes/back.png", Texture.class);
		// Cargamos las animaciones
		// -------
		// Cargamos los efectos de sonido
		adminComponentes.load("Audios/finalHit.ogg", Sound.class);
		adminComponentes.load("Audios/glass-break.ogg", Sound.class);
		adminComponentes.load("Audios/hit1.ogg", Sound.class);
		adminComponentes.load("Audios/hit2.ogg", Sound.class);
		adminComponentes.load("Audios/hit3.ogg", Sound.class);
		adminComponentes.load("Audios/menu-click.ogg", Sound.class);
		adminComponentes.load("Audios/sad-trombone.ogg", Sound.class);
		adminComponentes.load("Audios/victory.ogg", Sound.class);
		// Y cargamos la musicas de fondo
		adminComponentes.load("Audios/bgmusic01.ogg", Music.class);
		adminComponentes.load("Audios/bgmusic02.ogg", Music.class);
		adminComponentes.load("Audios/bgmusic03.ogg", Music.class);
		adminComponentes.load("Audios/bgmusic04.ogg", Music.class);
		adminComponentes.load("Audios/bgmusic05.ogg", Music.class);
		adminComponentes.load("Audios/bgmusic06.ogg", Music.class);
		adminComponentes.load("Audios/bgmusic07.ogg", Music.class);
		adminComponentes.load("Audios/bgmusic08.ogg", Music.class);
		adminComponentes.load("Audios/intro.ogg", Music.class);

		// Ahora llamamos la pantalla de carga
		setScreen(new PantallaCarga(this));
	}

	@Override
	public void render ()
	{
		super.render(); // La idea de esta instruccion es delegarle la responsabilidad del dibujo a la pantalla que actualmente se esta mostrando al usuario
	}

	// Metodo para obtener facilmente el administrador de componentes
	public AssetManager getAdminComponentes()
	{
		return adminComponentes;
	}
}