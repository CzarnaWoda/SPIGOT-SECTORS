package pl.supereasy.sectors.core.tablist;

import pl.supereasy.sectors.core.user.impl.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TablistVariablesParser
{
    private final Collection<TablistVariable> tablistVariables;

    public TablistVariablesParser() {
        this.tablistVariables = new ArrayList<TablistVariable>();
    }

    public void add(final TablistVariable variable) {
        this.tablistVariables.add(variable);
    }

    public VariableParsingResult createResultFor(final User user) {
        final Map<String, String> values = new HashMap<String, String>();
        for (final TablistVariable tablistVariable : this.tablistVariables) {
            final String value = tablistVariable.get(user);
            for (final String name : tablistVariable.names()) {
                values.put(name, value);
            }
        }
        return new VariableParsingResult(user, values);
    }
}
