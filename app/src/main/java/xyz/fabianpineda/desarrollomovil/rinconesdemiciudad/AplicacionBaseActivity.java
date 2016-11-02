package xyz.fabianpineda.desarrollomovil.rinconesdemiciudad;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

public abstract class AplicacionBaseActivity extends AppCompatActivity {
    private static final String ARCHIVO_PREFERENCIA_APLICACION = ExplorarRinconesActivity.class.getSimpleName();
    private static final String PREFERENCIA_GUID_APLICACION = "GUID_APLICACION";

    private static final String DIRECTORIO_FOTOS_USUARIO = "fotos_usuario";

    String GUIDAplicacion;
    SharedPreferences configuracion;

    File directorioArchivosAplicacion;
    File directorioFotosUsuario;

    static void notificar(Context contexto, String mensaje) {
        Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT).show();
    }

    static void notificar(Context contexto, int recursoStringMensaje) {
        AplicacionBaseActivity.notificar(contexto, contexto.getString(recursoStringMensaje));
    }

    void notificar(String mensaje) {
        AplicacionBaseActivity.notificar(this, mensaje);
    }

    void notificar(int recursoStringMensaje) {
        AplicacionBaseActivity.notificar(this, recursoStringMensaje);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.opcion_explorar_rincones:
                notificar("DEBUG: Presionado \"Explorar Rincones\".");
                return true;
            case R.id.opcion_explorar_mis_rincones:
                notificar("DEBUG Presionado \"Mis Rincones\".");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_explorar_rincones, menu);
        return true;
    }

    void inicializarMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    abstract void inicializarUI();

    private final void configurarRutas() {
        directorioArchivosAplicacion = getFilesDir();
        directorioFotosUsuario = getDir(DIRECTORIO_FOTOS_USUARIO, MODE_PRIVATE);
    };

    void cargarConfiguracion() {
        configuracion = getSharedPreferences(ARCHIVO_PREFERENCIA_APLICACION, MODE_PRIVATE);

        GUIDAplicacion = configuracion.getString(PREFERENCIA_GUID_APLICACION, null);
        if (GUIDAplicacion == null) {
            GUIDAplicacion = UUID.randomUUID().toString();
            configuracion.edit().putString(PREFERENCIA_GUID_APLICACION, GUIDAplicacion).commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cargarConfiguracion();
        configurarRutas();
        inicializarUI();
        inicializarMenu();
    }
}
