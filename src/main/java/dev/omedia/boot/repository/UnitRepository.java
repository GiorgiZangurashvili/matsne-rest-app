package dev.omedia.boot.repository;

import dev.omedia.boot.domain.Unit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends CrudRepository<Unit, Long> {
    List<Unit> findAllByParentId(long parentId);
}
