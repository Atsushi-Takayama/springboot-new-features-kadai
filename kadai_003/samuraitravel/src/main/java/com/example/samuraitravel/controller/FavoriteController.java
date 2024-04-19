package com.example.samuraitravel.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.FavoriteRegisterForm;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;

import jakarta.servlet.http.HttpSession;
@Controller
@RequestMapping("/favorites")
public class FavoriteController {
	private final FavoriteRepository favoriteRepository;
	private final FavoriteService favoriteService;
	private final HouseRepository houseRepository;
	
	public FavoriteController(FavoriteRepository favoriteRepository, HouseRepository houseRepository,
			FavoriteService favoriteService) {
		this.favoriteRepository = favoriteRepository;
		this.favoriteService = favoriteService;
		this.houseRepository = houseRepository;
	}
	
	@GetMapping
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC)Pageable pageable, Model model, HttpSession httpSession) {
		User user = userDetailsImpl.getUser();
		Page<Favorite> favoritePage = favoriteRepository.findAll(pageable);
		httpSession.setAttribute("favoriteHouse", houseRepository.findAll());
		
		model.addAttribute("user", user);
		model.addAttribute("favoritePage", favoritePage);
		
		return "favorites/index";
	}
	
	@GetMapping("/{id}/create")
	public String create(@ModelAttribute FavoriteRegisterForm favoriteRegisterForm, @PathVariable(name = "id") Integer id, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
		House house = houseRepository.getReferenceById(id);
		User user = userDetailsImpl.getUser();
		favoriteService.create(favoriteRegisterForm, house, user);
		
		
		return "redirect:/houses/{id}";
	}
	
	@GetMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, FavoriteRegisterForm favoriteRegisterForm, RedirectAttributes redirectAttributes, Model model) {
		Integer userId = userDetailsImpl.getUser().getId();
		favoriteRepository.deleteByHouseIdAndUserId(id, userId);
		return "redirect:/houses/{id}";
	}
}
