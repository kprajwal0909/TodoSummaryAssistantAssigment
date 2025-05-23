package in.Prajwal.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.Prajwal.Model.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {

}
