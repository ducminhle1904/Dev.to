package dev.dl.userservice.application.service;

import dev.dl.common.entity.BaseEntity;
import dev.dl.userservice.infrastructure.BaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class BaseService<E extends BaseEntity, R extends BaseRepository<E>> {

    protected final R repository;

    public BaseService(R repository) {
        this.repository = repository;
    }

    /**
     * Save all items
     *
     * @param entity
     * @return entity
     */
    public E save(E entity) {
        log.info("SAVE DATA");
        return this.repository.save(entity);
    }

    /**
     * Save all items
     *
     * @param entities
     * @return
     */
    public List<E> saveAll(Iterable<E> entities) {
        return this.repository.saveAll(entities);
    }

    /**
     * Delete all items
     *
     * @param entities
     */
    public void deleteAll(Iterable<E> entities) {
        this.repository.deleteAll(entities);
    }

    /**
     * Delete an item
     *
     * @param entity
     */
    public void delete(E entity) {
        this.repository.delete(entity);
    }

    /**
     * Delete an item by id
     *
     * @param id
     */
    public void delete(Long id) {
        this.repository.deleteById(id);
    }

    /**
     * Find one item
     *
     * @param id -> key
     * @return entity
     */
    public Optional<E> findById(Long id) {
        log.info("FIND ITEM BY ID");
        return this.repository.findById(id);
    }

    /**
     * Find all items
     *
     * @return multiple entities
     */
    public List<E> findAll() {
        log.info("FIND ALL ITEM");
        return this.repository.findAll();
    }

    /**
     * Find all items with pageable
     *
     * @param pageable
     * @return multiple entities
     */
    public List<E> findAllWithPageable(Pageable pageable) {
        log.info("FIND ALL ITEM WITH PAGEABLE");
        return this.repository.findAll(pageable).getContent();
    }
}
