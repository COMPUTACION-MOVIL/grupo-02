package com.aplastaelmonstruo.Actores;

import com.aplastaelmonstruo.MainGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

// Clase encargada del comportamiento y logica de los enemigos del juego
public class Monstruo
{
    // En primer lugar se crean todas las variables relacionadas con el actor enemigo:
    private Texture imagenMonstruo; // Imagen que representa el monstruo
    private Texture imagenMonstruoGolpeado; // Imagen que representa el monstruo despues de un golpe
    public Sprite spriteMonstruo; // Dibujador del monstruo y sus accciones
    public float X, Y; // Variables que guardan la posicion del actor
    private int vida, puntaje, tipo; // Variables o caracteristicas asociadas con el actor
    private float velocidad; // Variable que definira la velocidad de movimiento del actor
    private float dx, dy; // Estas variables definiran el diferencial de velocidad del actor
    private boolean inmune; // Variable que le permitira recuperarse al monstruo despues de un golpe
    private float contaTiempo; // Variable que define cuanto tiempo el monstruo es inmune a golpes

    // Luego, se crean unas constantes para poder generar cualquier tipo de monstruo facilmente
    public static final int CACHOS = 1;
    public static final int PINCHOS = 2;
    public static final int AMEBA = 3;
    public static final int PELUZA = 4;
    public static final int ZOMBIE = 5;

    // Constructor de la clase
    public Monstruo(float x, float y, int tipoMonstruo)
    {
        // En primer lugar se obtienen las coordenadas iniciales del monstruo y su tipo
        this.X = x;
        this.Y = y;
        this.tipo = tipoMonstruo;

        // Una vez se selecciona el tipo se procede a cargar su imagen y a establecer sus caracteristicas como actor (estas ultimas se definieron arbitrariamente)
        switch (tipo)
        {
            case (CACHOS):
                imagenMonstruo = new Texture(Gdx.files.internal("Imagenes/enemy01-A.png"));
                imagenMonstruoGolpeado = new Texture(Gdx.files.internal("Imagenes/enemy01-B.png"));
                vida = 7;
                puntaje = 100;
                velocidad = MathUtils.random(70, 100);
                break;
            case (PINCHOS):
                imagenMonstruo = new Texture(Gdx.files.internal("Imagenes/enemy02-A.png"));
                imagenMonstruoGolpeado = new Texture(Gdx.files.internal("Imagenes/enemy02-B.png"));
                vida = 6;
                puntaje = 80;
                velocidad = MathUtils.random(70, 80);
                break;
            case (AMEBA):
                imagenMonstruo = new Texture(Gdx.files.internal("Imagenes/enemy03-A.png"));
                imagenMonstruoGolpeado = new Texture(Gdx.files.internal("Imagenes/enemy03-B.png"));
                vida = 5;
                puntaje = 70;
                velocidad = MathUtils.random(50, 70);
                break;
            case (PELUZA):
                imagenMonstruo = new Texture(Gdx.files.internal("Imagenes/enemy04-A.png"));
                imagenMonstruoGolpeado = new Texture(Gdx.files.internal("Imagenes/enemy04-B.png"));
                vida = 4;
                puntaje = 50;
                velocidad = MathUtils.random(40, 60);
                break;
            case (ZOMBIE):
                imagenMonstruo = new Texture(Gdx.files.internal("Imagenes/enemy05-A.png"));
                imagenMonstruoGolpeado = new Texture(Gdx.files.internal("Imagenes/enemy05-B.png"));
                vida = 2;
                puntaje = 30;
                velocidad = MathUtils.random(20, 30);
                break;
        }

        // A continuacion, se genere aletoriamente una direccion inicial de movimiento la cual estara definida por dos diferencias (dx y dy) de distancia
        generarDiferenciales();

        // Por ultimo, llevo la imagen del monstruo a un sprite el cual es mas comodo de manejar
        spriteMonstruo = new Sprite(imagenMonstruo);
        // De igual modo, le indico a este enemigo donde debe aparecer
        spriteMonstruo.setPosition(X,Y);
        // Y que no es inicialmente inmune a golpes
        inmune = false;
    }

