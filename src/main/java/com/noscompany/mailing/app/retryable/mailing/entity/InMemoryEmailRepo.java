package com.noscompany.mailing.app.retryable.mailing.entity;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

//for unit testing
public class InMemoryEmailRepo implements EmailRepo {
    private long sequence = 1L;
    private Set<Email> mails = new HashSet<>();

    @Override
    public Set<Email> findAllByStatus(Email.Status status) {
        return mails
                .stream()
                .filter(m -> m.getStatus() == status)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Email> findAllEager() {
        return mails;
    }

    @Override
    public List<Email> findByStatusOrderByCreationDate(Set<Email.Status> statuses, Pageable pageable) {
        Stream<Email> sorted = mails
                .stream()
                .filter(m -> statuses.contains(m.getStatus()))
                .sorted(comparing(Email::getCreatedOn));
        try {
            return sorted
                    .limit(pageable.getPageSize())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return sorted.collect(Collectors.toList());
        }
    }

    @Override
    public List<Email> findAll() {
        return new ArrayList<>(mails);
    }

    @Override
    public List<Email> findAll(Sort sort) {
        return new ArrayList<>(mails);
    }

    @Override
    public Page<Email> findAll(Pageable pageable) {
        return new PageImpl<Email>(new ArrayList<>(mails));
    }

    @Override
    public List<Email> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return mails.size();
    }

    @Override
    public void deleteById(Long aLong) {
        mails = mails.stream().filter(m -> m.getId() != aLong).collect(Collectors.toSet());
    }

    @Override
    public void delete(Email entity) {
        mails.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends Email> entities) {
        for (Email m : entities) {
            mails.remove(m);
        }
    }

    @Override
    public void deleteAll() {
        mails = new HashSet<>();
    }

    @Override
    public <S extends Email> S save(S entity) {
        mails.add(entity);
        entity.setId(sequence++);
        return entity;
    }

    @Override
    public <S extends Email> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            mails.add(entity);
            entity.setId(sequence++);
            result.add(entity);
        }
        return result;
    }

    @Override
    public Optional<Email> findById(Long aLong) {
        return mails.stream().filter(m -> m.getId() == aLong).findFirst();
    }

    @Override
    public boolean existsById(Long aLong) {
        return mails.stream().anyMatch(m -> m.getId() == aLong);
    }

    @Override
    public void flush() { }

    @Override
    public <S extends Email> S saveAndFlush(S entity) {
        mails.add(entity);
        entity.setId(sequence++);
        return entity;
    }

    @Override
    public void deleteInBatch(Iterable<Email> entities) {
        for (Email m : entities)
            mails.remove(m);
    }

    @Override
    public void deleteAllInBatch() {
        mails = new HashSet<>();
    }

    @Override
    public Email getOne(Long aLong) {
        return findById(aLong).get();
    }

    @Override
    public <S extends Email> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Email> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Email> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Email> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Email> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Email> boolean exists(Example<S> example) {
        return false;
    }
}
