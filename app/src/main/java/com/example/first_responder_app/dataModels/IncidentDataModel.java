package com.example.first_responder_app.dataModels;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

import java.util.List;

public class IncidentDataModel {

    @DocumentId
    private String documentId;

    private List<String> units;
    private List<String> responding;
    private Timestamp received_time;
    private boolean incident_complete;
    private String location;
    private String incident_type;

    public IncidentDataModel(List<String> units, List<String> responding, Timestamp received_time, boolean incident_complete, String location, String incident_type) {
        this.units = units;
        this.responding = responding;
        this.received_time = received_time;
        this.incident_complete = incident_complete;
        this.location = location;
        this.incident_type = incident_type;
    }

    public IncidentDataModel() {}

    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public String getDocumentId() { return documentId; }

    public List<String> getUnits() { return units; }

    public List<String> getResponding() { return responding; }

    public Timestamp getReceived_time() { return received_time; }

    public boolean isIncident_complete() { return incident_complete; }

    public String getLocation() { return location; }

    public String getIncident_type() { return incident_type; }
}
