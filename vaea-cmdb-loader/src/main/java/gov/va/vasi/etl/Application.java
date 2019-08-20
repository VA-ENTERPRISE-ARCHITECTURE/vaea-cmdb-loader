package gov.va.vasi.etl;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import gov.va.vasi.etl.cmdb.ETLMainProcessor;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
	DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class Application {

    private static final Logger LOG = Logger.getLogger(Application.class.getName());
    @Autowired
    ETLMainProcessor processor;

    public static void main(String[] args) {
	SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
	return args -> {
	    processor.process();
	    ((ConfigurableApplicationContext) ctx).close();
	};
    }

}