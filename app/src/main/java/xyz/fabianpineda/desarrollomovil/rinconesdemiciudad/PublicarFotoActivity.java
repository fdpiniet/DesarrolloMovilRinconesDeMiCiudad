package xyz.fabianpineda.desarrollomovil.rinconesdemiciudad;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PublicarFotoActivity extends AplicacionBaseActivity {
    static final String PROPIEDAD_BUNDLE_CAMARA_NOMBRE_ARCHIVO = "nombre";

    static final String PROPIEDAD_BUNDLE_RESULTADO_ARCHIVO = "archivo";
    static final String PROPIEDAD_BUNDLE_RESULTADO_MENSAJE = "mensaje";

    private TextView publicarCargando;
    private ImageView publicarPreview;
    private EditText publicarDescripcion;
    private FloatingActionButton publicarFAB;

    private String nombreFoto;
    private Bitmap imagen;

    private Intent datosResultado;
    private int codigoResultado;

    static void lanzarActivity(Activity contexto, Bundle datos, int codigoResultadoActivity) {
        Intent intentPublicar = new Intent(contexto, PublicarFotoActivity.class);
        intentPublicar.putExtras(datos);

        contexto.startActivityForResult(intentPublicar, codigoResultadoActivity);
    }

    private void producirResultadoYTerminar(int mensaje) {
        producirResultado(mensaje);
        finish();
    }

    private void producirResultado(int mensaje) {
        datosResultado.putExtra(PROPIEDAD_BUNDLE_RESULTADO_ARCHIVO, nombreFoto);
        datosResultado.putExtra(PROPIEDAD_BUNDLE_RESULTADO_MENSAJE, getString(mensaje));
        setResult(codigoResultado, datosResultado);
    }

    private long crearRegistroFoto() {
        FotosUsuarioSQLite fotosUsuario = null;
        SQLiteDatabase db = null;

        long resultado = -1L;

        try {
            fotosUsuario = new FotosUsuarioSQLite(this);
            db = fotosUsuario.getWritableDatabase();
        } catch (SQLiteException e) {}

        if (fotosUsuario == null || db == null || !db.isOpen() || db.isReadOnly()) {
            return resultado;
        }

        db.beginTransaction();
        try {
            resultado = FotosUsuarioSQLite.insertar(db, GUIDAplicacion, nombreFoto, publicarDescripcion.getText().toString());
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {} finally {
            db.endTransaction();
        }

        db.close();

        return resultado;
    }

    private void handlerPublicarPresionado() {
        if (crearRegistroFoto() < 0) {
            producirResultadoYTerminar(R.string.error_sqlite);
        }

        codigoResultado = RESULT_OK;
        producirResultadoYTerminar(R.string.publicar_exito);
    }

    @Override
    public void onBackPressed() {
        producirResultado(R.string.publicar_cancelado);
        super.onBackPressed();
    }

    private void activarUI() {
        publicarPreview.setImageBitmap(imagen);
        publicarCargando.setVisibility(GONE);
        publicarPreview.setVisibility(VISIBLE);
        publicarFAB.setVisibility(VISIBLE);
        publicarFAB.setEnabled(true);
    }

    @Override
    void inicializarUI() {
        setContentView(R.layout.activity_publicar_foto);

        publicarCargando = (TextView) findViewById(R.id.publicar_mensaje_cargando);
        publicarPreview = (ImageView) findViewById(R.id.publicar_preview);
        publicarDescripcion = (EditText) findViewById(R.id.publicar_descripcion);

        publicarFAB = (FloatingActionButton) findViewById(R.id.publicar_fab);
        publicarFAB.setEnabled(false);
        publicarFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerPublicarPresionado();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle informacionFoto = getIntent().getExtras();

        datosResultado = new Intent();
        datosResultado.putExtra(PROPIEDAD_BUNDLE_RESULTADO_ARCHIVO, "");
        datosResultado.putExtra(PROPIEDAD_BUNDLE_RESULTADO_MENSAJE, getString(R.string.error_publicando));

        codigoResultado = RESULT_CANCELED;
        setResult(codigoResultado, datosResultado);

        if (informacionFoto == null || (nombreFoto = informacionFoto.getString(PROPIEDAD_BUNDLE_CAMARA_NOMBRE_ARCHIVO, null)) == null) {
            producirResultadoYTerminar(R.string.error_escritura);
        }

        BitmapFactory.Options opcionesDecodificador = new BitmapFactory.Options();
        opcionesDecodificador.inSampleSize = 4; // 1/4 tamaño original.

        imagen = BitmapFactory.decodeFile((new File(directorioArchivosAplicacionCompleto, nombreFoto)).getPath(), opcionesDecodificador);

        activarUI();
    }
}