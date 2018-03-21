package org.openmrs.mobile.activities.formview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.openmrs.mobile.R;
import org.openmrs.mobile.models.Observation;

import java.util.List;

/**
 * Created by Hector on 21/03/2018.
 */

public class EncounterAdapter extends RecyclerView.Adapter<EncounterAdapter.EncounterData> {

    List<Observation> mObservations;

    public EncounterAdapter(List<Observation> mObservations) {
        this.mObservations = mObservations;
    }

    @Override
    public EncounterAdapter.EncounterData onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.encounter_list,null,false);
        return new EncounterData(view);
    }

    @Override
    public void onBindViewHolder(EncounterAdapter.EncounterData holder, int position) {
            holder.assignData(mObservations.get(position));
    }

    @Override
    public int getItemCount() {
        return mObservations.size();
    }

    public class EncounterData extends RecyclerView.ViewHolder {

        TextView obsname;
        TextView obsvalue;

        public EncounterData(View itemView) {
            super(itemView);
            obsname = (TextView) itemView.findViewById(R.id.obsname);
            obsvalue = (TextView) itemView.findViewById(R.id.obsvalue);
        }

        public void assignData(Observation obs) {
            obsname.setText(obs.getDisplay());
            obsvalue.setText(obs.getDisplayValue());
        }
    }
}
