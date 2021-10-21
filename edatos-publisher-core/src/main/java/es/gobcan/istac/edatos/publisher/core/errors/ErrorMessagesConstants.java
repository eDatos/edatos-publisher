package es.gobcan.istac.edatos.publisher.core.errors;

public final class ErrorMessagesConstants {

    public static final String USUARIO_EXISTE = "Login ya en uso";
    public static final String ENTITY_NAME_DOCUMENTO = "documento";
    public static final String ID_MISSING_MESSAGE = "Se necesita un identificador";
    public static final String ENTITY_NOT_FOUND_MESSAGE = "Entidad %d no encontrada";
    public static final String ENTITY_CURRENTLY_DELETED = "Entidad actualmente eliminada.";
    public static final String ENTITY_CURRENTLY_NOT_DELETED = "Entidad actualmente no eliminada.";
    public static final String ID_EXISTE = "Una nueva entidad no debe de tener ID.";
    public static final String QUERY_NO_SOPORTADA = "Parámetro query no soportado: '%s'";
    public static final String PASSWORD_NOT_MATCH = "La contraseña debe ser la misma que la del usuario autorizado.";

    // QUARTZ
    public static final String QUARTZ_JOB_EXECUTION_ERROR = "Error durante la ejecución de un job Quartz";

    private ErrorMessagesConstants() {
    }

}
