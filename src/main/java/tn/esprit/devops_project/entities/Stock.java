package tn.esprit.devops_project.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Stock implements Serializable {

    // Initialisation du logger
    private static final Logger logger = Logger.getLogger(Stock.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idStock;
    String title;

    @OneToMany(mappedBy = "stock")
    Set<Product> products;

    // Méthode pour afficher les informations de Stock
    public void displayStockInfo() {
        logger.info("Affichage des informations du stock ID: " + idStock);
        logger.debug("Titre du stock: " + title);
        if (products != null) {
            logger.info("Le stock contient " + products.size() + " produits.");
        } else {
            logger.warn("Aucun produit associé à ce stock.");
        }
    }

    // Exemple d'une méthode pour ajouter un produit
    public void addProduct(Product product) {
        if (product != null) {
            products.add(product);
            logger.info("Produit ajouté au stock ID: " + idStock);
            logger.debug("Détails du produit ajouté : " + product.toString());
        } else {
            logger.error("Échec de l'ajout du produit : produit nul.");
        }
    }
}
