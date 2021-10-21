import { BaseVersionedAndAuditingEntity } from "arte-ng/model";
import { Type } from "class-transformer";
import { InternationalString } from "./international-string.model";

export class DataProtectionPolicy extends BaseVersionedAndAuditingEntity {
    
    @Type(() => InternationalString)
    public value: InternationalString;
}
