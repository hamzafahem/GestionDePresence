package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Le front lit "res.items" (voir menu.page.ts, listfornv.page.ts, module-c.page.ts) :
 * le nom de champ "items" doit rester tel quel (pas de snake_case à appliquer, un seul mot).
 */
@Getter
@Setter
@AllArgsConstructor
public class FormationListResponse {
    private List<FormationDto> items;
}
