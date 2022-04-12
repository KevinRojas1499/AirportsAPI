package airports;

public class AirportNotFoundException extends Throwable {
    AirportNotFoundException(String s) {
        super("The airport "+s+" was not found");
    }
}
