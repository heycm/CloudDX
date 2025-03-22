package cn.heycm.common.web;

import cn.heycm.common.entity.constant.AppConstant;
import cn.heycm.common.entity.error.Assert;
import cn.heycm.common.entity.error.CodeMsg;
import cn.heycm.common.entity.session.Session;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Session 解析器，在控制层方法参数中自动获取当前请求的 Session 对象，用法类似 HttpServletRequest
 * @author heycm
 * @version 1.0
 * @since 2024-11-16 13:56
 */
public class SessionResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Session.class == parameter.getParameterType();
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        // 获取请求头中的 token
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = request.getHeader(AppConstant.TOKEN_HEADER);
        Assert.notBlank(token, CodeMsg.ERROR_AUTHENTICATION);
        token = token.replace(AppConstant.TOKEN_PREFIX, "");
        // 从 Redis 中获取 Session
        Session session = new Session();
        session.setUserId(1);
        session.setRoleId(1);
        return session;
    }
}
