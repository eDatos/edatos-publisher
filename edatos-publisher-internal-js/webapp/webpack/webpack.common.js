const MergeJsonWebpackPlugin = require('merge-jsons-webpack-plugin');
const StringReplacePlugin = require('string-replace-webpack-plugin');
const path = require('path');
const fs = require('fs');

const utils = require('./utils.js');

const options = {
    env: process.env.BUILD_ENV,
};

function geti18nLanguages(i18nPathRelativeToSrc) {
    const i18nPath = path.join(__dirname, '../src', i18nPathRelativeToSrc);
    return fs.readdirSync(i18nPath, 'utf8');
}

const availableLanguages = geti18nLanguages('i18n');

const DATAS = {
    VERSION: `'${utils.parseVersion()}'`,
    DEBUG_INFO_ENABLED: options.env === 'development',
    AVAILABLE_LANGUAGES: JSON.stringify(availableLanguages),
};

module.exports = {
    module: {
        rules: [
            {
                test: /app.constants.ts$/,
                loader: StringReplacePlugin.replace({
                    replacements: [
                        {
                            pattern: /\/\* @toreplace (\w*?) \*\//gi,
                            replacement: (match, p1, offset, string) => `_${p1} = ${DATAS[p1]};`,
                        },
                    ],
                }),
            },
        ],
    },
    plugins: [
        new MergeJsonWebpackPlugin({
            output: {
                groupBy: availableLanguages.map((lang) => ({ pattern: `./src/i18n/${lang}/*.json`, fileName: `./i18n/${lang}.json` })),
            },
        }),
        new StringReplacePlugin(),
    ],
};
