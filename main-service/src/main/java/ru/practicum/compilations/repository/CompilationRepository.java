package ru.practicum.compilations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.compilations.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long>, QuerydslPredicateExecutor<Compilation> {
}
