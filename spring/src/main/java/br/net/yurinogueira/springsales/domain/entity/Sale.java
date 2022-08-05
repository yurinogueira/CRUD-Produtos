package br.net.yurinogueira.springsales.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import br.net.yurinogueira.springsales.domain.enums.SaleType;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name = "sale")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "sale")
    private List<Product> product;

    @Column(name = "description", length = 256)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SaleType type;

    @Column(name = "sale_check_amount")
    private Integer saleCheckAmount;

    @Column(name = "sale_amount")
    private Integer saleAmount;

    @Column(name = "sale_price", length = 20, precision = 2)
    private BigDecimal salePrice;

}
