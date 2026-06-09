# 📖 Gestionnaire de Bibliothèque Personnelle : JavaFX / MVVM

Projet personnel réalisé en **L2 Informatique** à l'Université de Lorraine (mai 2026).

Application desktop JavaFX pour gérer sa collection de livres, avec une architecture **MVVM** complète, persistance **JSON**, thème sombre et système de suggestion intelligente.

---

## Fonctionnalités

- **Catalogue complet** : ajout, modification, suppression de livres (titre, auteur, genre, année, synopsis)
- **Notation par étoiles** : système 1 à 5 étoiles géré dynamiquement en CSS
- **Statut de lecture** : marquage lu / non lu par livre
- **Filtrage instantané** : recherche par titre ou auteur, filtre par genre
- **Tri multi-critères** : titre, auteur, année, note (croissant et décroissant)
- **Import / Export JSON** : sauvegarde et chargement de la collection entre sessions
- **Suggestion intelligente** : recommande un livre au hasard selon genre, note minimale et statut de lecture
- **Thème sombre** : interface stylisée via `style.css` avec composants CSS personnalisés

---

## Architecture — MVVM

```
src/
├── Main.java                        # Point d'entrée JavaFX
├── model/
│   └── Book.java                    # Entité livre (title, author, genre, year, synopsis, rating, vu)
├── viewmodel/
│   ├── LibraryViewModel.java        # ViewModel principal : CRUD, filtre, tri, suggestion
│   └── BookViewModel.java           # ViewModel d'un livre (propriétés observables JavaFX)
├── view/
│   ├── Mainview.fxml                # Vue principale (layout JavaFX)
│   ├── SuggestionDialog.java        # Boîte de dialogue de suggestion
│   └── style.css                    # Thème sombre + notation étoiles
├── controller/
│   └── MainController.java          # Contrôleur : lien FXML ↔ ViewModel
└── service/
    └── BookService.java             # Sérialisation / désérialisation JSON
```

### Points techniques notables

- **`FilteredList` + `SortedList` JavaFX** : filtrage et tri réactifs sans rechargement de la liste
- **Propriétés observables** (`StringProperty`, `IntegerProperty`) pour la synchronisation automatique vue/modèle
- **Sérialisation JSON** via `org.json` : import/export de collections complètes
- **Séparation stricte** : `BookService` isole toute la logique d'accès aux données du ViewModel

---

## Prérequis

- Java 17+
- JavaFX 17+
- Bibliothèque `org.json` (JSON parsing)

---

## Lancement (IntelliJ IDEA)

```
1. Ouvrir le projet dans IntelliJ
2. Configurer le module JavaFX dans les VM options :
   --module-path /chemin/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
3. Lancer Main.java
```

---

## Exemple de structure JSON (données)

```json
[
  {
    "title": "Dune",
    "author": "Frank Herbert",
    "genre": "Science-Fiction",
    "year": 1965,
    "rating": 5,
    "vu": true,
    "synopsis": "Une planète désertique, une épice rare..."
  }
]
```

---

## Auteur

**Tiburce Jules Hinson** : L2 Informatique, Université de Lorraine  
[juleshinson1@gmail.com](mailto:juleshinson1@gmail.com)
