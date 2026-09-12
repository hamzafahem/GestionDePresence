package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class PublicationDto {
    private Long id;
    private Long doctorantId;
    private String doctorantNom;
    private String doctorantPrenom;
    private String type;
    private String titre;
    private String reference;
    private LocalDate datePublication;
    private String lien;
    private String description;
}
