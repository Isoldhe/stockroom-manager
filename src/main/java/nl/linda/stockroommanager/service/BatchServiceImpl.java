package nl.linda.stockroommanager.service;

import nl.linda.stockroommanager.model.Batch;
import nl.linda.stockroommanager.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchServiceImpl implements BatchService {
    private BatchRepository batchRepository;

    @Autowired
    public void setBatchRepository(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    @Override
    public List<Batch> findAllBatches() {
        return batchRepository.findAll();
    }

    @Override
    public Batch findBatchById(Long id) {
        return batchRepository.findById(id).orElse(null);
    }

    @Override
    public Batch saveBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    @Override
    public void deleteBatch(Batch batch) {
        batchRepository.delete(batch);
    }
}
