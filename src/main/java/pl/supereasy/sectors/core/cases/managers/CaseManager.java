package pl.supereasy.sectors.core.cases.managers;

import pl.supereasy.sectors.core.cases.api.Case;

import java.util.HashMap;

public interface CaseManager {

    void registerCase(Case addedCase);

    void unregisterCase(Case deleteCase);

    HashMap<String, Case> getCases();
}
