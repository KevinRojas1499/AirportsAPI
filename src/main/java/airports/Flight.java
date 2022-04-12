package airports;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;


@Entity
public class Flight {
    @Id
    private int flightNumber;

    @ManyToOne
    @JoinColumn(name = "departureFlights")
    @JsonBackReference
    private Airport originAirport;       //Airport to create the connections in the database

    private String originAirportID;      //Airport where it departs from
    private String destinationAirportID; //Airport where it is arriving
    private int departureTime;           //Time is given in minutes


    /*
       Constructors
     */
    public Flight() {}

    public Flight(Airport airport, String destinationAirportID) {
        this.originAirport = airport;
        this.originAirportID = airport.getAirportID();
        this.destinationAirportID = destinationAirportID;
    }

    public Flight(int flightNumber, Airport airport, String destinationAirportID, int departureTime) {
        this.flightNumber = flightNumber;
        this.originAirport = airport;
        this.originAirportID = airport.getAirportID();
        this.destinationAirportID = destinationAirportID;
        this.departureTime = departureTime;
    }

    /*
    Getters and Setters
     */

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Airport getOriginAirport() {
        return originAirport;
    }

    public void setOriginAirport(Airport originAirport) {
        this.originAirport = originAirport;
        this.originAirportID = originAirport.getAirportID();
    }

    public String getOriginAirportID() {
        return originAirportID;
    }

    public void setOriginAirportID(String originAirportID) {
        this.originAirportID = originAirportID;
    }

    public String getDestinationAirportID() {
        return destinationAirportID;
    }

    public void setDestinationAirportID(String destinationAirportID) {
        this.destinationAirportID = destinationAirportID;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(int departureTime) {
        this.departureTime = departureTime;
    }
}


