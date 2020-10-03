package dispatchApp.controller;

import dispatchApp.dao.CarrierDao;
import dispatchApp.model.UserOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import dispatchApp.service.OptionService;
@CrossOrigin("*")
@Controller
public class CarrierController {
    //only use this to create dummy carrier
    //it takes a while to add carriers
    @Autowired
    private CarrierDao carrierDao;

    @RequestMapping(value = "/addcarrier", method = RequestMethod.GET)
    public void addDummyCarrier() {
        carrierDao.addDummyCarrier();
    }

}
