import { ExternalUser } from '@app/core/model';
import { ExternalOperation } from '@app/shared/model/external-item.model';
import { BaseVersionedAndAuditingEntity } from 'arte-ng/model';
import { Type } from 'class-transformer';

export class Filter extends BaseVersionedAndAuditingEntity {
    public name: string;
    public resourceName: string | null = null;
    public permalink: string;
    public lastAccessDate: Date;
    public notes: string | null = null;

    @Type(() => ExternalUser)
    public externalUser: ExternalUser;

    @Type(() => ExternalOperation)
    public externalOperation: ExternalOperation;
}
