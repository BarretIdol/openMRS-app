/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.mobile.api;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.activeandroid.query.Select;

import org.openmrs.mobile.api.retrofit.VisitApi;
import org.openmrs.mobile.dao.PatientDAO;
import org.openmrs.mobile.dao.VisitDAO;
import org.openmrs.mobile.listeners.retrofit.DefaultResponseCallbackListener;
import org.openmrs.mobile.models.Encounter;
import org.openmrs.mobile.models.EncounterType;
import org.openmrs.mobile.models.Encountercreate;
import org.openmrs.mobile.utilities.NetworkUtils;
import org.openmrs.mobile.utilities.ToastUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;

public class EncounterService extends IntentService {

    private final RestApi apiService = RestServiceBuilder.createService(RestApi.class);

    public EncounterService() {
        super("Save Encounter");
    }

    public void addEncounter(final Encountercreate encountercreate, @Nullable DefaultResponseCallbackListener callbackListener) {

        if(NetworkUtils.isOnline()) {
            new VisitDAO().getActiveVisitByPatientId(encountercreate.getPatientId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(visit -> {
                        if (visit != null) {
                            encountercreate.setVisit(visit.getUuid());
                            if (callbackListener != null) {
                                syncEncounter(encountercreate, callbackListener);
                            }
                            else {
                                syncEncounter(encountercreate);
                            }
                        } else {
                            Log.d("EncounterService.java", "addEncounter..startNewVisitForEncounter");

                            startNewVisitForEncounter(encountercreate);
                        }
                    });
        }
        else {
            ToastUtil.error("No internet connection. Form data is saved locally " +
                    "and will sync when internet connection is restored. ");
        }
    }

    public void addEncounter(final Encountercreate encountercreate) {
        addEncounter(encountercreate, null);
    }

        private void startNewVisitForEncounter(final Encountercreate encountercreate, @Nullable final DefaultResponseCallbackListener callbackListener) {
            Log.d("EncounterS.java", "crea encounter sin visita");
            if (callbackListener != null) {
                syncEncounter(encountercreate, callbackListener);
            }
            else {
                syncEncounter(encountercreate);
            }
//            new VisitApi().startVisit(new PatientDAO().findPatientByUUID(encountercreate.getPatient()),
//                new StartVisitResponseListenerCallback() {
//                    @Override
//                    public void onStartVisitResponse(long id) {
//                        Log.d("EncounterS.java", "startNewVisitForEncounter ok");
//
//                        new VisitDAO().getVisitByID(id)
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(visit -> {
//                                    encountercreate.setVisit(visit.getUuid());
//                                    if (callbackListener != null) {
//                                       syncEncounter(encountercreate, callbackListener);
//                                    }
//                                    else {
//                                        syncEncounter(encountercreate);
//                                    }
//                                });
//                    }
//                    @Override
//                    public void onResponse() {
//                        Log.d("EncounterS.java", "startNewVisitForEncounter onResponse");
//                    }
//                    @Override
//                    public void onErrorResponse(String errorMessage) {
//                        Log.d("EncounterS.java", "startNewVisitForEncounter onErrorResponse");
//
//                        ToastUtil.error(errorMessage);
//                    }
//                });
    }

    public void startNewVisitForEncounter(final Encountercreate encountercreate) {
        startNewVisitForEncounter(encountercreate, null);
    }

    public void syncEncounter(final Encountercreate encountercreate, @Nullable final DefaultResponseCallbackListener callbackListener) {

        if (NetworkUtils.isOnline()) {
            Log.d("EncounterS.java", "syncEncounter 1");

            encountercreate.pullObslist();
            Call<Encounter> call = apiService.createEncounter(encountercreate);
            call.enqueue(new Callback<Encounter>() {
                @Override
                public void onResponse(Call<Encounter> call, Response<Encounter> response) {
                    if (response.isSuccessful()) {
                        Log.d("EncounterS.java", "onResponse success");

                        Encounter encounter = response.body();
                        encounter.setPatientUUID(encountercreate.getPatient());
                        if (encounter.getVisit()!=null)
                            linkvisit(encountercreate.getPatientId(),encountercreate.getFormname(), encounter, encountercreate);
                        encountercreate.setSynced(true);
                        encountercreate.save();
                        new VisitApi().syncBPUPEncounters(encountercreate.getPatient());
                        Log.d("EncounterS.java", "syncEncounter Visits Data Signals");
                        if (callbackListener != null) {
                            callbackListener.onResponse();
                        }
                    } else {
                        Log.d("EncounterS.java", "onResponse error");
                        if (callbackListener != null) {

                            callbackListener.onErrorResponse(response.errorBody().toString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Encounter> call, Throwable t) {
                    if (callbackListener != null) {
                        callbackListener.onErrorResponse(t.getLocalizedMessage());
                    }
                }
            });

        } else {
            ToastUtil.error("Sync is off. Turn on sync to save form data.");
        }

    }

    public void syncEncounter(final Encountercreate encountercreate) {
        syncEncounter(encountercreate, null);
    }

    private void linkvisit(Long patientid, String formname, Encounter encounter, Encountercreate encountercreate)
    {
        VisitDAO visitDAO = new VisitDAO();
        visitDAO.getVisitByUuid(encounter.getVisit().getUuid())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(visit -> {
                    encounter.setEncounterType(new EncounterType(formname));
                    for (int i=0;i<encountercreate.getObservations().size();i++)
                    {
                        encounter.getObservations().get(i).setDisplayValue
                                (encountercreate.getObservations().get(i).getValue());
                    }
                    List<Encounter> encounterList=visit.getEncounters();
                    encounterList.add(encounter);
                    Log.d("EncounterS.java", "visitSaveorUPdate1");
                    visitDAO.saveOrUpdate(visit, patientid)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(id ->
                                    ToastUtil.success(formname+" data saved successfully"));
                });
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(NetworkUtils.isOnline()) {

            List<Encountercreate> encountercreatelist = new Select()
                    .from(Encountercreate.class)
                    .execute();

            for(final Encountercreate encountercreate:encountercreatelist)
            {
                if(!encountercreate.getSynced() &&
                        new PatientDAO().findPatientByID(Long.toString(encountercreate.getPatientId())).isSynced())
                {
                    new VisitDAO().getActiveVisitByPatientId(encountercreate.getPatientId())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(visit -> {
                                if (visit != null) {
                                    encountercreate.setVisit(visit.getUuid());
                                    syncEncounter(encountercreate);

                                } else {
                                    Log.d("EncounterService.java", "onHandleVisit startNewVisitForEncounter");
                                    startNewVisitForEncounter(encountercreate);
                                }
                            });
                }
            }


        } else {
            ToastUtil.error("No internet connection. Form data is saved locally " +
                    "and will sync when internet connection is restored. ");
        }
    }

}