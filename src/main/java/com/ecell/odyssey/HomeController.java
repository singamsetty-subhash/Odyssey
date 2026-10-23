package com.ecell.odyssey;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    public record Stage(int number, String title, String tagline, String description) {}

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("eventName", "Odyssey");
        model.addAttribute("tagline", "Beyond the Horizon");
        model.addAttribute("stages", List.of(
            new Stage(1, "The Departure", "Every journey starts somewhere.",
                "Participants discover the problem, identify an opportunity, and begin their startup journey."),
            new Stage(2, "Into the Unknown", "Ideas meet reality.",
                "Teams face unexpected challenges, market uncertainties, and resource constraints."),
            new Stage(3, "The Trial of Tides", "Adaptation is everything.",
                "Teams pivot, refine their business models, and defend their decisions."),
            new Stage(4, "Beyond the Horizon", "Build what doesn't exist yet.",
                "Final pitches, investor evaluation, and the unveiling of the most promising ventures.")
        ));
        return "index";
    }

    @GetMapping({ "/calendar", "/calander", "/calendar.html", "/calander.html" })
    public String calendar(Model model) {
        model.addAttribute("eventName", "Odyssey");
        model.addAttribute("tagline", "Beyond the Horizon");
        return "calendar";
    }
}
