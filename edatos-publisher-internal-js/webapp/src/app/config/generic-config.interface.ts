export interface GenericConfig {
    cas: {
        login: any;
        logout: any;
        service: any;
    };
    endpoint: {
        appUrl: any;
    };
    metadata: {
        navbarScriptUrl: string;
        defaultLanguage: string;
    };
}
