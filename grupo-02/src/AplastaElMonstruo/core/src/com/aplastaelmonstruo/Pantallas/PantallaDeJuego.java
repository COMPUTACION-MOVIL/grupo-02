package com.aplastaelmonstruo.Pantallas;

import com.aplastaelmonstruo.MainGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaDeJuego implements Screen
{
    private MainGame juego; // Se crea un variable privada que nos permitira obtener/almecenar siempre la informacion del juego.

    private Texture fondoJuego;

    private OrthographicCamera camara; // Se crea una camara que nos permitira enfocar solo el area del juego que nos interesa mostrar al jugador
    private Viewport vistaJuego; // Y creamos un puerto o marco de vista el cual no es más que un adapatador de la imagen del juego con el tamaño del Display del dispostivo que ejecuta el juego

    public PantallaDeJuego(final MainGame juego, int modo) // Constructor de la pantalla de juego
    {
        this.juego = juego; // En primer lugar, obtenemos el juego como tal

        fondoJuego= new Texture("Imagenes/background05.jpg");

        camara = new OrthographicCamera(); // Se inicializa la camara
        vistaJuego = new FitViewport(1600, 900, camara); // Y se inicialza la vista con unas dimensiones iniciales. De todos modos esto cambia luego.
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta)
    {
        // Limpiamos la pantalla antes de dibujar cualquier cosa
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Ahora, con la siguiente instruccion se procede a decirle al juego en donde debe apuntar la camara segun la vista que se establecio
        camara.update();
        juego.dibujadorPantalla.setProjectionMatrix(camara.combined);

        // Finalmente, procedemos a dibujar el menu
        juego.dibujadorPantalla.begin();
        juego.dibujadorPantalla.draw(fondoJuego,0,0);
        juego.dibujadorPantalla.end();
    }

    @Override
    public void resize(int width, int height)
    {
        vistaJuego.update(width, height); // Esta instruccion permite actualizar las dimensiones de la camara y asi se ajuste el juego a la pantalla del dispositivo movil
        camara.update(); // De igual modo se actualiza la camara
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose()
    {
        fondoJuego.dispose();
    }
}