    // Metodo que aletoriamente decide el sentido del movimiento del actor
    public void generarDiferenciales()
    {
        // La siguiente instruccion no hace más que calcular un angulo entre 0 y 2pi
        float direccion = MathUtils.random(2 * 3.1415f);

        // Una vez que tenemos el angulo de la direccion, descomponemos dicho angulo en x y en y, para luego multplicarlo por la velocidad y asi obtener diferenciales de distancia en amabas coordenadas
        dx = MathUtils.cos(direccion) * velocidad;
        dy = MathUtils.sin(direccion) * velocidad;
    }

    // Metodo que controla el movimiento del monstruo
    public void actualizarPosicion(float delta)
    {
        // Si el monstruo es inmune a golpes entonces
        if (inmune)
        {
            // Reduzco el tiempo de inmunidad en un pequeño valor
            contaTiempo = contaTiempo - delta;

            // Si el tiempo de inmunidad ha llegado a cero entonces
            if (contaTiempo <= 0)
            {
                // Indico que ya dejo de ser inmune a golpes y vuelve a su apariencia normal
                inmune = false;
                spriteMonstruo.setTexture(imagenMonstruo);
            }
            else // sino
            {
                return; // No se permite calcular el siguiente paso o movimiento del monstruo
            }
        }

        // Una vez que el monstruo vuelve a ser golpeable entonces, calculamos las nuevas coordenadas X y Y del mosntruo. Esto se hace simplemente sumando la coordenadas actuales con un diferencial de distancia
        X = X + (dx*delta);
        Y = Y + (dy*delta);

        // En caso que las nuevas coordenas del actor se salgan o sobrepasen las dimensiones de la pantalla, entonces se hace un reposicionamiento
        if (X < 0){X = MainGame.ANCHO_VIRTUAL;}
        if (X > MainGame.ANCHO_VIRTUAL){X = 0;}
        if (Y < 0){Y = MainGame.ALTO_VIRTUAL;}
        if (Y > MainGame.ALTO_VIRTUAL){Y = 0;}

        // Ya con X y Y, se escriben estas en el sprite del actor
        spriteMonstruo.setPosition(X,Y);
    }

    // Metodos para obtener las caracteristicas basicas del monstruo
    public int getPuntaje() { return puntaje; }

    public int getVida(){ return vida; }

    public int getTipo() { return tipo; }

    // Metodo que revisa si el monstruo ha sido golpeado
    public boolean impactado(float toqueX, float toqueY)
    {
        // Si las coordenas de donde toco el usuario estan dentro del area del monstruo Y este no es inmune entonces
        if ((toqueX >= this.X) && (toqueX <= this.X + imagenMonstruo.getWidth()) && (toqueY >= this.Y) && (toqueY <= this.X + imagenMonstruo.getHeight()) && (!inmune))
        {
            // Se reduce una unidad de vida del actor
            vida  = vida - 1;

            // Se calcula un nuevo sentido de movimiento que permita escapar al monstruo, como tambien se le aumenta la velocidad
            generarDiferenciales();
            velocidad = velocidad + MathUtils.random(70, 100);

            // El mismo se volvera inmune por una pequeña fraccion de tiempo y su apariencia cambia
            inmune = true;
            contaTiempo = 0.8f;
            spriteMonstruo.setTexture(imagenMonstruoGolpeado);

            // Por utlimo, se retorna verdadero
            return true;
        }

        return false; // Si no se logro contacto entonces se retorna falso
    }

    // Metodo que indica el estado de vida del monstruo
    public boolean haMuerto()
    {
        return (vida == 0) && (!inmune);
    }

    // Metodo que se encarga de dibujar el mosntruo
    public void dibujarMonstruo(SpriteBatch dibujador)
    {
        spriteMonstruo.draw(dibujador);
    }

}
