package com.example.demo.Service;

import com.example.demo.Dto.Token.ConfirmationToken;
import com.example.demo.Dto.Token.ConfirmationTokenService;
import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.Repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private  final  static  String USER_NOT_FOUND_MSG =
            "user with email is not found";
private  final AppUserRepository appUserRepository;
private  final BCryptPasswordEncoder bCryptPasswordEncoder;
private final ConfirmationTokenService confirmationTokenService;
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return  appUserRepository.findByEmail(email)
                .orElseThrow(()->
                new UsernameNotFoundException(
                        String.format(USER_NOT_FOUND_MSG,email)));

    }
    public String signUpUser(AppUser appUser){
        boolean userExists=appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();
        if(userExists){
            throw new IllegalStateException("email already taken ");
        }
        String encodedPassword =bCryptPasswordEncoder
                .encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
        //TODO: Send confirmation taken
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken= new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken( confirmationToken);

        //TODO:send email
        return token;
    }


    public void enableAppUser(String email) {
    }
}
