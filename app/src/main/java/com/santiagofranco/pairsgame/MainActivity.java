package com.santiagofranco.pairsgame;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linearVertical;
    private Table table;
    private int[][] tablero;
    private int idReversoCarta;
    private ImageView ultimaImage;
    private Carta ultimaCarta;
    private boolean segundaCarta;
    private int contadorDeAciertos;
    private ImageView ivPulsada;
    private boolean tapando = false; // Variable que hace de bandera para que el hilo principal no se ejecute mientras el HiloTapar esta esperando
    private int intentos = 0;

    /**
     * En android no se hereda de Thread sino de AsyncTask para generar nuevos hilos
     * Lo de integer, integer, integer Paco no lo explico, dijo que pusieramos eso que ya lo explicara
     * El metodo doInBackground se tiene que implementar obligatoriamente, si en éste metodo se intenta
     *   pintar algo en pantalla generara errores (incluido los Toast) por eso lo de pintar se hace en
     *   otros metodos segun convenga, en este caso se pinta en pantalla el reverso de las cartas en onPostExecute
     *   que se ejecuta cuando termina doInBackground
     */
    private class HiloTapar extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            SystemClock.sleep(1000);
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            ponerReversoACartasPulsadas();
            tapando = false;
        }
    }

    /**
     * En clase solo vimos el HiloTapar, este otro lo necesitaba para mostrar los mensajes
     * y que se reiniciar el juego despues de ellos, por eso uso onPreExecute
     * iniciarJuego se llama en ese metodo por que pinta en pantalla
     */
    private class HiloMensajes extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mostrarMensajesFinales();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            SystemClock.sleep(6500);
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            iniciarJuego();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearVertical = (LinearLayout) findViewById(R.id.linearVertical);
        table = new Table();
        idReversoCarta = getResources().getIdentifier("blanco", "drawable", getPackageName());
        iniciarJuego();


    }

    private void iniciarJuego() {
        table.initMe();
        tablero = table.getTable();
        segundaCarta = false;
        ultimaCarta = null;
        ultimaImage = null;
        contadorDeAciertos = 0;
        setTableroConImagenes();
    }

    private void setTableroConImagenes() {
        for (int i = 0; i < linearVertical.getChildCount(); i++) {
            LinearLayout linearActual = (LinearLayout) linearVertical.getChildAt(i);
            for (int j = 0; j < linearActual.getChildCount(); j++) {
                int id = getResources().getIdentifier("p" + tablero[i][j], "drawable", getPackageName());
                ImageView iv = (ImageView) linearActual.getChildAt(j);
                setImageViewActual(i, j, id, iv);

            }
        }

    }

    private void setImageViewActual(int i, int j, int id, ImageView iv) {
        iv.setBackgroundResource(id);
        iv.setImageResource(idReversoCarta);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setOnClickListener(this);
        iv.setTag(new Carta(i, j, tablero[i][j]));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            iniciarJuego();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (tapando) return;

        ivPulsada = (ImageView) v;
        Carta carta = (Carta) ivPulsada.getTag();
        ivPulsada.setImageResource(-1);

        if (segundaCarta) {

            intentos++;

            if (falloDeCarta(carta)) {
                esperarParaTaparCartas();
                segundaCarta = false;

            } else {
                quitarListenerACartasAcertadas(ivPulsada);
                segundaCarta = false;
                preguntarSiHaAcabado();
            }

        } else {
            ultimaCarta = carta;
            ultimaImage = ivPulsada;
            segundaCarta = true;

        }


    }

    private boolean falloDeCarta(Carta carta) {
        return !igualValor(carta, ultimaCarta) || igualCoordenanda(carta, ultimaCarta);
    }

    private void esperarParaTaparCartas() {
        tapando = true;
        new HiloTapar().execute();
    }

    private boolean igualValor(Carta carta, Carta ultimaCarta) {
        return carta.getValue() == ultimaCarta.getValue();
    }

    private boolean igualCoordenanda(Carta carta, Carta ultimaCarta) {
        return carta.getPosX() == ultimaCarta.getPosX() && carta.getPosY() == ultimaCarta.getPosY();
    }

    private void ponerReversoACartasPulsadas() {
        ultimaImage.setImageResource(idReversoCarta);
        ivPulsada.setImageResource(idReversoCarta);

    }

    private void quitarListenerACartasAcertadas(ImageView iv) {
        ultimaImage.setOnClickListener(null);
        iv.setOnClickListener(null);
    }

    private void preguntarSiHaAcabado() {

        if (++contadorDeAciertos == 10) {
            new HiloMensajes().execute();
        }

    }

    private void mostrarMensajesFinales() {
        Toast.makeText(this, "Has terminado el juego, has gastado " + intentos + " intentos.", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "El juego se va a reiniciar", Toast.LENGTH_LONG).show();
    }
}
