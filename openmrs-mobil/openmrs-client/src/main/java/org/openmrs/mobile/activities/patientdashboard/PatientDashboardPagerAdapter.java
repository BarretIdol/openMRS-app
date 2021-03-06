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

package org.openmrs.mobile.activities.patientdashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import org.openmrs.mobile.activities.patientdashboard.charts.PatientChartsFragment;
import org.openmrs.mobile.activities.patientdashboard.charts.PatientDashboardChartsPresenter;
import org.openmrs.mobile.activities.patientdashboard.details.PatientDashboardDetailsPresenter;
import org.openmrs.mobile.activities.patientdashboard.details.PatientDetailsFragment;
import org.openmrs.mobile.activities.patientdashboard.encounters.PatientDashboardEncountersPresenter;
import org.openmrs.mobile.activities.patientdashboard.encounters.PatientEncountersFragment;
import org.openmrs.mobile.activities.patientdashboard.vitals.PatientStaticEncounterPresenter;
import org.openmrs.mobile.activities.patientdashboard.vitals.PatientStaticEncounterFragment;
import org.openmrs.mobile.models.EncounterType;
import org.openmrs.mobile.utilities.ApplicationConstants;

class PatientDashboardPagerAdapter extends FragmentPagerAdapter {

    private static final int TAB_COUNT = 5;

    private static final int DETAILS_TAB_POS = 0;
    private static final int PERSONAL_TAB_POS = 1;
    private static final int FORMS_TAB_POS = 2;
    private static final int FACTORS_TAB_POS = 3;
    private static final int CHARTS_TAB_POS = 4;

    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

    private String mPatientId;

    PatientDashboardPagerAdapter(FragmentManager fm, String id) {
        super(fm);
        this.mPatientId = id;
    }

    @Override
    public Fragment getItem(int i) {
        Bundle data = new Bundle();
        switch (i) {
            case DETAILS_TAB_POS:
                PatientDetailsFragment patientDetailsFragment = PatientDetailsFragment.newInstance();
                new PatientDashboardDetailsPresenter(mPatientId, patientDetailsFragment);
                return patientDetailsFragment;
            case PERSONAL_TAB_POS:
                PatientStaticEncounterFragment patientPersonalFragment = PatientStaticEncounterFragment.newInstance();
                data.putString(ApplicationConstants.BundleKeys.ENCOUNTERTYPE,EncounterType.PERSONAL_DATA);
                patientPersonalFragment.setArguments(data);
                new PatientStaticEncounterPresenter(mPatientId, patientPersonalFragment,""); //why?
                return patientPersonalFragment;
            case FORMS_TAB_POS:
                PatientEncountersFragment patientEncountersFragment = PatientEncountersFragment.newInstance();
                new PatientDashboardEncountersPresenter(mPatientId, patientEncountersFragment);
                return patientEncountersFragment;
            case FACTORS_TAB_POS:
                PatientStaticEncounterFragment patientStaticEncounterFragment = PatientStaticEncounterFragment.newInstance();
                data.putString(ApplicationConstants.BundleKeys.ENCOUNTERTYPE,EncounterType.RISK_FACTORS);
                patientStaticEncounterFragment.setArguments(data);
                new PatientStaticEncounterPresenter(mPatientId, patientStaticEncounterFragment,""); //why?
                return patientStaticEncounterFragment;
            case CHARTS_TAB_POS:
                PatientChartsFragment patientChartsFragment = PatientChartsFragment.newInstance();
                new PatientDashboardChartsPresenter(mPatientId, patientChartsFragment);
                return patientChartsFragment;
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

}