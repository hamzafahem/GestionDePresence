package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DashboardStatsDto {
    private long formationsCount;
    private long formationsDelta;
    private long doctorantsCount;
    private long doctorantsDelta;
    private long rdvsCount;
    private long rdvsDelta;
    private long demandesCount;
    private long demandesDelta;
}
