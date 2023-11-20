package com.decagon.karrigobe.controllers.admin_controller;

import com.decagon.karrigobe.entities.model.UserEntity;
import com.decagon.karrigobe.payload.response.ApiResponse;
import com.decagon.karrigobe.payload.response.UserPageDTO;
import com.decagon.karrigobe.services.admin_service.AdminService;
import com.decagon.karrigobe.commons.PageConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdminUserController {

   private final AdminService adminService;

    @GetMapping("/user-page")
    private ResponseEntity<UserPageDTO> getAllUsersByAdmin(
            @RequestParam(value = "pageNo", defaultValue = PageConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PageConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = PageConstant.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PageConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        UserPageDTO userPageDTO = adminService.getAllUsers(pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity.ok(userPageDTO);
    }
    @GetMapping("/get_all_drivers")
    public List<UserEntity> getAllDrivers(){
        return adminService.getAllDrivers();
    }

    @PostMapping("/driver_record_status/{driverId}")
    public ResponseEntity<ApiResponse<String>> adminToDisableADriver(@PathVariable("driverId") Long driverId){
        return ResponseEntity.ok(new ApiResponse<>(adminService.disableADriversAccount(driverId)));
    }
}
