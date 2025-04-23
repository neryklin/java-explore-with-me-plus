package ru.practicum.compilations.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.dto.CompilationsPublicParam;
import ru.practicum.compilations.dto.RequestCompilationCreate;
import ru.practicum.compilations.dto.RequestCompilationUpdate;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.model.MapperCompilation;
import ru.practicum.compilations.model.QCompilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.event.repository.EventRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @PersistenceUnit
    private EntityManagerFactory entityManager;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void initQueryFactory() {
        queryFactory = new JPAQueryFactory(entityManager.createEntityManager());
    }

    @Override
    @Transactional
    public CompilationsDto create(RequestCompilationCreate create) {
        Compilation compilation = new Compilation();
        compilation.setTitle(create.getTitle());
        compilation.setPinned(Optional.ofNullable(create.getPinned()).orElse(false));
        if (create.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllById(create.getEvents()));
        } else {
            compilation.setEvents(new ArrayList<>());
        }
        return MapperCompilation.toDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public CompilationsDto update(long compId, RequestCompilationUpdate update) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException("Подборка не найдена"));
        if (Objects.nonNull(update.getPinned())) {
            compilation.setPinned(update.getPinned());
        }
        if (Objects.nonNull(update.getTitle())) {
            compilation.setTitle(update.getTitle());
        }
        if (Objects.nonNull(update.getEvents())) {
            compilation.setEvents(eventRepository.findAllById(update.getEvents()));
        }
        return MapperCompilation.toDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public void delete(long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationsDto getById(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(EntityNotFoundException::new);
        return MapperCompilation.toDto(compilation);
    }

    @Override
    public Collection<CompilationsDto> findCompilations(CompilationsPublicParam param) {
        QCompilation compilation = QCompilation.compilation;
        JPAQuery<Compilation> query = queryFactory.selectFrom(compilation)
                .offset(param.from())
                .limit(param.size());

        if (Objects.nonNull(param.pinned())) {
            BooleanExpression expression = compilation.pinned.eq(param.pinned());
            query.where(expression);
        }

        Collection<Compilation> compilations = query.fetch();

        return compilations.stream()
                .map(MapperCompilation::toDto)
                .toList();
    }
}
