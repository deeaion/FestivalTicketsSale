package festival.persistance.Interfaces;


import festival.model.Entity;

/**
 * CRUD operations repository interface
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> - type of entities saved in repository
 */
public interface Repository<ID,E extends Entity<ID>> {
    E findOne(ID id);
    /**
     *
     * @return all entities
     */
    Iterable<E> findAll();
    E save(E entity);
    E delete(ID id);
    E update(ID id,E entity);
}
