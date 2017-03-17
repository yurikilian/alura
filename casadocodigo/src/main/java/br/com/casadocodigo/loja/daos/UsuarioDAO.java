package br.com.casadocodigo.loja.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.com.casadocodigo.loja.models.Usuario;

@Repository
public class UsuarioDAO implements UserDetailsService {

  @PersistenceContext
  private EntityManager entityManager;


  @Override
  public Usuario loadUserByUsername(String email) {
    List<Usuario> usuarios =
        entityManager.createQuery("select u from Usuario u where u.email = :email", Usuario.class)
            .setParameter("email", email).getResultList();

    if (usuarios.isEmpty()) {
      throw new UsernameNotFoundException("Usuário - " + email + " não foi encontrado");
    }

    return usuarios.get(0);
  }



}
