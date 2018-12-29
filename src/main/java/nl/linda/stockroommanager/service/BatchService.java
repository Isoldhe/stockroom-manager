package nl.linda.stockroommanager.service;

import nl.linda.stockroommanager.model.Batch;

import java.util.List;

public interface BatchService {
    List<Batch> findAllBatches();

    Batch findBatchById(Long id);

    Batch saveBatch(Batch batch);

    void deleteBatch(Batch batch);
}
