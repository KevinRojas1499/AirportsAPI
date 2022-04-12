package airports;

public class FlightNotFoundException extends RuntimeException {


    FlightNotFoundException(int id) {
        super("Couldn't find this flight" + id);
    }
}
