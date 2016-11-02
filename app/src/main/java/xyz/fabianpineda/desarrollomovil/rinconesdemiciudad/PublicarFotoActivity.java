package xyz.fabianpineda.desarrollomovil.rinconesdemiciudad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PublicarFotoActivity extends AplicacionBaseActivity {
    private TextView publicarMensaje;
    private ImageView publicarPreview;
    private LinearLayout publicarDescripcionContenedor;
    private EditText publicarDescripcion;
    private FloatingActionButton publicarFAB;

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

    private void handlerPublicarPresionado() {
        // TODO: guardar imágen en DISCX, hacer entrada en DB
        notificar("Botón publicar presionado.");
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
