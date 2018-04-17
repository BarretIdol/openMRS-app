/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.mobile.activities.patientdashboard.vitals;

import android.util.Log;

import com.google.common.collect.Lists;

import org.openmrs.mobile.activities.patientdashboard.PatientDashboardContract;
import org.openmrs.mobile.activities.patientdashboard.PatientDashboardMainPresenterImpl;
import org.openmrs.mobile.api.retrofit.VisitApi;
import org.openmrs.mobile.dao.EncounterDAO;
import org.openmrs.mobile.dao.PatientDAO;
import org.openmrs.mobile.listeners.retrofit.DefaultResponseCallbackListener;
import org.openmrs.mobile.models.Encounter;
import org.openmrs.mobile.models.EncounterMethods;
import org.openmrs.mobile.models.EncounterType;
import org.openmrs.mobile.models.Encountercreate;
import org.openmrs.mobile.models.Patient;
import org.openmrs.mobile.utilities.ApplicationConstants;
import org.openmrs.mobile.utilities.NetworkUtils;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;

public class PatientDashboardVitalsPresenter extends PatientDashboardMainPresenterImpl implements PatientDashboardContract.PatientVitalsPresenter {

    private EncounterDAO encounterDAO;
    //private VisitApi visitApi;
    private PatientDashboardContract.ViewPatientVitals mPatientVitalsView;
    private String mEncounterType;

    public PatientDashboardVitalsPresenter(String id, PatientDashboardContract.ViewPatientVitals mPatientVitalsView, String encounterType) {
        this.mPatient = new PatientDAO().findPatientByID(id);
        this.mPatientVitalsView = mPatientVitalsView;
        this.mPatientVitalsView.setPresenter(this);
        this.encounterDAO = new EncounterDAO();
        this.mEncounterType = encounterType;
        //this.visitApi = new VisitApi();
    }

    public PatientDashboardVitalsPresenter(Patient patient, PatientDashboardContract.ViewPatientVitals mPatientVitalsView,
                                           EncounterDAO encounterDAO, VisitApi visitApi) {
        this.mPatient = patient;
        this.mPatientVitalsView = mPatientVitalsView;
        this.mPatientVitalsView.setPresenter(this);
        this.encounterDAO = encounterDAO;
        //this.visitApi = visitApi;
    }

    @Override
    public void subscribe() {
        loadVitalsFromDB(mEncounterType);
        //loadStaticFormFromServer();
    }

    /*private void loadStaticFormFromServer() {
        if (NetworkUtils.isOnline()) {
            visitApi.syncLastVitals(mPatient.getUuid(), new DefaultResponseCallbackListener() {
                @Override
                public void onResponse() {
                    loadVitalsFromDB();
                }

                @Override
                public void onErrorResponse(String errorMessage) {
                    mPatientVitalsView.showErrorToast(errorMessage);
                }
            });
        }
    }*/

    /**Returns de latest encountercreate of Encountertype encountertype*/
    private Encountercreate getLatestEncountercreate(String encounterType) {
        List<Encountercreate> eCreate = mPatient.getEncountercreates();
        Encountercreate enc = null;
        if (!eCreate.isEmpty()) {
            for (Encountercreate c: Lists.reverse(eCreate)) {
                if (c.getFormname().equals(encounterType)) { //The newest will always be added last
                    enc = c;
                    break;
                   /* if(enc == null) { //es construeix de manera que els ultims son els més nous?
                        enc=c;
                    }
                    else if (enc.getEncounterDatetime()<=c.getEncounterDatetime()) {
                        enc = c;
                    }*/
                }
            }
        }
        return enc;
    }

    private void loadVitalsFromDB(String encounterType) {
        Encountercreate enc = getLatestEncountercreate(encounterType);
        if (enc != null) {
            mPatientVitalsView.showEncounterVitals(enc);
        } else {
            Encounter e = encounterDAO.getStaticFormEncounter1(mPatient.getUuid(), encounterType);
            if (e != null) {
                mPatientVitalsView.showEncounterVitals(e);
            } else {
                Log.d("Res", "Res de Res");
                mPatientVitalsView.showNoVitalsNotification();
            }
        /*addSubscription(encounterDAO.getStaticFormEncounter(mPatient.getUuid(),encounterType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(encounter -> {
                    if (encounter != null) {
                        mPatientVitalsView.showEncounterVitals(encounter);
                    } else {
                        Log.d("Res","Res de Res");
                        mPatientVitalsView.showNoVitalsNotification();
                    }
                }));*/
        }
    }

    @Override
    public void startFormDisplayActivityWithEncounter() {
        addSubscription(encounterDAO.getStaticFormEncounter(mPatient.getUuid(),mEncounterType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(encounter -> {
                    mPatientVitalsView.startFormDisplayActivity(encounter);
                }));
    }
}
