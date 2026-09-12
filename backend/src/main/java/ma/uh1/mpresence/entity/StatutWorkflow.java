package ma.uh1.mpresence.entity;

/**
 * Statut de traitement partagé par Demande et Reinscription (même cycle de
 * vie : soumis par le doctorant, traité par un admin/formateur).
 */
public enum StatutWorkflow {
    EN_ATTENTE,
    APPROUVEE,
    REJETEE
}
