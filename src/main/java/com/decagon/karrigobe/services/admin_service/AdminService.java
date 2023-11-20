package com.decagon.karrigobe.services.admin_service;

import com.decagon.karrigobe.entities.enums.Roles;
import com.decagon.karrigobe.entities.model.UserEntity;
import com.decagon.karrigobe.payload.response.UserPageDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {

    UserPageDTO getAllUsers(int pageNO, int pageSize, String sortBy, String sortDir);
    String disableADriversAccount(Long driverId);
    List<UserEntity> getAllDrivers();
}
