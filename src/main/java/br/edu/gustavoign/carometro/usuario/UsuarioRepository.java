package br.edu.gustavoign.carometro.usuario;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("select e from Usuario e where e.email = :email")
    public Usuario findByEmail(String email);

    @Query("select l from Usuario l where l.user = :user and l.senha = :senha")
    public Usuario buscarLogin(String user, String senha);
    
    @Query("select u from Usuario u where u.user = :user")
    public Usuario findByUser(String user);
    

}
