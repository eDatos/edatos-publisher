import { Language } from '@app/core/model/language.model';
import { Treatment } from '@app/core/model/treatment.model';
import { BaseVersionedAndAuditingWithDeletionEntity } from 'arte-ng/model';

export class ExternalUser extends BaseVersionedAndAuditingWithDeletionEntity {
    public id: number;
    public name: string;
    public surname1: string;
    public surname2: string;
    public password: string;
    public email: string;
    public organism: string;
    public treatment: Treatment;
    public language: Language;
    public phoneNumber: string;
    public emailNotificationsEnabled: boolean;

    public getFullName(): string {
        return `${this.name} ${this.surname1} ${this.surname2 ? this.surname2 : ''}`;
    }
}
