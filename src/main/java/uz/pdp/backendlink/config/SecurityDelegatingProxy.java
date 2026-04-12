package uz.pdp.backendlink.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityDelegatingProxy extends AbstractSecurityWebApplicationInitializer {

    @Override
    protected boolean isAsyncSecuritySupported() {
        return true;
    }

    @Override
    protected boolean enableHttpSessionEventPublisher() {
        return true;
    }

}