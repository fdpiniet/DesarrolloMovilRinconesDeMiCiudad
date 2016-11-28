package xyz.fabianpineda.desarrollomovil.rinconesdemiciudad;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class RinconesFragment extends Fragment {
    private ListView lista;

    void setAdaptador(AdaptadorRincones adaptador) {
        lista.setAdapter(adaptador);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_rincones, container, false);
        lista = (ListView) layout.findViewById(R.id.rinconesView);

        return layout;
    }
}
