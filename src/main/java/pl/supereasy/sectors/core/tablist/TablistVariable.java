package pl.supereasy.sectors.core.tablist;


import pl.supereasy.sectors.core.user.impl.User;

public interface TablistVariable
{
    String[] names();

    String get(final User p0);
}
