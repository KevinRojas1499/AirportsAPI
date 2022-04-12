package airports;

import org.springframework.data.jpa.repository.JpaRepository;
/*
 * A repository for airport
 * */
public interface AirportRepository extends JpaRepository<Airport,String> {

    public Airport findByAirportID(String airportId);
    public int countAirportByAirportID(String airportId);

}
