package com.deemor.ttsai.repository;

import com.deemor.ttsai.entity.request.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RequestRepository extends PagingAndSortingRepository<Request, Long> {

    Page<Request> findAll(Pageable pageable);
}
