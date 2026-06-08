# ⚽ Simulation d'un match de football — NetLogo

Projet universitaire réalisé en **L2 Informatique** à l'Université de Lorraine (décembre 2025).  
Développé en groupe avec **Hamidouni**, **Mahsoun** et **Morel**.

Simulation multi-agents d'un match de football complet en **NetLogo** : deux équipes de joueurs autonomes se déplacent, se passent la balle et tirent au but selon des règles comportementales définies par agent.

---

## Concept

Le modèle utilise la programmation **orientée agents** de NetLogo :
- Chaque joueur est un agent avec son propre comportement (positionnement, prise de décision, déplacement)
- La balle est un agent séparé dont la position est mise à jour à chaque tick
- Les gardiens ont un comportement distinct des joueurs de champ
- Le score est suivi en temps réel sur l'interface NetLogo

---

## Comportements simulés

- Déplacement des joueurs vers la balle ou vers leur position tactique
- Passe à un coéquipier mieux placé
- Tir au but quand le joueur est en zone de frappe
- Gardien : se déplace pour intercepter la trajectoire du tir
- Changement de possession après une interception ou un but

---

## Fichiers

```
projet/
├── match_de_foot.nlogox    # Modèle NetLogo complet (agents + interface)
├── Rapport.odt             # Rapport de projet (choix de conception, résultats)
└── presentation.odp        # Présentation de soutenance
```

---

## Lancement

1. Installer **NetLogo** (netlogo.org)
2. Ouvrir `match_de_foot.nlogox` dans NetLogo
3. Cliquer **Setup** pour initialiser le terrain et les agents
4. Cliquer **Go** pour lancer la simulation

Les paramètres (vitesse des joueurs, probabilité de passe, taille du terrain) sont ajustables via les sliders de l'interface.

---

## Auteurs

**Tiburce Jules Hinson**, **Hamidouni**, **Mahsoun**, **Morel** — L2 Informatique, Université de Lorraine  
[juleshinson1@gmail.com](mailto:juleshinson1@gmail.com)
