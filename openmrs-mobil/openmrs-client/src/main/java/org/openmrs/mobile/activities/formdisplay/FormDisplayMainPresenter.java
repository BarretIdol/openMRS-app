/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.mobile.activities.formdisplay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import org.joda.time.LocalDateTime;
import org.openmrs.mobile.activities.BasePresenter;
import org.openmrs.mobile.api.EncounterService;
import org.openmrs.mobile.api.RestApi;
import org.openmrs.mobile.api.retrofit.VisitApi;
import org.openmrs.mobile.dao.ConceptDAO;
import org.openmrs.mobile.dao.EncounterDAO;
import org.openmrs.mobile.dao.PatientDAO;
import org.openmrs.mobile.dao.VisitDAO;
import org.openmrs.mobile.dao.LocationDAO;
import org.openmrs.mobile.listeners.retrofit.DefaultResponseCallbackListener;
import org.openmrs.mobile.models.Encounter;
import org.openmrs.mobile.models.EncounterType;
import org.openmrs.mobile.models.Encountercreate;
import org.openmrs.mobile.models.Obscreate;
import org.openmrs.mobile.models.Observation;
import org.openmrs.mobile.models.Patient;
import org.openmrs.mobile.models.Visit;
import org.openmrs.mobile.models.VisitType;
import org.openmrs.mobile.utilities.ApplicationConstants;
import org.openmrs.mobile.utilities.DateUtils;
import org.openmrs.mobile.utilities.FormService;
import org.openmrs.mobile.utilities.InputField;
import org.openmrs.mobile.utilities.SelectOneField;
import org.openmrs.mobile.utilities.ToastUtil;
import org.openmrs.mobile.application.OpenMRS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.openmrs.mobile.utilities.FormService.getFormResourceByName;

public class FormDisplayMainPresenter extends BasePresenter implements FormDisplayContract.Presenter.MainPresenter {

    private final long mPatientID;
    private final String mEncountertype;
    private final String mFormname;
    private FormDisplayContract.View.MainView mFormDisplayView;
    private Patient mPatient;
    private FormPageAdapter mPageAdapter;

