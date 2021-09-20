package util;

public class nullUserInfoException extends Exception{
    
    /**
     * Exceção gerada pela inserção de usuário sem as informações de Nome, Chave ou Sexo.
     * @param string 
     */
    public nullUserInfoException(String string) {
        super(string);
    }
}
