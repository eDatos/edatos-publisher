package es.gobcan.istac.edatos.publisher;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

public class ApiLatestURLRewriteFilter extends UrlRewriteFilter {

    @Override
    protected void loadUrlRewriter(FilterConfig filterConfig) throws ServletException {
        try {
            ClassPathResource resource = new ClassPathResource("urlrewrite.xml");
            checkConf(new Conf(filterConfig.getServletContext(), resource.getInputStream(), resource.getFilename(), StringUtils.EMPTY));
        } catch (IOException e) {
            throw new ServletException("Unable to load URL-rewrite configuration file from classpath", e);
        }
    }
}
