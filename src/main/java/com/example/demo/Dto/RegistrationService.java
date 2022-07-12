package com.example.demo.Dto;

import com.example.demo.Service.AppUserService;
import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {
    private EmailValidator emailValidator;
    private  final AppUserService appUserService;
    public String register(RegistrationRequest request)
    {
     boolean isValid=emailValidator.test(request.getEmail());
     if(!isValid){
         throw new IllegalStateException("email not valid");
     }
        return appUserService.signUpUser(
                new AppUser (request.getFirstName(),
                               request.getLastName(),
                               request.getEmail(),
                                request.getPassword(),
                                AppUserRole.USER));
    }
}
