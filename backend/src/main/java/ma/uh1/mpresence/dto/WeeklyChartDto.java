package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class WeeklyChartDto {
    private List<String> labels;
    private List<Integer> counts;
    /**
     * Points prêts à l'emploi pour un <polyline points="..."> SVG,
     * viewBox 0 0 700 200 (calculés côté serveur pour rester simple côté template).
     */
    private String svgPoints;
    private int maxCount;
    private String rangeLabel;
}
