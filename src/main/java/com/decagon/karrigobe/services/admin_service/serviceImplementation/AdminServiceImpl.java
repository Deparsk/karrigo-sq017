package com.decagon.karrigobe.services.admin_service.serviceImplementation;

import com.decagon.karrigobe.entities.model.UserEntity;
import com.decagon.karrigobe.exceptions.UserNotFoundException;
import com.decagon.karrigobe.payload.response.UserPageDTO;
import com.decagon.karrigobe.payload.response.UserResponse;
import com.decagon.karrigobe.repositories.UserRepository;
import com.decagon.karrigobe.services.admin_service.AdminService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.decagon.karrigobe.entities.enums.RecordStatusConstant.ACTIVE;
import static com.decagon.karrigobe.entities.enums.RecordStatusConstant.INACTIVE;
import static com.decagon.karrigobe.entities.enums.Roles.DRIVER;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ModelMapper mapper = new ModelMapper();
    private final UserRepository userRepository;


    @Override
    public UserPageDTO getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<UserEntity> userPage = userRepository.findAll(pageable);

        List<UserEntity> userEntities = userPage.getContent();
        List<UserResponse>  userResponses = new ArrayList<>();
        for (UserEntity user : userEntities){
            userResponses.add(mapper.map(user,UserResponse.class));
        }
        return UserPageDTO.builder()
                .pageNo(userPage.getNumber())
                .pageSize(userPage.getSize())
                .totalPage(userPage.getTotalPages())
                .lastPage(userPage.isLast())
                .userResponseList(userResponses)
                .build();
    }

    @Override
    public List<UserEntity> getAllDrivers() {
        return userRepository.findByRoles(DRIVER);
    }

    @Override
    public String disableADriversAccount(Long driverID) {
        UserEntity driver = userRepository.findById(driverID)
                .orElseThrow(()-> new UserNotFoundException("user does not exist"));

        if (driver.getRecordStatus().equals(ACTIVE)){
            driver.setRecordStatus(INACTIVE);
            userRepository.save(driver);
            return "Driver's account has been disabled";

        }return "Driver's account is already disabled";
    }



}
