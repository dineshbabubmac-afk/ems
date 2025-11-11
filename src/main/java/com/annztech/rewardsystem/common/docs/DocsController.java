package com.annztech.rewardsystem.common.docs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocsController {

    @GetMapping("/redoc")
    public String redoc() {
        return "redoc";
    }

    @GetMapping("/docs")
    public String docs() {
        return "redoc";
    }

    @GetMapping("/new-doc")
    public String newDoc() {
        return "interactive-redoc";
    }

    //static
    //http://localhost:8080/api/v1/rapidoc.html

}
