export class LocalisedString {
    public label: string;
    public locale: string;

    constructor();
    constructor(locale: string, label: string);
    constructor(locale?: string, label?: string) {
        if (locale != null && label != null) {
            this.label = label;
            this.locale = locale;
        }
    }
}
