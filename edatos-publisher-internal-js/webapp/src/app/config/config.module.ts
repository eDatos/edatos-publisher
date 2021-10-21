import { NgModule } from '@angular/core';
import { ConfigService } from './config.service';
import { PaginationConfig } from './uib-pagination.config';

@NgModule({
    providers: [ConfigService, PaginationConfig],
})
export class ConfigModule {}
