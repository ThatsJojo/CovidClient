package util;

public class DuplicatedUserException extends Exception {

    /**
     * Exceção para um usuário duplicado.
     * @param string mensagem que presenta o erro.
     */
    public DuplicatedUserException(String string) {
        super(string);
    }
    
}
