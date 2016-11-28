package xyz.fabianpineda.desarrollomovil.rinconesdemiciudad;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class AdaptadorRincones extends CursorAdapter {
    File directorioFotos;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_rincones, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView rinconImagen = (ImageView) view.findViewById(R.id.rinconImagen);
        TextView rinconFecha = (TextView) view.findViewById(R.id.rinconFecha);
        TextView rinconDescripcion = (TextView) view.findViewById(R.id.rinconDescripcion);

        rinconFecha.setText(cursor.getString(5));

        String descripcion = cursor.getString(4);

        if (descripcion == null || descripcion.trim().length() < 1) {
            descripcion = context.getString(R.string.explorar_no_descripcion);
        }

        rinconDescripcion.setText(descripcion);

        BitmapFactory.Options opcionesDecodificador = new BitmapFactory.Options();
        opcionesDecodificador.inSampleSize = 4; // 1/4 tamaño original.

        rinconImagen.setImageBitmap(BitmapFactory.decodeFile((new File(directorioFotos, cursor.getString(3))).getPath(), opcionesDecodificador));
    }

    public AdaptadorRincones(Context contexto, Cursor cursor, File dirFotos) {
        super(contexto, cursor, 0);
        directorioFotos = dirFotos;
    }
}
