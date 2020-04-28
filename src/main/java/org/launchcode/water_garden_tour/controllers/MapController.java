package org.launchcode.water_garden_tour.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MapController {

    @RequestMapping("map")
    public String map(Model model) {

        model.addAttribute("title","Kansas City Water Garden Tours");
        return "map";
    }

}