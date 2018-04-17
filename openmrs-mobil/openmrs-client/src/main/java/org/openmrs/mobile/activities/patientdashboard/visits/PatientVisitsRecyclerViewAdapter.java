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

package org.openmrs.mobile.activities.patientdashboard.visits;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.formview.FormViewActivity;
import org.openmrs.mobile.application.OpenMRS;
import org.openmrs.mobile.models.Encounter;
import org.openmrs.mobile.models.EncounterType;
import org.openmrs.mobile.models.Encountercreate;
import org.openmrs.mobile.utilities.ApplicationConstants;
import org.openmrs.mobile.utilities.DateUtils;
import org.openmrs.mobile.utilities.FontsUtil;

import java.util.ArrayList;
import java.util.List;

import static org.openmrs.mobile.activities.patientdashboard.visits.PatientVisitsFragment.REQUEST_CODE_FOR_VISIT;

public class PatientVisitsRecyclerViewAdapter extends RecyclerView.Adapter<PatientVisitsRecyclerViewAdapter.EncounterViewHolder> {
    private PatientVisitsFragment mContext;
    private List<Object> mData = new ArrayList<>();


    public PatientVisitsRecyclerViewAdapter(PatientVisitsFragment context, List<Encounter> items, List<Encountercreate> encountercreate) { // modified
        this.mContext = context;
        mData.addAll(encountercreate);
        mData.addAll(items);
    }

    @Override
    public EncounterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_visit_row, parent, false);
        FontsUtil.setFont((ViewGroup) itemView);
        return new EncounterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EncounterViewHolder encounterViewHolder, final int position) {
        final int adapterPos = encounterViewHolder.getAdapterPosition();

        if (mData.get(adapterPos) instanceof Encounter) {
            Encounter encounter = (Encounter) mData.get(adapterPos);
            encounterViewHolder.mEncounterDate.setText(encounter.getDisplay());
            encounterViewHolder.mEncounterDate.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            encounterViewHolder.mEncounterPlace.setText(" ");
            if (encounter.getLocation() !=  null) {
                encounterViewHolder.mEncounterPlace.setText("in " + encounter.getLocation().getDisplay());
            }
            else {
                encounterViewHolder.mEncounterPlace.setText("in Unknown Location");
            }

        } else if (mData.get(adapterPos) instanceof  Encountercreate){
            Encountercreate encounterCreate = (Encountercreate) mData.get(adapterPos);
            // TO DO EncounterCreate hasn't encounterDate
            encounterViewHolder.mEncounterDate.setText(encounterCreate.getDisplay());
            encounterViewHolder.mEncounterPlace.setText("in " + OpenMRS.getInstance().getLocation());
            //encounterViewHolder.mEncounterDate.setText(encounterCreate.getEncounterType());
            encounterViewHolder.mEncounterDate.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext.getContext(), R.drawable.past_visit_dot), null, null, null);
        }

       /* if (DateUtils.convertTime(visit.getStopDatetime()) != null) {
            visitViewHolder.mVisitEnd.setVisibility(View.VISIBLE);
            visitViewHolder.mVisitEnd.setText(DateUtils.convertTime1((visit.getStopDatetime()), DateUtils.DATE_WITH_TIME_FORMAT));

            Drawable icon = mContext.getResources().getDrawable(R.drawable.past_visit_dot);
            icon.setBounds(0, 0, icon.getIntrinsicHeight(), icon.getIntrinsicWidth());
            visitViewHolder.mVisitStatus.setCompoundDrawables(icon, null, null, null);
            visitViewHolder.mVisitStatus.setText(mContext.getString(R.string.past_visit_label));
        } else {
            visitViewHolder.mVisitEnd.setVisibility(View.INVISIBLE);
            Drawable icon = mContext.getResources().getDrawable(R.drawable.active_visit_dot);
            icon.setBounds(0, 0, icon.getIntrinsicHeight(), icon.getIntrinsicWidth());
            visitViewHolder.mVisitStatus.setCompoundDrawables(icon, null, null, null);
            visitViewHolder.mVisitStatus.setText(mContext.getString(R.string.active_visit_label));
        }
        if (visit.getLocation() != null) {
            visitViewHolder.mVisitPlace.setText(mContext.getString(R.string.visit_in, visit.getLocation().getDisplay()));
        }*/

        encounterViewHolder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.get(adapterPos) instanceof Encounter) {
                    Encounter encounter = (Encounter) mData.get(adapterPos);
                    mContext.enc = encounter;
                    mContext.goToVisitDashboard(encounter.getId(), true);
                }
                else{
                    Encountercreate encountercreate = (Encountercreate)mData.get(adapterPos);
                    mContext.goToVisitDashboard(encountercreate.getId(), false);
                }
            }
        });
    }

    @Override
    public void onViewDetachedFromWindow(EncounterViewHolder holder) {
        holder.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class EncounterViewHolder extends RecyclerView.ViewHolder{
        private TextView mEncounterPlace;
        private TextView mEncounterDate;
        //private TextView mVisitEnd;
        //private TextView mVisitStatus;
        private RelativeLayout mRelativeLayout;

        public EncounterViewHolder(View itemView) {
            super(itemView);
            mRelativeLayout = (RelativeLayout) itemView;
            mEncounterDate = (TextView) itemView.findViewById(R.id.patientVisitStartDate);
            //mVisitEnd = (TextView) itemView.findViewById(R.id.patientVisitEndDate);
            mEncounterPlace = (TextView) itemView.findViewById(R.id.patientVisitPlace);
            //mVisitStatus = (TextView) itemView.findViewById(R.id.visitStatusLabel);
        }
        public void clearAnimation() {
            mRelativeLayout.clearAnimation();
        }
    }
}


