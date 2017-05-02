package edu.ateneo.cie199.tara;

import java.util.ArrayList;

/**
 * Created by Angelo on 4/28/2017.
 */

public class Event {
    private String user = "";
    private int index = 0;
    private String time = "";
    private String venue = "";
    private String category = "";
    private int numPeople = 0;

    public Event(int eIndex, String eVenue, String eTime, String eCategory, int eNumPeople, String eUser) {
        user = eUser;
        index = eIndex;
        time = eTime;
        venue = eVenue;
        category = eCategory;
        numPeople = eNumPeople;
    }

    public String getEventUser() { return user; }
    public int getEventIndex() { return index; }
    public String getEventTime() {return time;}
    public String getEventVenue() {return venue;}
    public String getEventCategory() {return category;}
    public int getNumPeople() {return numPeople;}

    public String toString() { return venue + " at " + time; }
}
