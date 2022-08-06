package br.net.yurinogueira.springsales.config;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.net.yurinogueira.springsales.domain.entity.Client;
import br.net.yurinogueira.springsales.domain.entity.Role;
import br.net.yurinogueira.springsales.domain.entity.Sale;
import br.net.yurinogueira.springsales.domain.entity.SystemUser;
import br.net.yurinogueira.springsales.domain.enums.SaleType;
import br.net.yurinogueira.springsales.domain.repository.RoleRepository;
import br.net.yurinogueira.springsales.domain.service.ClientService;
import br.net.yurinogueira.springsales.domain.service.SaleService;
import br.net.yurinogueira.springsales.domain.service.impl.UserServiceImpl;


@Configuration
@RequiredArgsConstructor
public class InitialDataConfig {
    
    private final UserServiceImpl userService;
    private final SaleService saleService;
    private final ClientService clientService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Bean
    public CommandLineRunner startData() {
        return args -> {
            final String adminLogin = "admin@email.com";
            final String adminPassword = encoder.encode("admin");

            if (userService.existsByLogin(adminLogin)) return;
    
            Role adminRole = Role.builder().authority("ADMIN").build();
            Role userRole = Role.builder().authority("USER").build();

            roleRepository.save(adminRole);
            roleRepository.save(userRole);

            Client client = Client.builder()
                        .name("Admin Central")
                        .document("95414627070")
                        .build();

            clientService.save(client);

            SystemUser user = SystemUser.builder()
                        .login(adminLogin)
                        .password(adminPassword)
                        .roles(List.of(adminRole, userRole))
                        .client(client)
                        .build();

            userService.save(user);

            Sale APASale = Sale.builder()
                        .saleCheckAmount(2)
                        .saleAmount(1)
                        .type(SaleType.AMOUNT_PER_AMOUNT)
                        .description("Leve 2 e Pague 1")
                        .build();
            Sale APPSale = Sale.builder()
                        .saleCheckAmount(3)
                        .salePrice(10.0)
                        .type(SaleType.AMOUNT_PER_PRICE)
                        .description("3 por R$10,00")
                        .build();

            saleService.save(APASale);
            saleService.save(APPSale);
        };
    }
    
}
