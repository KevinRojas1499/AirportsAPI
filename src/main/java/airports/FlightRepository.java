package airports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*
* A repository for flight
* */
public interface FlightRepository extends JpaRepository<Flight,Integer> {

    public Flight findByFlightNumber(int flightNumber);
    public List<Flight> findAllBydestinationAirportID(String arrivingId);



}
