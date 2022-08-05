package br.net.yurinogueira.springsales.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "client")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 64)
    @NotEmpty(message = "{error.client.name-needed}")
    private String name;

    @Column(name = "document", length = 11)
    @NotEmpty(message = "{error.client.cpf-needed}")
    @CPF(message = "{error.client.cpf-invalid}")
    private String document;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private Set<Cart> carts;

    @Override
    public String toString() {
        return this.name;
    }

}
