package com.bibliotech.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfig {

  @Bean
  @Profile("!local")
  public DataSource dataSource() {
    // Obtiene el puerto y el nombre de la base de datos de las variables de entorno
    String dbPort = System.getenv("DB_PORT");
    String dbName = System.getenv("DB_NAME");
    String dbUsername = System.getenv("DB_USER");
    String dbpassword = System.getenv("DB_PASSWORD");

    // Construye la URL del DataSource
    String dbUrl = "jdbc:mysql://localhost:" + dbPort + "/" + dbName;



    //setea los datos de conexion
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setUrl(dbUrl);
    dataSource.setUsername(dbUsername);
    dataSource.setPassword(dbpassword);

    return dataSource;
  }
}
