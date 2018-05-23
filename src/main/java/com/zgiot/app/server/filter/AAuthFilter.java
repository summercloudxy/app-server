package com.zgiot.app.server.filter;


import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.pojo.CurrentUser;
import com.zgiot.common.pojo.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@WebFilter(urlPatterns = "/*", filterName = "AAuthFilter")
public class AAuthFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        /**
         * 如果header中包含userId, 则查询当前用户的基本信息, 然后放在当前会话(ThreadLocal)中
         */
        HttpServletRequest hsRequest = (HttpServletRequest) request;

        //从 API 网关接受当前用户id
        String userId = hsRequest.getHeader(GlobalConstants.USER_ID);
        String userUuid = hsRequest.getHeader(GlobalConstants.USER_UUID);
        String requestId = hsRequest.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        String userLoginame = hsRequest.getHeader(GlobalConstants.USER_LOGINNAME);

        logger.trace("visit userId is {} , userUuid is {},requestId is {}", userId, userUuid, requestId);


        //用户获取数据成功
        CurrentUser currentUser = new CurrentUser();

        currentUser.setUserId(userId);
        currentUser.setUserUuid(userUuid);
        currentUser.setUserLoginName(userLoginame);
        currentUser.setRequestId(requestId);


        SessionContext.setSession(currentUser);


        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        //咱不用处理
    }


}
