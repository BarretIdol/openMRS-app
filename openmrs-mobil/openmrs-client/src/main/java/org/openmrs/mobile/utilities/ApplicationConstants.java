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

package org.openmrs.mobile.utilities;

import org.openmrs.mobile.models.EncounterType;

public abstract class ApplicationConstants {

    public static final String EMPTY_STRING = "";
    public static final String SERVER_URL = "server_url";
    public static final String SESSION_TOKEN = "session_id";
    public static final String AUTHORIZATION_TOKEN = "authorisation";
    public static final String SECRET_KEY = "secretKey";
    public static final String LOCATION = "location";
    public static final String VISIT_TYPE_UUID = "visit_type_uuid";
    public static final String LAST_SESSION_TOKEN = "last_session_id";
    public static final String LAST_LOGIN_SERVER_URL = "last_login_server_url";
    // public static final String DEFAULT_OPEN_MRS_URL = "https://demo.openmrs.org/openmrs";
    public static final String DEFAULT_OPEN_MRS_URL = "http://147.83.49.47:8081/openmrs-standalone";

    public abstract static class OpenMRSSharedPreferenceNames {
        public static final String SHARED_PREFERENCES_NAME = "shared_preferences";
    }

    public abstract static class API {
        public static final String REST_ENDPOINT = "/ws/rest/v1/";
        public static final String FULL = "full";
    }

    public abstract static class UserKeys {
        public static final String USER_NAME = "username";
        public static final String PASSWORD = "password";
        public static final String USER_PERSON_NAME = "userDisplay";
        public static final String USER_UUID = "userUUID";
        public static final String LOGIN = "login";
    }

    public abstract static class DialogTAG {
        public static final String LOGOUT_DIALOG_TAG = "logoutDialog";
        public static final String END_VISIT_DIALOG_TAG = "endVisitDialogTag";
        public static final String START_VISIT_DIALOG_TAG = "startVisitDialog";
        public static final String START_VISIT_IMPOSSIBLE_DIALOG_TAG = "startVisitImpossibleDialog";
        public static final String WARNING_LOST_DATA_DIALOG_TAG = "warningLostDataDialog";
        public static final String SIMILAR_PATIENTS_TAG = "similarPatientsDialogTag";
        public static final String DELET_PATIENT_DIALOG_TAG = "deletePatientDialogTag";
    }

    public abstract static class RegisterPatientRequirements {
        public static final int MAX_PATIENT_AGE = 120;
    }

    public abstract static class BundleKeys {
        public static final String CUSTOM_DIALOG_BUNDLE = "customDialogBundle";
        public static final String PATIENT_ID_BUNDLE = "patientID";
        public static final String VISIT_ID = "visitID";
        public static final String ENCOUNTERTYPE = "encounterType";
        public static final String VALUEREFERENCE = "valueReference";
        public static final String FORM_NAME = "formName";
        public static final String CALCULATED_LOCALLY = "CALCULATED_LOCALLY";
        public static final String PATIENTS_AND_MATCHES = "PATIENTS_AND_MATCHES";
        public static final String FORM_FIELDS_BUNDLE = "formFieldsBundle";
        public static final String FORM_FIELDS_LIST_BUNDLE = "formFieldsListBundle";
        public static final String PATIENT_QUERY_BUNDLE = "patientQuery";
        public static final String PATIENTS_START_INDEX = "patientsStartIndex";
        public static final String ENCOUNTER_ID= "encounter_id";
        public static final java.lang.String ENCOUNTER_SYNC = "encounter_sync";
        public static final String GO_TO_ENCOUNTER_TAB = "goToEncounterTab";
    }

    public abstract static class ServiceActions {
        public static final String START_CONCEPT_DOWNLOAD_ACTION = "com.openmrs.mobile.services.conceptdownloadservice.action.startforeground";
        public static final String STOP_CONCEPT_DOWNLOAD_ACTION = "com.openmrs.mobile.services.conceptdownloadservice.action.stopforeground";
    }

    public abstract static class BroadcastActions {
        public static final String CONCEPT_DOWNLOAD_BROADCAST_INTENT_ID = "com.openmrs.mobile.services.conceptdownloadservice.action.broadcastintent";
        public static final String CONCEPT_DOWNLOAD_BROADCAST_INTENT_KEY_COUNT = "com.openmrs.mobile.services.conceptdownloadservice.broadcastintent.key.count";
    }

    public abstract static class ServiceNotificationId {
        public static final int CONCEPT_DOWNLOADFOREGROUND_SERVICE = 101;
    }

    public abstract static class SystemSettingKeys {
        public static final String WS_REST_MAX_RESULTS_ABSOLUTE = "webservices.rest.maxResultsAbsolute";
    }

    public abstract static class ConceptUuids {
        public static final String BMI = "87b4123c-998a-4306-80dd-60d310b30c62";
        public static final String PULSE = "5087AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String SYSTOLIC = "5085AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String DIASTOLIC = "5086AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    }

    public abstract static class EncounterTypes {
        public static final String VITALS = "67a71486-1a54-468f-ac3e-7091a9a79584";
        public static final String VITALSBPUP = "d9bf9142-f315-4079-9190-bc6f9807d9dd"; // added by hector
        public static final String PERSONAL_DATA = "abf0c631-a818-412d-af7f-8cc000dddb4e";
        public static final String MED_BACKGROUND = "8c5674c8-26ed-40ac-9b08-1bc22b823784";
        public static final String RISK_FACTORS  = " 98ff6a0b-c758-4975-bd38-271cebfc2de9 ";
        //public static final String VITALS = "40fd285b-c6f8-4862-944f-5603645fc749";

        public static String[] ENCOUNTER_TYPES_DISPLAYS = {EncounterType.VITALS, EncounterType.ADMISSION,
                EncounterType.DISCHARGE, EncounterType.VISIT_NOTE, EncounterType.VITALS_PRES, EncounterType.PHARMACOLOGICAL, EncounterType.MED_BACKGROUND, EncounterType.TEST_ADHERENCE,EncounterType.PERSONAL_DATA, EncounterType.RISK_FACTORS};
    }

}
