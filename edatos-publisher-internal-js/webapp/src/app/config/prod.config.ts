import { enableProdMode } from '@angular/core';
import { DEBUG_INFO_ENABLED } from '@app/app.constants';
import { Logger } from '@app/core/service/log';

export function ProdConfig() {
    // disable debug data on prod profile to improve performance
    if (!DEBUG_INFO_ENABLED) {
        Logger.enableProductionMode();
        enableProdMode();
    }
}
