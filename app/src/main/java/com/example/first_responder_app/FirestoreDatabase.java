package com.example.first_responder_app;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.TextView;

import com.example.first_responder_app.dataModels.AnnouncementsDataModel;
import com.example.first_responder_app.dataModels.EventsDataModel;
import com.example.first_responder_app.dataModels.IncidentDataModel;
import com.example.first_responder_app.dataModels.IncidentTypesDataModel;
import com.example.first_responder_app.dataModels.UsersDataModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreDatabase {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirestoreDatabase instance = new FirestoreDatabase();

    public static FirestoreDatabase getInstance(){
        return instance;
    }

    public FirebaseFirestore getDb(){
        return db;
    }

    public void addEvent(String location, String title, String description, ArrayList<String> participants) {
        EventsDataModel newEvent = new EventsDataModel(title, description, location, participants);


        db.collection("events")
                .add(newEvent)
                .addOnSuccessListener(documentReference -> Log.d("new event page", "new event has been successfully created in the DB"))
                .addOnFailureListener(e -> Log.d("new event page", "failed to create new event"));
    }

    public void addAnnouncement(String title, String description) {

        AnnouncementsDataModel newAnnoun = new AnnouncementsDataModel(title, description);


        db.collection("announcements")
                .add(newAnnoun)
                .addOnSuccessListener(documentReference -> Log.d("new announcement page", "new announcement has been successfully created in the DB"))
                .addOnFailureListener(e ->Log.d("new announcement page", "failed to create new announcement"));
    }

    /**
     * Update the database to show the active user is responding
     *
     * @param user_id The id of the user that is responding
     * @param incident_id The id of the incident that the user is responding to
     * @param status The response that the user gave
     * @param available Whether the user is available or not
     */
    public void responding(String user_id, String incident_id, String status, boolean available){
        if(available){
            db.collection("users").document(user_id).update("is_responding", true);
        }

        db.collection("incident").document(incident_id).get().addOnCompleteListener((typeTask) -> {
            if (typeTask.isSuccessful()) {
                IncidentDataModel incidentDataModel = typeTask.getResult().toObject(IncidentDataModel.class);

                //Update status map
                Map<String, String> statusList = incidentDataModel.getStatus();

                if (statusList == null) {
                    statusList = new HashMap<>();
                }

                if(!statusList.containsKey(user_id)){
                    statusList.put(user_id, status);
                }

                List<String> responding = new ArrayList<>();
                if(available) {
                    //update incident responding list
                    responding = incidentDataModel.getResponding();
                    if (!responding.contains(user_id)) {
                        responding.add(user_id);
                    }
                }

                Map<String, Object> updates = new HashMap<>();
                updates.put("status", statusList);

                if(available) updates.put("responding", responding);
                db.collection("incident").document(incident_id).update(updates);



            } else {
                Log.d(TAG, "Error getting documents: ", typeTask.getException());
            }
        });
    }



}
