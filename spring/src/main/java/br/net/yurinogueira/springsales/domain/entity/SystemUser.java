package br.net.yurinogueira.springsales.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "systemuser")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "login")
    @NotEmpty(message = "{error.user.login-needed}")
    private String login;

    @Column(name = "password")
    @NotEmpty(message = "{error.user.password-needed}")
    private String password;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToMany
    private List<Role> roles;

    public String[] getRoles() {
        return roles.stream().map(Role::getAuthority).toArray(String[]::new);
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public void addRole(Role role) {
        roles.add(role);
    }

}
