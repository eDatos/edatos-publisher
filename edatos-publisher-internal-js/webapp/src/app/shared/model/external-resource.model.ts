import {BaseVersionedAndAuditingEntity, BaseVersionedEntity} from 'arte-ng/model';
import { Type } from 'class-transformer';
import { InternationalString } from './international-string.model';

/**
 * This enum is a subset from the one located at
 * {@link
    * https://git.arte-consultores.com/istac/edatos-core-common/blob/master/src/main/java/org/siemac/edatos/core/common/enume/TypeExternalArtefactsEnum.java|edatos-core-common}.
 */
export enum TypeExternalArtefacts {
    CATEGORY_SCHEME ='CATEGORY_SCHEME',
    CODELIST = 'CODELIST',
    CONCEPT_SCHEME = 'CONCEPT_SCHEME',
    INDICATOR = 'INDICATOR',
    ORGANISATION_SCHEME = 'ORGANISATION_SCHEME',
    DATASET = 'DATASET'
}

export enum CkanState {
    SENT = 'SENT',
    FAILED = 'FAILED'
}

export class ExternalItem extends BaseVersionedAndAuditingEntity {
    public id: number;
    public code: string;
    public urn: string;

    @Type(() => InternationalString)
    public name: InternationalString;

    public type: TypeExternalArtefacts;

    public getLocalisedName(languageCode: string): string | undefined {
        return this.name?.getLocalisedLabel(languageCode);
    }

    public get schema(): { code?: string; version?: string } {
        const regex = /^urn:(?:\w+:)?(?:\w+\.)+\w+=\w+:(?<schemaCode>\w+)\((?<schemaVersion>.+?)\)/.exec(this.urn);
        return {
            code: regex?.groups?.schemaCode,
            version: regex?.groups?.schemaVersion,
        };
    }
}

export class ExternalCkanItemEntity extends ExternalItem {

    public ckanState: CkanState;
    public kafkaMessage: string;
}
