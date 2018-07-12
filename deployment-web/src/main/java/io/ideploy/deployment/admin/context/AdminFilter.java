package io.ideploy.deployment.admin.context;

import io.ideploy.deployment.common.util.IpAddressUtils;
import javax.servlet.annotation.WebFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 管理系统的 filter
 * @author ten 2015/10/6.
 */
@Component
@WebFilter(urlPatterns = { "/*" },filterName = "adminFilter")
public class AdminFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AdminFilter.class);

    // 排除的URL，不需要登录
    private static final Set<String> EXCLUDE_URL = new HashSet<>();

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        try {

            // set client IP
            String ip = IpAddressUtils.getClientIpAddr(request);
            String p = getURI(request);


            if (logger.isDebugEnabled()) {
                String pathInfo = getURI(request);
                logger.debug("request: {}, ip: {}", pathInfo, ip);
            }

            AdminContext.loadFromCookie(request);

            chain.doFilter(req, resp);

        } finally {
            // clean private data
            AdminContext.clear();
        }
    }

    /**
     * 获取系统资源地址
     *
     * @param request
     */
    private String getURI(HttpServletRequest request) {
        UrlPathHelper helper = new UrlPathHelper();
        String uri = helper.getOriginatingRequestUri(request);
        String ctxPath = helper.getOriginatingContextPath(request);

        return uri.replaceFirst(ctxPath, "");
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        EXCLUDE_URL.add("/pages/login.html");
        EXCLUDE_URL.add("/admin/login.do");
        EXCLUDE_URL.add("/admin/welcome.xhtml");
    }

    protected boolean isExcludePath(String path) {
        if (path != null) {
            if (EXCLUDE_URL.contains(path) || path.startsWith("/include/")) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        EXCLUDE_URL.clear();
    }
}
