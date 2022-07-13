package com.example.demo.Dto;

import com.example.demo.Dto.Token.ConfirmationToken;
import com.example.demo.Service.AppUserService;
import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserRole;
import email.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private EmailValidator emailValidator;
    private  final AppUserService appUserService;
    private final ConfirmationToken confirmationTokenService;
    private final EmailSender emailSender;

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
//     emailSender.send(request.getEmail(),);
    }
@Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken=confirmationTokenService
                .getToken(token).orElseThrow(()->new IllegalStateException("token not found"));
        if(confirmationToken.getConfirmedAt()!=null){
            throw new IllegalStateException("email already confirmed");
        }
    LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("token expired");
        }
        confirmationTokenService.setConfirmedAt(LocalDateTime.parse(token));
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());

    return "confirmed";
    }
}
