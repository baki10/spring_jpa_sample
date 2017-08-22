package com.bakigoal.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import java.io.File;

/**
 * This class will replace web.xml and it will map the spring’s dispatcher servlet
 */
public class WebServletConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

  @Override
  protected String[] getServletMappings() {
    return new String[]{"/"};
  }

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return null;
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class[]{SpringMvcConfig.class};
  }

  @Override
  protected void customizeRegistration(ServletRegistration.Dynamic registration) {
    // upload temp file will put here
    File uploadDirectory = new File(System.getProperty("java.io.tmpdir"));
    // register a MultipartConfigElement
    int maxUploadSizeInMb = 5 * 1024 * 1024;
    MultipartConfigElement multipartConfigElement = new MultipartConfigElement(uploadDirectory.getAbsolutePath(),
        maxUploadSizeInMb, maxUploadSizeInMb * 2, maxUploadSizeInMb / 2);
    registration.setMultipartConfig(multipartConfigElement);
  }

}
