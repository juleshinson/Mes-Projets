package labyrinth.service;

import labyrinth.service.dialog.DialogService;
import labyrinth.service.persistence.SqlPersistenceService;
import labyrinth.service.serialization.SerializationService;

// agregateur de services; initialisé au debut et passé aux viewmodels qui en ont besoin
public class Services {

    private final SerializationService serializationService;
    private final DialogService dialogService;
    private final SqlPersistenceService persistenceService;

    public Services(SerializationService serializationService, DialogService dialogService) {
        this(serializationService, dialogService, null) ;
    }

    public Services(SerializationService serializationService, DialogService dialogService, SqlPersistenceService persistenceService) {
        // ici on vérifie que c'est pas null sinon ca crashera plus tard de maniere bizarre
        if (serializationService == null || dialogService == null) {
            throw new IllegalArgumentException("les services peuvent pas etre null");
        }
        this.serializationService = serializationService;
        this.dialogService = dialogService;
        this.persistenceService = persistenceService;
    }

    public SerializationService getSerializationService() {
        return serializationService;
    }

    public DialogService getDialogService() {
        return dialogService;
    }

    public SqlPersistenceService getPersistenceService() {
        return persistenceService ;
    }
}