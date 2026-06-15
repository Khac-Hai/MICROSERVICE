package re.edu.userservice.service.impl;

import org.springframework.stereotype.Service;
import re.edu.userservice.entity.User;
import re.edu.userservice.repository.UserRepository;
import re.edu.userservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}

