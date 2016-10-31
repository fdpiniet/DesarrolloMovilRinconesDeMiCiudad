package xyz.fabianpineda.desarrollomovil.rinconesdemiciudad;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import permissions.dispatcher.PermissionRequest;

public class DialogoPermiso {
    private Context contexto;
    private AlertDialog.Builder builder;

    public static String stringPermisoDenegadoPermanentemente(Context contexto, String permiso) {
        return String.format(
                contexto.getString(R.string.permiso_denegado_permanentemente_formato),
                stringNombrePermiso(contexto, permiso)
        );
    }

    public static String stringPermisoDenegado(Context contexto, String permiso) {
        return String.format(
                contexto.getString(R.string.permiso_denegado_formato),
                stringNombrePermiso(contexto, permiso)
        );
    }

    public static String stringPermisoConcedido(Context contexto, String permiso) {
        return String.format(
                contexto.getString(R.string.permiso_concedido_formato),
                stringNombrePermiso(contexto, permiso)
        );
    }

    public static String stringSolicitudPermiso(Context contexto, String permiso) {
        return String.format(
            contexto.getString(R.string.dialogo_solicitud_permiso_mensaje_formato),
            stringNombrePermiso(contexto, permiso)
        );
    }

    public static String stringNombrePermiso(Context contexto, String permiso) {
        switch (permiso) {
            case "CAMERA":
                return  contexto.getString(R.string.permiso_camara);
            case "WRITE_EXTERNAL_STORAGE":
                return contexto.getString(R.string.permiso_almacenamiento_externo);
            default:
                return contexto.getString(R.string.permiso_camara);
        }
    }

    public void mostrar() {
        builder.show();
    }

    public DialogoPermiso(final Context contexto, final PermissionRequest solicitud, final String permiso) {
        this.contexto = contexto;
        builder = new AlertDialog.Builder(contexto);

        builder.setTitle(R.string.dialogo_solicitud_permiso_titulo);
        builder.setMessage(stringSolicitudPermiso(contexto, permiso));

        builder.setPositiveButton(R.string.boton_permiso_conceder, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExplorarRinconesActivity.notificar(contexto, stringPermisoConcedido(contexto, permiso));
                solicitud.proceed();
            }
        });

        builder.setNegativeButton(R.string.boton_permiso_denegar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                solicitud.cancel();
            }
        });

        builder.setCancelable(false);
    }
}
