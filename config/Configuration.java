package lk.upalisupermarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@org.springframework.context.annotation.Configuration
@EnableWebSecurity
public class Configuration {
    @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //Service Request
         http.authorizeHttpRequests(auth->{
            auth
            .requestMatchers("/bootsrap-5.2.3/**").permitAll()
            .requestMatchers("/fontawesome-free-6.6.0/**").permitAll()
            .requestMatchers("/fonts/**").permitAll()
            .requestMatchers("/css/**").permitAll()
            .requestMatchers("/js/**").permitAll()
            .requestMatchers("/scss/**").permitAll()
            .requestMatchers("/images/**").permitAll()
            .requestMatchers("/createadmin").permitAll()
            .requestMatchers("/login").permitAll()
            .requestMatchers("/dashboard").hasAnyAuthority("Admin","Cashier","Manager","Assistant_Manager","Store_Manager")//who has access to index page
            .requestMatchers("/employee/**").hasAnyAuthority("Admin","Manager")//emplyee eken pssee findall nisa thama ** dmme
            .requestMatchers("/privilege/**").hasAnyAuthority("Admin","Manager")//Store_Manager,Assistant_Manager
            .requestMatchers("/user/**").hasAnyAuthority("Admin","Manager")
            .requestMatchers("/customer/**").hasAnyAuthority("Admin","Manager","Assistant_Manager","Cashier")
            .requestMatchers("/supplier/**").hasAnyAuthority("Admin","Manager","Assistant_Manager","Store_Manager")
            .requestMatchers("/item/**").hasAnyAuthority("Admin","Manager","Cashier","Assistant_Manager","Store_Manager")
            .requestMatchers("/itemseasonaldiscount/**").hasAnyAuthority("Admin","Manager","Cashier","Store_Manager","Assistant_Manager")
            .requestMatchers("/grn/**").hasAnyAuthority("Admin","Manager","Assistant_Manager","Store_Manager")
            .requestMatchers("/quotationrequest/**").hasAnyAuthority("Admin","Manager","Assistant_Manager","Store_Manager")
            .requestMatchers("/purchaseorder/**").hasAnyAuthority("Admin","Manager","Assistant_Manager","Store_Manager")
            .requestMatchers("/quotation/**").hasAnyAuthority("Admin","Manager","Assistant_Manager","Store_Manager")
            .requestMatchers("/customerpayment/**").hasAnyAuthority("Admin","Manager","Assistant_Manager","Cashier")
            .requestMatchers("/inventory/**").hasAnyAuthority("Admin","Manager","Assistant_Manager","Store_Manager","Cashier")
            .requestMatchers("/invoice/**").hasAnyAuthority("Admin","Manager","Cashier","Assistant_Manager")
            .requestMatchers("/supplierpayment/**").hasAnyAuthority("Admin","Manager","Assistant_Manager")
            .requestMatchers("/purchasepaymentreport/**").hasAnyAuthority("Admin","Manager")
            .requestMatchers("/employeereport/**").hasAnyAuthority("Admin","Manager")
            .anyRequest().authenticated();
         })
         //Login
         .formLogin(login->{
            login
            .loginPage("/login")
            .defaultSuccessUrl("/dashboard",true)
            .failureUrl("/login?error=usernamepassworderror")
            .usernameParameter("username")
            .passwordParameter("password");
         })
         //Log out
        .logout(logout->{
            logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login");
        })
        //Exception
        .exceptionHandling(exp->{
            exp.accessDeniedPage("/errorpage");
        })
        //Diabling csrf if not we cannot access any service through js files
        .csrf(csrf->{
            csrf.disable();
        });
         return http.build();
     }

    //Password Encryption 
    @Bean //
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    } 

}
