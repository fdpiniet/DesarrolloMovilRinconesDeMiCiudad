package xyz.fabianpineda.desarrollomovil.rinconesdemiciudad;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class PublicarFotoActivity extends AplicacionBaseActivity {
    private TextView publicarMensaje;
    private ImageView publicarPreview;
    private LinearLayout publicarDescripcionContenedor;
    private EditText publicarDescripcion;
    private FloatingActionButton publicarFAB;

    private File archivoFoto;
    private Bitmap foto;

    static String stringErrorFoto (Context contexto, String error) {
        return String.format(
            contexto.getString(R.string.publicar_error_formato),
            "\n\n\n" +
            error
        );
    }

    static void lanzarActivity(Context contexto, Bundle datos) {
        Intent intentPublicar = new Intent(contexto, PublicarFotoActivity.class);
        intentPublicar.putExtras(datos);

        contexto.startActivity(intentPublicar);
    }

    private long crearRegistroFoto() {
        // TODO: probar.
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
            resultado = FotosUsuarioSQLite.insertar(db, GUIDAplicacion, /*TODO ruta*/ null, publicarDescripcion.getText().toString());
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {} finally {
            db.endTransaction();
        }

        db.close();

        return resultado;
    }

    private boolean guardarArchivoFoto() {
        // TODO: implementar.

        /*archivoFoto = new File(directorioFotosUsuario, UUID.randomUUID().toString() + ".jpg");

        FileOutputStream salida;
        try {
            salida = openFileOutput(archivoFoto.toString, Context.MODE_PRIVATE);
            salida.write(string.getBytes());
            salida.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        return true;
    }

    private void handlerPublicarPresionado() {
        if (!guardarArchivoFoto()) {
            abortarActivity(R.string.error_sqlite);
        } else if (crearRegistroFoto() < 0) {
            abortarActivity(R.string.error_sqlite);
        } else {
            // TODO
            notificar("TO-DO: guardarArchivoFoto(), crearRegistroFoto(), handlerPublicarPresionado()");
        }
    }

    private void errorFoto() {
        publicarMensaje.setText(stringErrorFoto(this, getString(R.string.publicar_error_foto)));
    }

    @Override
    void inicializarMenu() {
        super.inicializarMenu();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void activarUI() {
        publicarMensaje.setVisibility(View.GONE);

        publicarPreview.setVisibility(View.VISIBLE);

        publicarFAB.setVisibility(View.VISIBLE);
        publicarFAB.setEnabled(true);

        publicarDescripcionContenedor.setVisibility(View.VISIBLE);
        publicarDescripcion.setEnabled(true);

        publicarPreview.setImageBitmap(foto);
    }

    @Override
    void inicializarUI() {
        setContentView(R.layout.activity_publicar_foto);

        publicarMensaje = (TextView) findViewById(R.id.publicar_mensaje);
        publicarPreview = (ImageView) findViewById(R.id.publicar_preview);

        publicarDescripcionContenedor = (LinearLayout) findViewById(R.id.publicar_descripcion_contenedor);
        publicarDescripcion = (EditText) findViewById(R.id.publicar_descripcion);
        publicarDescripcion.setEnabled(false);

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

        Object imagen;

        if (informacionFoto == null || (imagen = informacionFoto.get("data")) == null) {
            errorFoto();
        } else {
            try {
                foto = (Bitmap) imagen;
            } catch (ClassCastException e) {}

            if (foto == null) {
                errorFoto();
            } else {
                activarUI();
            }
        }
    }
}
