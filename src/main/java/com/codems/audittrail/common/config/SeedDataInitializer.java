package com.codems.audittrail.common.config;

import com.codems.audittrail.domain.user.model.Role;
import com.codems.audittrail.domain.user.model.User;
import com.codems.audittrail.domain.user.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SeedDataInitializer implements ApplicationRunner {

    private final SeedProperties seedProperties;
    private final AdminProperties adminProperties;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SeedDataInitializer(
            SeedProperties seedProperties,
            AdminProperties adminProperties,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.seedProperties = seedProperties;
        this.adminProperties = adminProperties;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!seedProperties.enabled()) {
            return;
        }

        userRepository.findByEmail(adminProperties.email())
                .orElseGet(() -> userRepository.save(User.of(
                        adminProperties.name(),
                        adminProperties.email(),
                        passwordEncoder.encode(adminProperties.password()),
                        Role.ADMIN
                )));

        userRepository.findByEmail(seedProperties.userEmail())
                .orElseGet(() -> userRepository.save(User.of(
                        "Demo User",
                        seedProperties.userEmail(),
                        passwordEncoder.encode(seedProperties.password()),
                        Role.USER
                )));
    }
}
