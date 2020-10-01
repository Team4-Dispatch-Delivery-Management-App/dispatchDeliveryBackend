package dispatchApp.controller;

import dispatchApp.model.UserOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import dispatchApp.service.OptionService;

@Controller
public class OptionController {

    @Autowired
    private  OptionService optionService;

    //https://localhost:8080/option?from=x&to=x
    @RequestMapping(value = "/option", method = RequestMethod.GET)
    public UserOption[] getUserOptions(@RequestParam("from") String from, @RequestParam("to") String to) {
        return optionService.getUserOptions(from, to);
    }

}
