package org.emma.catchupperiod.controllerThymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class ControllerLibraryTh {


    @GetMapping("/library/lend")
    public String lend() {
        return "lend";
    }
}
