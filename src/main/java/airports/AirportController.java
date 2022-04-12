package airports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AirportController {

    //Airport repository
    @Autowired
    private AirportRepository airportRepository;

    //Flights repository
    @Autowired
    private FlightRepository flightRepository;


    /*
     ***********************************************
     * GET
     * *********************************************
     */

    @GetMapping(value = "/airports")
    public List<Airport> getAllFlightsFromAirport(){
        Optional<List<Airport>> airports = Optional.of(airportRepository.findAll());
        List<Airport> airportsList = new ArrayList<Airport>();
        if(airports.isPresent()){
            airportsList = airports.get();
        }
        return airportsList;
    }

    public boolean airportIDValidationMethod(String airportId){
        //Validates if string has 3 letters in uppercase format
        if(airportId.length()!=3){
            return false;
        }
        for(int i = 0; i<airportId.length(); i++){
            char currentLetter = airportId.charAt(i);
            if(Character.isAlphabetic(currentLetter) || !Character.isLowerCase(currentLetter)){
                return false;
            }
        }
        return true;
    }

    @GetMapping(value = "/airport/{airportId}")
    public Airport getAirport(@PathVariable(value="airportId") String airportId){

        if(airportIDValidationMethod(airportId)){
            System.out.println("The airport has the right format");
        }
        Optional<Airport> opAirport = Optional.ofNullable(airportRepository.findByAirportID(airportId));
        Airport currentAirport = new Airport();
        if(opAirport.isPresent()){
            currentAirport = opAirport.get();
        }
        return currentAirport;

    }



    @GetMapping(value = "/flightsArriving/{airportId}")
    public List<Flight> getAllArrivingFlightsToAirport(@PathVariable(value="airportId") String airportId){
        Optional<List<Flight>> flights = Optional.of(flightRepository.findAllBydestinationAirportID(airportId));
        List<Flight> flightsList = new ArrayList<Flight>();
        if(flights.isPresent()){
            flightsList = flights.get();
        }
        return flightsList;

    }




    @GetMapping(value = "/flights/{airportId}")
    public List<Flight> getAllFlightsFromAirport(@PathVariable(value="airportId") String airportId){
        Optional<Airport> currentAirport = Optional.ofNullable(airportRepository.findByAirportID(airportId));
        List<Flight> flightsList = new ArrayList<Flight>();
        if(currentAirport.isPresent()){
            flightsList = currentAirport.get().getDepartureFlights();
        }
        return flightsList;

    }

    @GetMapping(value = "/flight/{flightNumber}")
    public Flight getFlight(@PathVariable(value = "flightNumber") int flightNumber){
        Optional<Flight> opFlight = flightRepository.findById(flightNumber);
        Flight flight = new Flight();
        if(opFlight.isPresent()){
            flight = opFlight.get();
        }
        return flight;
    }

    /*
    ***********************************************
    * DELETE
    * *********************************************
     */

    @DeleteMapping("/airport/{airportId}")
    public String deleteAirportById(@PathVariable(value="airportId")String airportId){
        if(airportRepository.existsById(airportId)){
            Airport airport = airportRepository.findByAirportID(airportId);
            for(Flight flight : airport.getDepartureFlights()){
                deleteFlight(flight.getFlightNumber());
            }
            Optional<List<Flight>> opArrivingFlights = Optional.of(flightRepository.findAllBydestinationAirportID(airportId));
            if(opArrivingFlights.isPresent()){
                for(Flight flight : opArrivingFlights.get()){
                    deleteFlight(flight.getFlightNumber());
                }
            }

            airportRepository.deleteById(airportId);
            return "Deleted successfully";
        }
        else{
            return "There is nothing to delete";
        }

    }

    @DeleteMapping("/airport")
    public String deleteAirport(@RequestBody Airport airport){
        return deleteAirportById(airport.getAirportID());
    }
    @DeleteMapping("/airportsById")
    public String deleteAirportsById(@RequestBody List<String> airportsIds){
        for(String id : airportsIds){
            deleteAirportById(id);
        }
        return "Deleted successfully";
    }

    @DeleteMapping("/airports")
    public String deleteAirports(@RequestBody List<Airport> airports){
        for(Airport airport: airports){
            deleteAirport(airport);
        }
        return "Deleted successfully";
    }


    @DeleteMapping("/flights/{flightNumber}")
    public String deleteFlight(@PathVariable(value = "flightNumber") int flightNumber){
        if(flightRepository.existsById(flightNumber)){
            flightRepository.deleteById(flightNumber);
            return "Deleted successfully";
        }
        else{
            return "There is nothing to delete";
        }
    }

    @DeleteMapping("/flights")
    public String deleteFlight(@RequestBody List<Integer> flightNumbers){
        for(int flightNumber: flightNumbers){
            deleteFlight(flightNumber);
        }
        return "Deleted successfully";
    }

    /*
     ***********************************************
     * POST
     * *********************************************
     */

    @PostMapping("/flight")
    public String addFlight(@RequestBody Flight flight){
        /*
         Adds a flight to the repository, flights with unknown airports are not added

         */
        if(flightRepository.existsById(flight.getFlightNumber())){
            return "This flight ID is already there, use put or delete if you want to change it ";
        }
        String airportID = flight.getOriginAirportID();
        try{
            Airport originAirport = airportRepository.findByAirportID(airportID);
            //If the aiport is not there it will throw an exception

            if(!airportRepository.existsById(flight.getDestinationAirportID())){
                //If we don't find the destination airport
                throw new AirportNotFoundException(flight.getDestinationAirportID());
            }

            originAirport.getDepartureFlights().add(flight);
            flight.setOriginAirport(originAirport);
            flightRepository.save(flight);
            return "The flight was added succesfully";
        }
        catch(Exception e){
            return "The airport "+airportID+" was not found in our repository";

        }
        catch(AirportNotFoundException e){
            return "The airport "+flight.getDestinationAirportID()+" was not found in our repository";
        }
    }

    @PostMapping("/flights")
    public String addFlights(@RequestBody List<Flight> flights ){
        //Adds multiple flights to the repository
        for(Flight flight: flights){
            addFlight(flight);
        }
        return "The flights were added succesfully";


    }

    @PostMapping("/airport/{airportId}")
    public String addAirportById(@PathVariable String airportId){
        //Adds a new airport by its ID
        Airport newAirport = new Airport(airportId);
        return addAirport(newAirport);
    }


    @PostMapping("/airport")
    public String addAirport(@RequestBody Airport newAirport){
        //Adds airports and flights

        if(airportRepository.countAirportByAirportID(newAirport.getAirportID()) == 0){
            Airport airportToInsert = new Airport(newAirport.getAirportID());
            airportRepository.save(airportToInsert);
        }
        else{
            return "This airport was already included, please use put if you want to update";
        }

        if(newAirport.getDepartureFlights()!= null) {
            for (Flight flight : newAirport.getDepartureFlights()) {
                flight.setOriginAirport(newAirport);
            }
            flightRepository.saveAll(newAirport.getDepartureFlights());
        }
        return "The airport was added succesfully";
    }

    @PostMapping("/airports")
    public String addAirports(@RequestBody List<Airport> newAirports){
        //Adds multiple airports
        for(Airport newAirport : newAirports){
            addAirport(newAirport);
        }
        return "The airports were added succesfully";

    }
    @PostMapping("/airportsById")
    public String addAirportsById(@RequestBody List<String> newAirports){
        // Adds multiple airports IDs
        for(String newAirport : newAirports){
            addAirportById(newAirport);
        }
        return "The airports were added succesfully";

    }

    /*
     ***********************************************
     * PUT
     * *********************************************
     */

    @PutMapping("/flightsUpdateDepartureTime/{flightNumber}/{departureTime}")
    public String updateDepartureTime(@PathVariable(value = "flightNumber")int flightNumber,@PathVariable(value = "departureTime")int departureTime){
        // Updates a flight with a new departure time

        Optional<Flight> opFlight = flightRepository.findById(flightNumber);
        Flight flight = new Flight();
        if(opFlight.isPresent()) {
            flight = opFlight.get();
            flight.setDepartureTime(departureTime);
            flightRepository.save(flight);
            return "Update was succesfull";
        }
        else{
            return "The flight is not in our repository";
        }
    }

    @PutMapping("/flightsUpdateArrivalAirport/{flightNumber}/{arrivalAirportId}")
    public String updateArrivalAirport(@PathVariable(value = "flightNumber")int flightNumber,@PathVariable(value = "arrivalAirportId")String arrivalAirportId){
        //Updates a flight with a new arrival airport, does not support adding an unknown airport
        Optional<Flight> opFlight = flightRepository.findById(flightNumber);
        Optional<Airport> opAirport = Optional.ofNullable(airportRepository.findByAirportID(arrivalAirportId));

        if(opFlight.isPresent() && opAirport.isPresent()) {
            Flight flight = opFlight.get();
            flight.setDestinationAirportID(arrivalAirportId);
            flightRepository.save(flight);
            return "Update was succesfull";
        }
        else{
            return "The flight is not in our repository";
        }
    }

}
