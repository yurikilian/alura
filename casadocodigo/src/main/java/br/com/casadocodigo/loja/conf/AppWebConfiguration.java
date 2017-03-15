package br.com.casadocodigo.loja.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.google.common.cache.CacheBuilder;

import br.com.casadocodigo.loja.controllers.HomeController;
import br.com.casadocodigo.loja.daos.ProdutoDao;
import br.com.casadocodigo.loja.infra.FileSaver;
import br.com.casadocodigo.loja.models.CarrinhoCompras;

@EnableWebMvc
@ComponentScan(basePackageClasses = {HomeController.class, ProdutoDao.class, FileSaver.class,
    CarrinhoCompras.class})
@EnableCaching
public class AppWebConfiguration extends WebMvcConfigurerAdapter {

  @Bean
  public InternalResourceViewResolver internalResourceViewResolver() {
    final InternalResourceViewResolver resolver = new InternalResourceViewResolver();
    resolver.setPrefix("/WEB-INF/views/");
    resolver.setSuffix(".jsp");
    resolver.setExposedContextBeanNames("carrinhoCompras");
    return resolver;
  }

  @Bean
  public MessageSource messageSource() {
    final ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("/WEB-INF/messages");
    messageSource.setDefaultEncoding("ISO-8859-1");
    messageSource.setCacheSeconds(15);
    return messageSource;
  }

  @Bean
  public FormattingConversionService mvcConversionService() {
    DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
    DateFormatterRegistrar dateFormatterRegistrar = new DateFormatterRegistrar();
    dateFormatterRegistrar.setFormatter(new DateFormatter("dd/MM/yyyy"));
    dateFormatterRegistrar.registerFormatters(conversionService);
    return conversionService;
  }

  @Bean
  public MultipartResolver multipartResolver() {
    return new StandardServletMultipartResolver();
  }

  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public CacheManager cacheManager() {
    CacheBuilder<Object, Object> cacheBuilder =
        CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(3, TimeUnit.MINUTES);
    GuavaCacheManager cacheManager = new GuavaCacheManager();
    cacheManager.setCacheBuilder(cacheBuilder);
    return cacheManager;
  }


  @Bean
  public ViewResolver contentNegotationViewResolver(ContentNegotiationManager manager) {
    List<ViewResolver> viewResolvers = new ArrayList<ViewResolver>();
    viewResolvers.add(internalResourceViewResolver());
    viewResolvers.add(new JsonViewResolver());

    ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
    resolver.setViewResolvers(viewResolvers);
    resolver.setContentNegotiationManager(manager);
    return resolver;
  }



}
