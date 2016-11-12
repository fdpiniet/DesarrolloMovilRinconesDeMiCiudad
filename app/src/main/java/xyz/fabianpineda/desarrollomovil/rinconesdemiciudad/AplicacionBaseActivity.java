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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public abstract class AplicacionBaseActivity extends AppCompatActivity {
    private static final String ARCHIVO_PREFERENCIA_APLICACION = ExplorarRinconesActivity.class.getSimpleName();
    private static final String PREFERENCIA_GUID_APLICACION = "GUID_APLICACION";

    static final String PLANTILLA_NOMBRE_ARCHIVO_FOTO = "%s.%s";
    static final String NOMBRE_ARCHIVO_FOTO_FORMATO = "JPEG";

    static final boolean FECHAS_SON_UTC = true;
    static final String FORMATO_FECHA_LOCAL = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; // ISO 8601
    static final String FORMATO_FECHA_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; // ISO 8601

    String GUIDAplicacion;
    SharedPreferences configuracion;

    File directorioArchivosAplicacion;
    File directorioArchivosAplicacionCompleto;

    static String fecha() {
        DateFormat formato;

        if (FECHAS_SON_UTC) {
            formato = new SimpleDateFormat(FORMATO_FECHA_UTC);
            formato.setTimeZone(TimeZone.getTimeZone("UTC"));
        } else {
            formato = new SimpleDateFormat(FORMATO_FECHA_LOCAL);
        }

        return formato.format(new Date());
    }

    static String nombreFoto() {
        return String.format(
                PLANTILLA_NOMBRE_ARCHIVO_FOTO,
                fecha(),
                NOMBRE_ARCHIVO_FOTO_FORMATO
        );
    }

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
        directorioArchivosAplicacionCompleto = getExternalFilesDir(null);
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
