package com.codems.audittrail.config;

import com.codems.audittrail.common.constants.ApplicationConstants;
import com.codems.audittrail.common.security.model.SecurityUser;
import com.codems.audittrail.common.util.ApplicationUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAwareImpl")
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return ApplicationUtility.getLoggedInUser()
                .map(SecurityUser::getUsername)
                .or(() -> Optional.of(ApplicationConstants.SYSTEM));
    }
}
