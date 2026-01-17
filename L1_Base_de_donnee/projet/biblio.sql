-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : dim. 01 juin 2025 à 15:28
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `biblio`
--

-- --------------------------------------------------------

--
-- Structure de la table `livre`
--

CREATE TABLE `livre` (
  `id_livre` int(11) NOT NULL,
  `titre` text NOT NULL,
  `auteur` text NOT NULL,
  `images` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `livre`
--

INSERT INTO `livre` (`id_livre`, `titre`, `auteur`, `images`) VALUES
(14, 'Les Misérables', 'Victor Hugo', '../image/livre/miserables.jpg'),
(15, 'Le Comte de Monte-Cristo', 'Alexandre Dumas', '../image/livre/montecristo.jpg'),
(16, 'Orgueil et préjugés', 'Jane Austen', '../image/livre/austen.jpg'),
(17, 'Le Rouge et le Noir', 'Stendhal', '../image/livre/rougeetnoir.jpg'),
(18, 'Candide', 'Voltaire', '../image/livre/candide.jpg'),
(19, 'La Guerre des mondes', 'H.G. Wells', '../image/livre/guerremonde.jpeg'),
(20, 'Frankenstein', 'Mary Shelley', '../image/livre/frankenstein.jpeg'),
(21, 'Le Horla', 'Guy de Maupassant', '../image/livre/horla.jpeg'),
(22, 'Le Mythe de Sisyphe', 'Albert Camus', '../image/livre/sisyphe.png'),
(23, 'Ainsi parlait Zarathoustra', 'Friedrich Nietzsche', '../image/livre/zarathoustra.jpg'),
(24, 'Les 4 Accords Toltèques', 'Don Miguel Ruiz', '../image/livre/accord.jpg'),
(25, 'L Alchimiste', 'Paulo Coelho', '../image/livre/alchimiste.jpeg'),
(26, 'Persepolis', 'Marjane Satrapi', '../image/livre/persepolis.jpg');

-- --------------------------------------------------------

--
-- Structure de la table `reservation`
--

CREATE TABLE `reservation` (
  `id_utilisateur` int(11) NOT NULL,
  `titre` text NOT NULL,
  `auteur` text NOT NULL,
  `images` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `reservation`
--

INSERT INTO `reservation` (`id_utilisateur`, `titre`, `auteur`, `images`) VALUES
(2, 'Candide', 'Voltaire', '../image/livre/candide.jpg'),
(2, 'Le Horla', 'Guy de Maupassant', '../image/livre/horla.jpeg');

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `id_utilisateur` int(11) NOT NULL,
  `nom` text NOT NULL,
  `prenom` text NOT NULL,
  `email` text NOT NULL,
  `motdepasse` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `livre`
--
ALTER TABLE `livre`
  ADD PRIMARY KEY (`id_livre`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`id_utilisateur`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `livre`
--
ALTER TABLE `livre`
  MODIFY `id_livre` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id_utilisateur` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
