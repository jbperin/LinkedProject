package biz.perin.jibe.linkedproject.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Famille PERIN on 20/10/2017.
 */
public class Announce {

    private Integer id;
    private String ownerPseudo;
    private String description;

    private Direction direction;
    private Category category;

    public enum Direction {
        DEMAND
        ,OFFER
    }


    public enum Category {
        ACTIONS_COLLECTIVES ("Actions collectives")
        , AIDE_A_LA_PERSONNE                  ("Aide à la personne")
        , ALIMENTATION                        ("Alimentation")
        , ATELIER_COURS                       ("Atelier/Cours")
        , AUTRE_OBJET                         ("Autre objet")
        , AUTRE_SERVICE                       ("Autre service")
        , BRICOLAGE                           ("Bricolage")
        , COUTURE                            ("Couture")
        , COVOITURAGE                        ("Covoiturage")
        , HEBERGEMENT                        ("Hébergement")
        , INFORMATIQUE                       ("Informatique (dépannage)")
        , JARDINAGE                          ("Jardinage")
        , JEUX_JOUETS                        ("Jeux/Jouets")
        , MAISON                             ("Maison")
        , MATERIEL_OUTILLAGE                 ("Matériel/Outillage")
        , MULTIMEDIA                         ("Multimédia")
        , VETEMENTS_ACCESSOIRES              ("Vêtements et accessoires");

         private String name = "";
         Category (String name){

             this.name = name;

         }
        private static Map<String, Category> stringMap = new HashMap<String, Category>();

        static {
            stringMap.put("Actions collectives", ACTIONS_COLLECTIVES);
            stringMap.put("Aide à la personne", AIDE_A_LA_PERSONNE);
            stringMap.put("Atelier/Cours" , ATELIER_COURS);
            stringMap.put("Autre objet"                                    , AUTRE_OBJET);
            stringMap.put("Autre service"                                  , AUTRE_SERVICE);
            stringMap.put("Bricolage"                                      , BRICOLAGE);
            stringMap.put("Couture"                                         , COUTURE);
            stringMap.put("Covoiturage"                                     , COVOITURAGE);
            stringMap.put("Hébergement"                                     , HEBERGEMENT);
            stringMap.put("Informatique (dépannage)"                        , INFORMATIQUE);
            stringMap.put("Jardinage"                                       , JARDINAGE);
            stringMap.put("Jeux/Jouets"                                     , JEUX_JOUETS);
            stringMap.put("Maison"                                          , MAISON);
            stringMap.put("Matériel/Outillage"                              , MATERIEL_OUTILLAGE);
            stringMap.put("Multimédia"                                      , MULTIMEDIA);
            stringMap.put("Vêtements et accessoires"                       , VETEMENTS_ACCESSOIRES  );

            stringMap = Collections.unmodifiableMap(stringMap);
        }
        public static Category fromName(String name) {return stringMap.get(name);}

        @Override
        public String toString() {
            return  name ;
        }
    }




    public Announce(String ownerPseudo, String description, Direction direction, Category category) {
        this.ownerPseudo = ownerPseudo;
        this.description = description;
        this.direction = direction;
        this.category = category;
    }
    public Announce() {

    }

    @Override
    public String toString() {
        return "Announce{" +
                "id=" + id +
                ", ownerPseudo='" + ownerPseudo + '\'' +
                ", description='" + description + '\'' +
                ", direction=" + direction +
                ", category=" + category +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOwnerPseudo() {
        return ownerPseudo;
    }

    public void setOwnerPseudo(String ownerPseudo) {
        this.ownerPseudo = ownerPseudo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


}
