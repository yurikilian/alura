package br.com.casadocodigo.loja.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.casadocodigo.loja.daos.ProdutoDao;

@Controller
public class HomeController {

  @Autowired
  private ProdutoDao produtoDao;

  @RequestMapping("/")
  @Cacheable(value = "produtosHome")
  public ModelAndView index() {
    ModelAndView modelAndView = new ModelAndView("home");
    modelAndView.addObject("produtos", produtoDao.listar());
    return modelAndView;
  }

}
