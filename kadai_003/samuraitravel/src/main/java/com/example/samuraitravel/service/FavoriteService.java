package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.FavoriteRegisterForm;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.UserRepository;
@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final HouseRepository houseRepository;
	private final UserRepository userRepository;
	
	public FavoriteService(FavoriteRepository favoriteRepository, HouseRepository houseRepository,
			UserRepository userRepository) {
		this.favoriteRepository = favoriteRepository;
		this.houseRepository = houseRepository;
		this.userRepository = userRepository;
	}
	
	@Transactional
	public void create(FavoriteRegisterForm favoriteRegisterForm, House house, User user) {
		Favorite favorite = new Favorite();
		
		favoriteRegisterForm.setHouseId(house.getId());
		favoriteRegisterForm.setUserId(user.getId());
		
		favorite.setHouse(houseRepository.getReferenceById(favoriteRegisterForm.getHouseId()));
		favorite.setUser(userRepository.getReferenceById(favoriteRegisterForm.getUserId()));
		
		favoriteRepository.save(favorite);
	}
	
	@Transactional
	public boolean isFavoritedHouseAndFavoritedUser(House house, User user) {		
		Favorite isFavorited = favoriteRepository.getByHouseIdAndUserId(house.getId(), user.getId());
		
		if (isFavorited != null) {
				return true;
		}
		
		return false;
	}

}
