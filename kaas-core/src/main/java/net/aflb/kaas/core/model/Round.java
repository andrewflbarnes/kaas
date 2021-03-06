package net.aflb.kaas.core.model;

import java.util.Date;
import java.util.Set;

public record Round(
        long id,
        String name,
        Set<Team> teams,
        Division division,
        League league,
        Date date
) {
}
