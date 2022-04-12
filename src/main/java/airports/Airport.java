package airports;

import javax.persistence.*;
import java.util.*;


@Entity
public class Airport {


    @Id
    private String airportID; //Unique identifier for Airports


    @OneToMany(mappedBy = "originAirport")
    private List<Flight> departureFlights = new ArrayList<Flight>(); //Stores a list of flights

    /*
    Constructors
     */

    public Airport(){}

    public Airport(String airportID) {
        this.airportID = airportID;
    }

    public Airport(String airportID, List<Flight> departureFlights) {
        this.airportID = airportID;
        this.departureFlights = departureFlights;
    }

    /*
    Getters and Setters
     */


    public List<Flight> getDepartureFlights() {
        return departureFlights;
    }

    public void setDepartureFlights(List<Flight> flights) {
        this.departureFlights = flights;
    }

    public String getAirportID() {
        return airportID;
    }

    public void setAirportID(String airportID) {
        this.airportID = airportID;
    }

}

