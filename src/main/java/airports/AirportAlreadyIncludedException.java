package airports;

public class AirportAlreadyIncludedException extends Throwable{

    AirportAlreadyIncludedException(String s) {
        super("The airport "+s+" is already included");
    }
}
