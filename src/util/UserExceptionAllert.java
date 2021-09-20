package util;

public class UserExceptionAllert extends Exception{
    private final String message;
    private final Exception ex;

    /**
     * Exceção utilizada para alerta do usuário.
     * @param message Mensagem de alerta.
     * @param ex Exceção que pode ter sido gerada.
     */
    public UserExceptionAllert(String message, Exception ex) {
        this.message = message;
        this.ex = ex;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Exception getEx() {
        return ex;
    }
    
    
    
}
