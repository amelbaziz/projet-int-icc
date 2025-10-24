package be.icc.pid.reservations_springboot.controller;

import java.util.List;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import be.icc.pid.reservations_springboot.model.Artist;
import be.icc.pid.reservations_springboot.service.ArtistService;
//import ch.qos.logback.core.model.Model;


@Controller
public class ArtistController {
	@Autowired
	ArtistService service;

	@GetMapping("/artists/{id}")
    public String show(Model model, @PathVariable("id") long id) {
	Artist artist = service.getArtist(id);

	model.addAttribute("artist", artist);
	model.addAttribute("title", "Fiche d'un artiste");
		
        return "artist/show";
    }


}

