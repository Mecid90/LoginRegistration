package com.LoginAndRegistration.LoginRegistration.registration;

import com.LoginAndRegistration.LoginRegistration.appuser.AppUser;
import com.LoginAndRegistration.LoginRegistration.appuser.AppUserRole;
import com.LoginAndRegistration.LoginRegistration.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private EmailValidator emailValidator;

    public String register(RegistrationRequest request) {
       boolean isValidEmail = emailValidator.test(request.getEmail());
       if(!isValidEmail){
           throw new IllegalStateException("email not valid");
       }
        return appUserService.signUpUser(new AppUser(1L,
                request.getFirstName(),
                request.getLastName(),request.getEmail(),request.getPassword(), AppUserRole.USER
        ));
    }
}
