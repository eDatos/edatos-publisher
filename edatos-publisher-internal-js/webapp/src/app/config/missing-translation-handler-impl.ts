import {MissingTranslationHandler, MissingTranslationHandlerParams} from '@ngx-translate/core';

export class MissingTranslationHandlerImpl implements MissingTranslationHandler {
    handle(params: MissingTranslationHandlerParams) {
        if (params.interpolateParams) {
            return params.interpolateParams["default"].toUpperCase() || params.key;
        }
        return params.key;
    }
}