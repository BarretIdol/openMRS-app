package org.openmrs.mobile.activities.formview;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.ACBaseActivity;
import org.openmrs.mobile.dao.EncounterDAO;
import org.openmrs.mobile.dao.ObservationDAO;
import org.openmrs.mobile.utilities.ApplicationConstants;

/**
 * Created by Hector on 21/03/2018.
 */

public class FormViewActivity extends ACBaseActivity {

    RecyclerView recycler;
    long encID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_encounter_viewer);

        recycler = (RecyclerView) findViewById(R.id.encounter_recycle_view);
        recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        ObservationDAO observationDAO = new ObservationDAO();
        Bundle bundle = getIntent().getExtras();
        encID = bundle.getLong(ApplicationConstants.BundleKeys.ENCOUNTER_ID);
        EncounterAdapter adapter = new EncounterAdapter(observationDAO.findObservationByEncounterID(encID));
        recycler.setAdapter(adapter);
    }
}
