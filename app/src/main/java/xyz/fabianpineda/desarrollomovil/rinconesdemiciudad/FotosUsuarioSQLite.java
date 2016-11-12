package xyz.fabianpineda.desarrollomovil.rinconesdemiciudad;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FotosUsuarioSQLite extends SQLiteOpenHelper {
    static final String DB_NOMBRE = "RinconesDeMiCiudad.db";
    static final int DB_VERSION = 1;

    static final String TABLA_FOTOS_USUARIO = "FotosUsuario";
    static final String TABLA_FOTOS_USUARIO_PK = "id";
    static final String TABLA_FOTOS_USUARIO_ID = "id_usuario";
    static final String TABLA_FOTOS_USUARIO_RUTA = "ruta";
    static final String TABLA_FOTOS_USUARIO_DESCRIPCION = "descripcion";
    static final String TABLA_FOTOS_USUARIO_FECHA_CREACION = "fecha_creacion";
    static final String TABLA_FOTOS_USUARIO_SQL_CREAR = String.format(
        "CREATE TABLE %s (" +
            "%s INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
            "%s TEXT NOT NULL," +
            "%s TEXT NOT NULL," +
            "%s TEXT," +
            "%s	DATETIME DEFAULT CURRENT_TIMESTAMP" +
        ");",

        TABLA_FOTOS_USUARIO,
            TABLA_FOTOS_USUARIO_PK,
            TABLA_FOTOS_USUARIO_ID,
            TABLA_FOTOS_USUARIO_RUTA,
            TABLA_FOTOS_USUARIO_DESCRIPCION,
            TABLA_FOTOS_USUARIO_FECHA_CREACION
    );
    static final String TABLA_FOTOS_USUARIO_SQL_DESTRUIR = String.format(
        "DROP TABLE IF EXISTS %s;",
        TABLA_FOTOS_USUARIO
    );

    static long insertar(SQLiteDatabase db, String id_usuario, String ruta, String descripcion) {
        ContentValues registro = new ContentValues();

        registro.put(TABLA_FOTOS_USUARIO_ID, id_usuario);
        registro.put(TABLA_FOTOS_USUARIO_RUTA, ruta);
        registro.put(TABLA_FOTOS_USUARIO_DESCRIPCION, descripcion);

        return db.insert(TABLA_FOTOS_USUARIO, null, registro);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLA_FOTOS_USUARIO_SQL_DESTRUIR);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_FOTOS_USUARIO_SQL_CREAR);
    }

    FotosUsuarioSQLite(Context contexto) {
        super(contexto, DB_NOMBRE, null, DB_VERSION);
    }
}