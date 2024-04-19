package com.example.samuraitravel.form;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class FavoriteRegisterForm {
	@NotNull
	private Integer houseId;
	
	@NotNull
	private Integer userId;
	
	
	@Transactional
	public void deleteByHouseIdAndUserId(Integer houseId, Integer userId) {
		this.houseId = houseId;
		this.userId = userId;
	}
}
