package xyz.fabianpineda.desarrollomovil.rinconesdemiciudad;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ExplorarRinconesActivity extends AplicacionBaseActivity {
    private static final int CODIGO_RESULTADO_INTENT_PROCESADO = -1;
    private static final int CODIGO_RESULTADO_LIMITE_SUPERIOR = 65536; // no inclusivo

    private static final Random PRNG = new Random();
    private int codigoResultadoIntentCamara;
    private int codigoResultadoIntentPublicar;

    private FloatingActionButton camaraFAB;
    private boolean camaraActivada;

    private String tempNombreFoto;

    private void resultadoPublicar(boolean ok, Intent datos) {
        Bundle extras = null;
        String archivo;

        if (datos == null || (extras = datos.getExtras()) == null) {
            notificar(R.string.error_publicando);
            return;
        } else if (!ok || (archivo = extras.getString(PublicarFotoActivity.PROPIEDAD_BUNDLE_RESULTADO_ARCHIVO, null)) == null || archivo.trim() == "") {
            notificar(extras.getString(PublicarFotoActivity.PROPIEDAD_BUNDLE_RESULTADO_MENSAJE, getString(R.string.publicar_cancelado)));
            return;
        }
    }

    private void resultadoCamara(boolean ok) {
        if (!ok) {
            notificar(R.string.camara_operacion_cancelada);
            return;
        }

        Bundle datos = new Bundle();

        codigoResultadoIntentPublicar = PRNG.nextInt(CODIGO_RESULTADO_LIMITE_SUPERIOR);

        datos.putString(PublicarFotoActivity.PROPIEDAD_BUNDLE_CAMARA_NOMBRE_ARCHIVO, new String(tempNombreFoto));
        tempNombreFoto = null;

        PublicarFotoActivity.lanzarActivity(this, datos, codigoResultadoIntentPublicar);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void lanzarCamara() {
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

        String nombreArchivo = nombreFoto();
        File archivoFoto = new File(directorioArchivosAplicacionCompleto, nombreArchivo);
        Uri rutaArchivoFoto = FileProvider.getUriForFile(this, "xyz.fabianpineda.desarrollomovil.rinconesdemiciudad.FileProvider", archivoFoto);

        if (rutaArchivoFoto == null) {
            notificar(R.string.error_escritura);

            camaraFAB.setEnabled(false);
            camaraFAB.setVisibility(View.GONE);
        }

        Intent intentAppCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intentAppCamara.resolveActivity(getPackageManager()) == null) {
            notificar(R.string.error_camara_no_app);

            camaraFAB.setEnabled(false);
            camaraFAB.setVisibility(View.GONE);

            return;
        }

        tempNombreFoto = nombreArchivo;
        intentAppCamara.putExtra(MediaStore.EXTRA_OUTPUT, rutaArchivoFoto);
        startActivityForResult(intentAppCamara, codigoResultadoIntentCamara);
    }

    /*@OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void permisoAlmacenamientoDenegadoPermanentemente() {
        notificar(DialogoPermiso.stringPermisoDenegadoPermanentemente(this, Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }*/

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void permisoCamaraDenegadoPermanentemente() {
        notificar(DialogoPermiso.stringPermisoDenegadoPermanentemente(this, Manifest.permission.CAMERA));
    }

    /*@OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void permisoAlmacenamientoDenegado() {
        notificar(DialogoPermiso.stringPermisoDenegado(this, Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }*/

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void permisoCamaraDenegado() {
        notificar(DialogoPermiso.stringPermisoDenegado(this, Manifest.permission.CAMERA));
    }

    /*@OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void permisoAlmacenamientoDialogoConfirmacion(final PermissionRequest solicitud) {
        (new DialogoPermiso(this, solicitud, Manifest.permission.WRITE_EXTERNAL_STORAGE)).mostrar();
    }*/

    @OnShowRationale(Manifest.permission.CAMERA)
    void permisoCamaraDialogoConfirmacion(final PermissionRequest solicitud) {
        (new DialogoPermiso(this, solicitud, Manifest.permission.CAMERA)).mostrar();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ExplorarRinconesActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == codigoResultadoIntentCamara) {
            codigoResultadoIntentCamara = CODIGO_RESULTADO_INTENT_PROCESADO;
            resultadoCamara(resultCode == RESULT_OK);
        } else if (requestCode == codigoResultadoIntentPublicar) {
            codigoResultadoIntentPublicar = CODIGO_RESULTADO_INTENT_PROCESADO;
            resultadoPublicar(resultCode == RESULT_OK, data);
        }
    }

    @Override
    public void onResume () {
        super.onResume();

        camaraActivada = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);

        camaraFAB.setEnabled(camaraActivada);
        camaraFAB.setVisibility(camaraActivada ? View.VISIBLE : View.GONE);
    }

    private void handlerTomarFotoPresionado() {
        ExplorarRinconesActivityPermissionsDispatcher.lanzarCamaraWithCheck(this);
    }

    @Override
    void inicializarUI() {
        setContentView(R.layout.activity_explorar_rincones);

        camaraFAB = (FloatingActionButton) findViewById(R.id.camara_fab);
        camaraFAB.setEnabled(false);
        camaraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerTomarFotoPresionado();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        codigoResultadoIntentCamara = CODIGO_RESULTADO_INTENT_PROCESADO;
        codigoResultadoIntentPublicar = CODIGO_RESULTADO_INTENT_PROCESADO;
    }
}