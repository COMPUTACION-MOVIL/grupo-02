package com.aplastaelmonstruo.Pantallas;

import com.aplastaelmonstruo.MainGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

// Clase encargada de la pantalla que nos indicara el progreso de carga de los elementos necesarios del juego antes de comenzar a jugar
public class PantallaCarga implements Screen
{
    // Se crea un variable privada que nos permitira obtener/almecenar siempre la informacion del juego.
    private MainGame juego;

    // A continuacion, creamos algunos objetos para dibujar como tal la pantalla
    Stage escenarioCarga;
    Skin skinCarga;
    Label etiquetaProgesoCarga;

    public PantallaCarga(final MainGame juego) // Constructor de la pantalla de carga
    {
        this.juego = juego; // Aqui solo obtenemos el juego y lo guardamos
        // Se inicializan y dibujan los elementos de la pantalla de carga
        escenarioCarga = new Stage(new FitViewport(MainGame.ANCHO_VIRTUAL, MainGame.ALTO_VIRTUAL));
        skinCarga = new Skin(Gdx.files.internal("Skin/uiskin.json"));
        etiquetaProgesoCarga = new Label("Cargando...", skinCarga);
        etiquetaProgesoCarga.setPosition(((MainGame.ANCHO_VIRTUAL/2)-(etiquetaProgesoCarga.getWidth()/2)), ((MainGame.ALTO_VIRTUAL/2)-(etiquetaProgesoCarga.getHeight()/2)));
        escenarioCarga.addActor(etiquetaProgesoCarga);
    }

    @Override
    public void render(float delta)
    {
        // Limpiamos la pantalla antes de dibujar cualquier cosa
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(juego.getAdminComponentes().update()) // Si ya terminamos de cargar todos los elementos del juego entonces
        {
            juego.setScreen(new PantallaMenu(juego)); // Se pasa a la pantalla de menu
        }
        else // Si aun faltan elementos por cargar entonces
        {
            int progreso = (int) (juego.getAdminComponentes().getProgress() * 100); // Se calcula el progreso. Se hace como un entero para no mostrar decimales
            etiquetaProgesoCarga.setText("Cargando..." + progreso + "%"); // Se actualiza la etiqueta de progreso
        }

        // A continuacion se actualiza el escenario de carga
        escenarioCarga.act();
        escenarioCarga.draw();
    }

    @Override
    public void dispose()
    {
        // Cuando termina esta pantalla pues liberamos los recursos que ya no necesitamos
        escenarioCarga.dispose();
        skinCarga.dispose();
    }

    // Los siguientes metodos simplemente no se implementan
    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
