package net.paguo.trafshow.backend.snmp.summary.commands;

/**
 * @author Reyentenko
 */
public interface UpdateDatabaseCommand<T> {
    void doUpdate(T commandObject);
}
