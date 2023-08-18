package es.gobcan.istac.edatos.publisher.core.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalCkanItemEntity;
import es.gobcan.istac.edatos.publisher.core.repository.ExternalCkanItemRepository;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCkanItemService;
import es.gobcan.istac.edatos.publisher.core.util.QueryUtil;

@Service
public class ExternalCkanItemServiceImpl implements ExternalCkanItemService {

    private final ExternalCkanItemRepository externalCkanItemRepository;
    private final QueryUtil queryUtil;

    public ExternalCkanItemServiceImpl(ExternalCkanItemRepository externalCkanItemRepository, QueryUtil queryUtil) {
        this.externalCkanItemRepository = externalCkanItemRepository;
        this.queryUtil = queryUtil;
    }

    @Override
    public ExternalCkanItemEntity save(ExternalCkanItemEntity externalCkanItem) {
        return externalCkanItemRepository.save(externalCkanItem);
    }

    @Override
    public Page<ExternalCkanItemEntity> find(Pageable pageable, String query) {
        DetachedCriteria criteria = buildExternalCkanResourcesCriteria(pageable, query);
        return externalCkanItemRepository.findAll(criteria, pageable);
    }

    private DetachedCriteria buildExternalCkanResourcesCriteria(Pageable pageable, String query) {
        StringBuilder queryBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(query)) {
            queryBuilder.append(query);
        }
        return queryUtil.queryToUserExternalUserCriteria(pageable, queryBuilder.toString());
    }
}
