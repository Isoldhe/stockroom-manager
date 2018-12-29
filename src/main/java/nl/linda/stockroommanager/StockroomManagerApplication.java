package nl.linda.stockroommanager;

import nl.linda.stockroommanager.model.Batch;
import nl.linda.stockroommanager.service.BatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StockroomManagerApplication {

	private static final Logger log = LoggerFactory.getLogger(StockroomManagerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(StockroomManagerApplication.class, args);
	}

//	This method is automatically being called due to the @EnableAutoConfiguarion annotation, which is part of the @SpringBootApplication
//  @EnableAutoConfiguarion searches for beans to autoconfigure and because this method is declared as a @Bean,
//  it fires as soon as the application starts
	@Bean
	public String loadData(BatchService batchService) {
		batchService.saveBatch(new Batch("Catfood", 15L, 12607L, "I", 1));
		batchService.saveBatch(new Batch("Dogfood", 15L, 12608L, "C", 2));
		batchService.saveBatch(new Batch("Cat toys", 10L, 13400L, "B", 3));
		batchService.saveBatch(new Batch("Treats", 5L, 25602L, "H", 2));
		batchService.saveBatch(new Batch("Beds", 150L, 34300L, "A", 2));
		batchService.saveBatch(new Batch("Rats", 150L, 34300L, "B", 2));
		batchService.saveBatch(new Batch("Homes", 150L, 34300L, "C", 3));
		batchService.saveBatch(new Batch("Birds", 150L, 34300L, "E", 1));
		batchService.saveBatch(new Batch("Leashes", 150L, 34300L, "F", 3));
		batchService.saveBatch(new Batch("Hamsters", 150L, 34300L, "B", 1));
		batchService.saveBatch(new Batch("Fish", 150L, 34300L, "G", 1));

		// fetch all customers
		log.info("Stocks found with findAll() in Application:");
		log.info("-------------------------------");
		for (Batch batch : batchService.findAllBatches()) {
			log.info(batch.getName());
		}

		return "Data loaded";
	}

}

