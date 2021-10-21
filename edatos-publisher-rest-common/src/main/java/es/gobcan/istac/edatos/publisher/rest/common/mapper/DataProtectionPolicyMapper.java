package es.gobcan.istac.edatos.publisher.rest.common.mapper;

import org.mapstruct.Mapper;

import es.gobcan.istac.edatos.publisher.core.domain.DataProtectionPolicyEntity;
import es.gobcan.istac.edatos.publisher.rest.common.dto.DataProtectionPolicyDto;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.resolver.GenericMapperResolver;

@Mapper(componentModel = "spring", uses = {GenericMapperResolver.class, InternationalStringVOMapper.class})
public interface DataProtectionPolicyMapper extends EntityMapper<DataProtectionPolicyDto, DataProtectionPolicyEntity> {

}
