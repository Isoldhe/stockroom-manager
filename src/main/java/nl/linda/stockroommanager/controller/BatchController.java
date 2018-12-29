package nl.linda.stockroommanager.controller;

import nl.linda.stockroommanager.model.Batch;
import nl.linda.stockroommanager.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// The mappings aren't necessary in this project, because Vaadin uses an instance of this controller to get to the methods

@RestController
public class BatchController {
    private BatchService batchService;

    @Autowired
    public void setBatchService(BatchService batchService) {
        this.batchService = batchService;
    }

    public List<Batch> findAllBatches() {
        return batchService.findAllBatches();
    }

    public Batch findBatch(@PathVariable long id) {
        Batch batch = null;
        try {
            batch = batchService.findBatchById(id);
        } catch(NullPointerException npe) {
            npe.printStackTrace();
        }
        return batch;
    }

    public Batch createBatch(Batch batch) {
        return batchService.saveBatch(batch);
    }

    public Batch updateBatch(Batch batch) {
        return batchService.saveBatch(batch);
    }

    public void deleteBatch(Batch batch) {
        batchService.deleteBatch(batch);
    }
}
