package com.LoginAndRegistration.LoginRegistration.appuser;

import com.LoginAndRegistration.LoginRegistration.registration.token.ConfirmationToken;
import com.LoginAndRegistration.LoginRegistration.registration.token.ConfirmationTokenService;
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
// UserDetailsService is how we find user when we try to login
public class AppUserService implements UserDetailsService {

    private final static String USER_NAME_NOT_FOUND_MSG = "User with email %s not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(
                        String.format(USER_NAME_NOT_FOUND_MSG,email)));
    }

    public String signUpUser(AppUser appUser) {
        boolean userExist = appUserRepository.findByEmail(appUser.getEmail())
                .isPresent();
        if (userExist) {
            throw new IllegalStateException("user is already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());

        appUserRepository.save(appUser);

        appUser.setPassword(encodedPassword);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        //TODO: Send email

        return token;
    }
}

