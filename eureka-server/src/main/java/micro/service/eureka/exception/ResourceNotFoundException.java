package micro.service.eureka.exception;

/**
 * Exception levee lorsqu'une ressource demandee est introuvable en base de donnees.
 * <p>
 * Exemple d'utilisation : tentative d'acces a une ressource inexistante via son ID.
 * Cette exception etend {@link RuntimeException} et peut etre interceptee
 * par un gestionnaire global d'exceptions ({@code @ControllerAdvice}).
 * </p>
 *
 * @author GitXavv
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Construit l'exception avec un message decrivant la ressource manquante.
     *
     * @param message description de la ressource introuvable
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
