package com.ecommerce.user.service.impl;

import com.ecommerce.shared.dto.UserEvent;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import com.ecommerce.user.dto.MessageResponseDto;
import com.ecommerce.user.dto.ResetPasswordRequestDto;
import com.ecommerce.user.dto.UpdateUserDto;
import com.ecommerce.user.dto.UserDto;
import com.ecommerce.user.entity.Profil;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.exception.EmailVerificationException;
import com.ecommerce.user.kafka.KafkaUserProducer;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.repository.ProfilRepository;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfilRepository profilRepository;
    private final KafkaUserProducer kafkaUserProducer;
    private final PasswordEncoder passwordEncoder;

    @Value("${kafka.topic.user.confirmed.name}")
    private String userConfirmedTopicName;

    @Value("${kafka.topic.forgot.password.name}")
    private String userForgotPasswordTopicName;

    @Value("${kafka.topic.reset.password.name}")
    private String userResetPasswordTopicName;

    public UserServiceImpl(UserRepository userRepository, ProfilRepository profilRepository, KafkaUserProducer kafkaUserProducer, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.profilRepository = profilRepository;
        this.kafkaUserProducer = kafkaUserProducer;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return UserMapper.INSTANCE.userToUserDtos(users);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString())
        );
        return UserMapper.INSTANCE.userToUserDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.INSTANCE.userDtoToUser(userDto);
        user.setPassword("password");
        User savedUser = userRepository.save(user);
        return UserMapper.INSTANCE.userToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(Long id, UpdateUserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString())
        );
        user.setId(id);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        if(userDto.getProfilesIds() != null) {
            List<Profil> profiles = profilRepository.findAllById(userDto.getProfilesIds());
            user.setProfils(profiles);
        }
        User savedUser = userRepository.save(user);
        return UserMapper.INSTANCE.userToUserDto(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        getUserById(id);
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public String verifyUserEmail(int verificationCode) {
        Optional<User> optionalUser = userRepository.findByVerificationCode(verificationCode);

        if(optionalUser.isEmpty()) {
            throw new EmailVerificationException("Le code de vérification est incorrect.");
        }

        User user = optionalUser.get();

        if(user.getVerificationCodeExpireAt() != null && user.getVerificationCodeExpireAt().isBefore(LocalDateTime.now())) {
            throw new EmailVerificationException("Le code de verification a expiré.");
        }

        user.setEnabled(true);
        user.setVerified(true);
        userRepository.save(user);

        UserEvent userEvent = new UserEvent(user.getFirstName() + " " + user.getLastName(), user.getEmail(), 0);
        kafkaUserProducer.sendMessage(userEvent, userConfirmedTopicName);
        return user.getFirstName() + " " + user.getLastName();
    }

    @Override
    public MessageResponseDto forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email)
        );

        Random randomCode = new Random();
        int verificationCode = randomCode.nextInt(900000) + 100000;

        user.setVerificationCode(verificationCode);
        user.setVerificationCodeExpireAt(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        UserEvent userEvent = new UserEvent(user.getFirstName() + " " + user.getLastName(), user.getEmail(), verificationCode);
        kafkaUserProducer.sendMessage(userEvent, userForgotPasswordTopicName);
        return new MessageResponseDto("Veuillez vérifié votre boite mail pour réinitialiser votre mot de passe.");
    }

    @Override
    public void updatePassword(Long id, ResetPasswordRequestDto resetPasswordRequestDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString())
        );

        if(!passwordEncoder.matches(resetPasswordRequestDto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Votre mot de passe actuel est incorrect.");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordRequestDto.getNewPassword()));
        userRepository.save(user);

        UserEvent userEvent = new UserEvent(user.getFirstName() + " " + user.getLastName(), user.getEmail(), 0);
        kafkaUserProducer.sendMessage(userEvent, userResetPasswordTopicName);
    }

    @Override
    public String getUserFullName(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString())
        );
        return user.getFirstName() + " " + user.getLastName();
    }

    @Override
    public UserDto subscribeToNewsletter(Long id, String email) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString())
        );
        user.setSubscribed(true);
        user.setSubscriptionEmail(email);
        User savedUser = userRepository.save(user);
        return UserMapper.INSTANCE.userToUserDto(savedUser);
    }

}