    public FormDisplayMainPresenter(FormDisplayContract.View.MainView mFormDisplayView, Bundle bundle, FormPageAdapter mPageAdapter) {
        this.mFormDisplayView = mFormDisplayView;
        this.mPatientID =(long) bundle.get(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
        this.mPatient =new PatientDAO().findPatientByID(Long.toString(mPatientID));
        this.mEncountertype =(String)bundle.get(ApplicationConstants.BundleKeys.ENCOUNTERTYPE);
        this.mFormname = (String) bundle.get(ApplicationConstants.BundleKeys.FORM_NAME);
        this.mPageAdapter = mPageAdapter;
        mFormDisplayView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        // This method is intentionally empty
    }

    @Override
    public void createEncounter() {
        List<InputField> inputFields = new ArrayList<>();
        List<SelectOneField> radioGroupFields = new ArrayList<>();
        FormService fs = new FormService();
        mFormDisplayView.enableSubmitButton(false);

        Encountercreate encountercreate=new Encountercreate();
        encountercreate.setPatient(mPatient.getUuid());
        encountercreate.setEncounterType(mEncountertype);
        List<Obscreate> observations=new ArrayList<>();

       /* EncounterDAO encDAO = new EncounterDAO();
        Encounter encounter = new Encounter();
        encounter.setId(new Random().nextLong());
        encounter.setEncounterType(new EncounterType(mEncountertype));
        encounter.setUuid(UUID.randomUUID().toString());
        //encounter.setDisplay(display);
        encounter.setEncounterDatetime(DateUtils.convertTime(System.currentTimeMillis(), DateUtils.OPEN_MRS_REQUEST_FORMAT));
        //encounter.setForm(fs.getForm(mFormname));
        encounter.setPatient(mPatient);
        encounter.setEncounterType(encDAO.getEncounterTypeByFormName(mFormname));
        List<Observation> encounter_obs = new ArrayList<>();*/

        SparseArray<Fragment> activefrag = mPageAdapter.getRegisteredFragments();
        boolean valid=true;
        for (int i = 0;i < activefrag.size();i++) {
            FormDisplayPageFragment formPageFragment=(FormDisplayPageFragment)activefrag.get(i);
            if(!formPageFragment.checkInputFields()) {
                valid=false;
                break;
            }

            inputFields.addAll(formPageFragment.getInputFields());
            radioGroupFields.addAll(formPageFragment.getSelectOneFields());
        }

        if(valid) {
            for (InputField input: inputFields) {
                if(input.getValue()!=-1.0) {
                    Obscreate obscreate = new Obscreate();
                    obscreate.setConcept(input.getConcept());
                    obscreate.setValue(String.valueOf(input.getValue()));
                    LocalDateTime localDateTime = new LocalDateTime();
                    obscreate.setObsDatetime(localDateTime.toString());
                    obscreate.setPerson(mPatient.getUuid());
                    observations.add(obscreate);
                    // prueba por hector, cuando intenta cargar el encounter con observaciones falla (hace falta algo)
                    // necesita arreglarse, de momento el encounter tiene una lista vacia de obs
                  /*  Observation obs = new Observation();
                    obs.setObsDatetime(localDateTime.toString());
                    obs.setPerson(mPatient.getPerson());
                    obs.setId(new Random().nextLong());
                    obs.setEncounterID(encounter.getId());
                    obs.setDisplayValue(String.valueOf(input.getValue()));
                    ConceptDAO cd = new ConceptDAO();
                    obs.setUuid(UUID.randomUUID().toString());
                    obs.setConcept(cd.findConceptsByUUID(input.getConcept()));
                    encounter_obs.add(obs);*/
                }
            }

            for (SelectOneField radioGroupField : radioGroupFields) {
                if (radioGroupField.getChosenAnswer() != null) {
                    Obscreate obscreate = new Obscreate();
                    obscreate.setConcept(radioGroupField.getConcept());
                    obscreate.setValue(radioGroupField.getChosenAnswer().getConcept());
                    LocalDateTime localDateTime = new LocalDateTime();
                    obscreate.setObsDatetime(localDateTime.toString());
                    obscreate.setPerson(mPatient.getUuid());
                    observations.add(obscreate);
                    // prueba por hector, cuando intenta cargar el encounter con observaciones falla (hace falta algo)
                    // necesita arreglarse, de momento el encounter tiene una lista vacia de obs
                  /*  Observation obs = new Observation();
                    obs.setPerson(mPatient.getPerson());
                    obs.setObsDatetime(localDateTime.toString());
                    obs.setId(new Random().nextLong());
                    obs.setEncounterID(encounter.getId());
                    obs.setEncounter(encounter);
                    obs.setUuid(UUID.randomUUID().toString());
                    obs.setDisplayValue(radioGroupField.getChosenAnswer().getConcept());
                    ConceptDAO cd = new ConceptDAO();
                    obs.setConcept(cd.findConceptsByUUID(radioGroupField.getConcept()));
                    encounter_obs.add(obs);*/
                }
            }

            encountercreate.setObservations(observations);
            encountercreate.setFormname(mFormname);
            encountercreate.setPatientId(mPatientID);
            encountercreate.setFormUuid(getFormResourceByName(mFormname).getUuid());
            encountercreate.setObslist();
            encountercreate.save();

           /* encounter.setObservations(encounter_obs);
            Visit visita = new Visit();
            visita.setStartDatetime(DateUtils.convertTime(System.currentTimeMillis(), DateUtils.OPEN_MRS_REQUEST_FORMAT));
            visita.setPatient(mPatient);
            LocationDAO locationDAO = new LocationDAO();
            visita.setLocation(locationDAO.findLocationByName(OpenMRS.getInstance().getLocation()));
            visita.setVisitType(new VisitType(null, OpenMRS.getInstance().getVisitTypeUUID()));
            visita.setUuid(UUID.randomUUID().toString());
            Random randomlong = new Random();
            long idd= randomlong.nextLong();
            visita.setId(idd);
            List<Encounter> encounters = new ArrayList<>();
            encounters.add(encounter);
            visita.setEncounters(encounters);*/
           // visita.setStopDatetime(DateUtils.convertTime(System.currentTimeMillis(), DateUtils.OPEN_MRS_REQUEST_FORMAT));
          /*  VisitDAO vd = new VisitDAO();
            vd.saveVisit(visita, mPatient.getId());*/
           // la api de visita no me crea nada
           /* VisitApi visitApi = new VisitApi();
            visitApi.createVisitProva(visita.getPatient(),null,visita);^*/

            // de esta manera puedo acceder a los encountercreate guardados
            /*Encountercreate prova = new Encountercreate();
            prova = prova.load(prova.getClass(), encountercreate.getId());
            prova.getFormUuid();*/
            if(!mPatient.isSynced()) {
                mPatient.addEncounters(encountercreate.getId());
                new PatientDAO().updatePatient(mPatient.getId(),mPatient);
                ToastUtil.error("Patient not yet registered. Form data is saved locally " +
                        "and will sync when internet connection is restored. ");
                mFormDisplayView.enableSubmitButton(true);
            }
            else {
                new EncounterService().addEncounter(encountercreate, new DefaultResponseCallbackListener() {
                    @Override
                    public void onResponse() {
                        mFormDisplayView.enableSubmitButton(true);
                    }
                    @Override
                    public void onErrorResponse(String errorMessage) {
                        mFormDisplayView.showToast(errorMessage);
                        mFormDisplayView.enableSubmitButton(true);
                    }
                });
                mFormDisplayView.quitFormEntry();
            }
        }
        else {
            mFormDisplayView.enableSubmitButton(true);
        }
    }
}
