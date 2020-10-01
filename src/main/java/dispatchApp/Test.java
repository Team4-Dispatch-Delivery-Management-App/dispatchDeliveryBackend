package dispatchApp;
import dispatchApp.external.GoogleMapAPIClient;
import dispatchApp.dao.CarrierDao;
import dispatchApp.model.Option;
import dispatchApp.model.Route;
import dispatchApp.model.UserOption;
import dispatchApp.service.OptionService;


public class Test {

    public static void main(String[] args) {
        Test test = new Test();
        GoogleMapAPIClient googleMapAPIClient = new GoogleMapAPIClient();
        final String DEFAULT_STATION = "San Francisco";
        String start = "San Jose";
        String end = "710 Florida St, San Francisco, CA 94110";
//        Route stationToStart = googleMapAPIClient.getRobotRoute(DEFAULT_STATION, start);
//        Route startToEnd = googleMapAPIClient.getRobotRoute(start, end);
//        Route endToStation = googleMapAPIClient.getRobotRoute(end, DEFAULT_STATION);
//        System.out.println(stationToStart.toString());
//        System.out.println(startToEnd.toString());
//        System.out.println(endToStation.toString());
        OptionService optionService = new OptionService();
        UserOption[] options = optionService.getUserOptions(start, end);
        for (UserOption option : options) {
            System.out.println(option.toString());
        }

    }
}
