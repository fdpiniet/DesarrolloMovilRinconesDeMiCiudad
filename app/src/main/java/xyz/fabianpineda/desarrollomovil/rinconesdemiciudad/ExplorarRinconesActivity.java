package xyz.fabianpineda.desarrollomovil.rinconesdemiciudad;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Random;
import java.util.UUID;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ExplorarRinconesActivity extends AppCompatActivity {
    private static final String ARCHIVO_PREFERENCIA_APLICACION = ExplorarRinconesActivity.class.getSimpleName();
    private static final String PREFERENCIA_GUID_APLICACION = "GUID_APLICACION";

    private static final int CODIGO_RESULTADO_INTENT_PROCESADO = -1;
    private static final int CODIGO_RESULTADO_LIMITE_SUPERIOR = 65536; // no inclusivo

    private static final Random PRNG = new Random();
    private int codigoResultadoIntentCamara;

    private String GUIDAplicacion;
    private SharedPreferences configuracion;

    FloatingActionButton camaraFAB;
    boolean camaraActivada;

    public static void notificar(Context contexto, String mensaje) {
        Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT).show();
    }

    public static void notificar(Context contexto, int recursoString) {
        notificar(contexto, contexto.getString(recursoString));
    }

    protected void resultadoCamara(boolean ok, Intent datos) {
        if (!ok) {
            notificar(this, R.string.camara_operacion_cancelada);
            return;
        }

        /*
         * Los datos de la imagen están en la propiedad "data" del bundle extras de "datos".
         * TODO: enviar datos a PublicarFotoActivity; terminar su Layout, implementar funciones HTTP
         */
        notificar(this, "OK!");
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void lanzarCamara() {
        /*
         * codigoResultadoIntentCamara debe ser un número entero de 16 bits; usando un número
         * entero con un valor superior a 16 bits causaba:
         *
         *      java.lang.IllegalArgumentException: Can only use lower 16 bits for requestCode
         *
         * Adicionalmente, codigoResultadoIntentCamara debe ser igual o mayor que 0 para poder
         * identificar su resultado.
         */
        codigoResultadoIntentCamara = PRNG.nextInt(CODIGO_RESULTADO_LIMITE_SUPERIOR);

        Intent intentAppCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intentAppCamara.resolveActivity(getPackageManager()) == null) {
            notificar(this, R.string.error_camara_no_app);

            camaraFAB.setEnabled(false);
            camaraFAB.setVisibility(View.GONE);

            return;
        }

        startActivityForResult(intentAppCamara, codigoResultadoIntentCamara);
    }

    /*@OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void permisoAlmacenamientoDenegadoPermanentemente() {
        notificar(this, DialogoPermiso.stringPermisoDenegadoPermanentemente(this, Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }*/

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    public void permisoCamaraDenegadoPermanentemente() {
        notificar(this, DialogoPermiso.stringPermisoDenegadoPermanentemente(this, Manifest.permission.CAMERA));
    }

    /*@OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void permisoAlmacenamientoDenegado() {
        notificar(this, DialogoPermiso.stringPermisoDenegado(this, Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }*/

    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void permisoCamaraDenegado() {
        notificar(this, DialogoPermiso.stringPermisoDenegado(this, Manifest.permission.CAMERA));
    }

    /*@OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void permisoAlmacenamientoDialogoConfirmacion(final PermissionRequest solicitud) {
        (new DialogoPermiso(this, solicitud, Manifest.permission.WRITE_EXTERNAL_STORAGE)).mostrar();
    }*/

    @OnShowRationale(Manifest.permission.CAMERA)
    public void permisoCamaraDialogoConfirmacion(final PermissionRequest solicitud) {
        (new DialogoPermiso(this, solicitud, Manifest.permission.CAMERA)).mostrar();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ExplorarRinconesActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == codigoResultadoIntentCamara && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            notificar(this, "OK!");
        }*/

        if (requestCode == codigoResultadoIntentCamara) {
            codigoResultadoIntentCamara = CODIGO_RESULTADO_INTENT_PROCESADO;
            resultadoCamara(resultCode == RESULT_OK, data);
        }
    }

    @Override
    public void onResume () {
        super.onResume();

        camaraActivada = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);

        camaraFAB.setEnabled(camaraActivada);
        camaraFAB.setVisibility(camaraActivada ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.opcion_explorar_rincones:
                notificar(this, "DEBUG: Presionado \"Explorar Rincones\".");
                return true;
            case R.id.opcion_explorar_mis_rincones:
                notificar(this, "DEBUG Presionado \"Mis Rincones\".");
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

    protected void inicializarUI() {
        final ExplorarRinconesActivity self = this;

        setContentView(R.layout.activity_explorar_rincones);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        camaraFAB = (FloatingActionButton) findViewById(R.id.fab);
        camaraFAB.setEnabled(false);
        camaraFAB.setVisibility(View.GONE);

        camaraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExplorarRinconesActivityPermissionsDispatcher.lanzarCamaraWithCheck(self);
            }
        });
    }

    private void cargarGUIDAplicacion() {
        GUIDAplicacion = configuracion.getString(PREFERENCIA_GUID_APLICACION, null);

        if (GUIDAplicacion == null) {
            GUIDAplicacion = UUID.randomUUID().toString();
            configuracion.edit().putString(PREFERENCIA_GUID_APLICACION, GUIDAplicacion).commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configuracion = getSharedPreferences(ARCHIVO_PREFERENCIA_APLICACION, MODE_PRIVATE);

        inicializarUI();
        cargarGUIDAplicacion();

        codigoResultadoIntentCamara = CODIGO_RESULTADO_INTENT_PROCESADO;
    }
}