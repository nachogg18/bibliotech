package com.bibliotech.config;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {
  private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

  @Bean
  @Profile("dockerlocal")
  public DataSource dataSource() {
    
    // Obtiene el puerto y el nombre de la base de datos de las variables de entorno
    String dbServer = System.getenv("DB_SERVER");
    String dbPort = System.getenv("DB_PORT");
    String dbName = System.getenv("DB_NAME");
    String dbUsername = System.getenv("DB_USER");
    String dbpassword = System.getenv("DB_PASSWORD");

    // Construye la URL del DataSource
    String dbUrl = "jdbc:mysql://" + dbServer +":" + dbPort + "/" + dbName;


    logger.info(String.format("Configure with url: %s", dbUrl));

    //setea los datos de conexion
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setUrl(dbUrl);
    dataSource.setUsername(dbUsername);
    dataSource.setPassword(dbpassword);

    logger.info(String.format("Datasource configured: %s", dataSource));

    return dataSource;
  }
}
