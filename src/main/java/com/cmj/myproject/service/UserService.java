package com.cmj.myproject.service;


import com.cmj.myproject.domain.User;
import com.cmj.myproject.dto.UserForm;
import com.cmj.myproject.exception.DuplicateEmailException;
import com.cmj.myproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void signUpUser(UserForm userForm) {
        validateDuplicateEmail(userForm.getEmail());
        userRepository.save(User.createUser(userForm));
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
    }

}
