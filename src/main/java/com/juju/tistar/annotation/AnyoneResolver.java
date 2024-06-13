package com.juju.tistar.annotation;
import com.juju.tistar.config.AuthorizationExtractor;
import com.juju.tistar.config.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AnyoneResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Anyone.class);
    }

    @Override
    public Long resolveArgument(final MethodParameter parameter,
                                final ModelAndViewContainer mavContainer,
                                final NativeWebRequest webRequest,
                                final WebDataBinderFactory binderFactory) throws Exception {

        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final boolean isAuthenticated = (boolean) request.getAttribute("isAuthenticated");

        if (!isAuthenticated) {
            final UUID sessionId = UUID.fromString(request.getSession().getId());
            final Long accessId = sessionId.getMostSignificantBits();
            return accessId;
        }

        final String token = AuthorizationExtractor.extract(request, "Bearer ");

        return tokenProvider.decodeAccessToken(token);
    }
}