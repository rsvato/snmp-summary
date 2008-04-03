package net.paguo.trafshow.backend.snmp.summary.model;

/**
 * @author Reyentenko
 */
public class BadParameterException extends Exception {
    private String badParameter;
    private static final long serialVersionUID = 3923164107961116715L;

    public BadParameterException(String s) {
        this.badParameter = s;
    }

    public String getBadParameter() {
        return badParameter;
    }
}
