package es.gobcan.istac.edatos.publisher.rest.common.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import es.gobcan.istac.edatos.publisher.rest.common.dto.ExternalUserAccountBaseDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalUserEntity;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.ExternalUserRole;
import es.gobcan.istac.edatos.publisher.core.repository.ExternalUserRepository;
import es.gobcan.istac.edatos.publisher.core.security.SecurityUtils;
import es.gobcan.istac.edatos.publisher.rest.common.dto.ExternalUserAccountDto;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.config.AuditingMapperConfig;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.resolver.GenericMapperResolver;

@Mapper(componentModel = "spring", config = AuditingMapperConfig.class, uses = {GenericMapperResolver.class})
public abstract class ExternalUserAccountMapper implements EntityMapper<ExternalUserAccountDto, ExternalUserEntity> {

    @Autowired
    private ExternalUserRepository externalUserRepository;

    @Override
    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    public abstract ExternalUserEntity toEntity(ExternalUserAccountDto dto);

    @Named("encodePassword")
    public String encodePassword(String unencryptedPassword) {
        return SecurityUtils.passwordEncoder(unencryptedPassword);
    }

    @AfterMapping
    public void setRoles(ExternalUserAccountBaseDto dto, @MappingTarget ExternalUserEntity entity) {
        Set<ExternalUserRole> roles = new HashSet<>();
        roles.add(ExternalUserRole.USER);
        roles.stream().collect(Collectors.toSet());
        entity.setRoles(roles);
    }

    @Override
    @Mapping(target = "password", ignore = true)
    public abstract ExternalUserAccountDto toDto(ExternalUserEntity dto);

    public ExternalUserEntity getUserFromEmail(String email) {
        return externalUserRepository.findOneByEmail(email).orElse(null);
    }

    @Named("externalUserToBaseDto")
    public abstract ExternalUserAccountBaseDto toBaseDto(ExternalUserEntity entity);

    public abstract ExternalUserEntity baseDtoToEntity(ExternalUserAccountBaseDto dto);
}
