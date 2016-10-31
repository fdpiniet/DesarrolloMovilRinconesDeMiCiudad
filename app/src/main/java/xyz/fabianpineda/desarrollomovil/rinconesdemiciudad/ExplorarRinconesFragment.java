package xyz.fabianpineda.desarrollomovil.rinconesdemiciudad;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class ExplorarRinconesFragment extends Fragment {

    public ExplorarRinconesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explorar_rincones, container, false);
    }
}
