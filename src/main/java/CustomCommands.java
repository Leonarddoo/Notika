import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomCommands {

    /**
     * Attribut String
     */
    private final String author, name, title, description , image;

    /**
     * Attribut int
     */
    private final int price, red, green, blue;

    /**
     * Attribut représentant la liste des id pouvant effectuer la commande
     */
    private final List<String> acceptedId = new ArrayList<>();

    /**
     * Liste des CustomsCommands crées
     */
    private final static List<CustomCommands> commandsList = new ArrayList<>();

    /**
     * Constructeur complet
     * @param a L'auteur
     * @param n Le nom
     * @param t Le titre
     * @param d La description
     * @param i L'image
     * @param p Le prix
     * @param r La valeur de rouge
     * @param g La valeur de vert
     * @param b La valeur de rouge
     */
    public CustomCommands(String a, String n, String t, String d, String i, int p, int r, int g, int b){
        this.author = a;
        this.name = n;
        this.title = t;
        this.description = d;
        this.image = i;
        this.price = p;
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    /**
     * Permet d'initialiser la liste des CustomCommands selon un fichier de config XML
     * @param path Le chemin d'accès du fichier de config
     */
    public static void initializeCommandsList(String path) throws ParserConfigurationException, IOException, SAXException {
        //On clear l'ancienne liste de commande
        commandsList.clear();
        //On ouvre le fichier de config XML
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
        //On récupère la racine du document
        Element racine = document.getDocumentElement();
        //On parcourt toutes les commandes
        for(int i = 0 ; i < racine.getChildNodes().getLength() ; i++){
            //Si jsp
            if(racine.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE){
                Element command = (Element) racine.getChildNodes().item(i);
                //On crée une nouvelle commande
                CustomCommands custom = new CustomCommands(
                    command.getElementsByTagName("auteur").item(0).getTextContent(),
                    command.getElementsByTagName("nom").item(0).getTextContent(),
                    command.getElementsByTagName("titre").item(0).getTextContent(),
                    command.getElementsByTagName("description").item(0).getTextContent(),
                    command.getElementsByTagName("image").item(0).getTextContent(),
                    Integer.parseInt(command.getElementsByTagName("prix").item(0).getTextContent()),
                    Integer.parseInt(command.getElementsByTagName("rouge").item(0).getTextContent()),
                    Integer.parseInt(command.getElementsByTagName("vert").item(0).getTextContent()),
                    Integer.parseInt(command.getElementsByTagName("bleu").item(0).getTextContent())
                );
                //On parcourt toutes les balises id
                for(int j = 0; j < command.getElementsByTagName("id").getLength() ; j++){
                    //On ajoute dans la liste les id qui ont la permission
                    custom.getAcceptedId().add(command.getElementsByTagName("id").item(j).getTextContent());
                }
                //On ajoute la nôtre nouvelle commande à la liste des commandes connues
                commandsList.add(custom);
            }
        }
    }

    /**
     * Permet de récupérer une commande par rapport à son nom
     * @param name Nom de la commande qu'à rechercher
     * @return La commande correspondante ou null
     */
    public static CustomCommands retrieveCommandByName(String name){
        //On parcourt toutes les commandes de la liste
        for(CustomCommands cmd : commandsList){
            //Si le nom de la commande correspond avec le paramètre
            if(cmd.getName().equals(name))
                //On renvoie la commande
                return cmd;
        }
        return null;
    }

    /**
     * Permet de récupérer la liste des commandes
     * @return Liste des CustomCommands
     */
    public static List<CustomCommands> getCommandsList() {
        return commandsList;
    }

    /**
     * Permet de récupérer l'auteur
     * @return L'auteur de la commande
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Permet de récupérer le nom
     * @return Le nom de la commande
     */
    public String getName() {
        return name;
    }

    /**
     * Permet de récupérer le titre
     * @return Titre de l'embed
     */
    public String getTitle() {
        return title;
    }

    /**
     * Permet de récupérer la description
     * @return Description de l'embed
     */
    public String getDescription() {
        return description;
    }

    /**
     * Permet de récupérer l'image
     * @return L'image de l'embed
     */
    public String getImage() {
        return image;
    }

    /**
     * Permet de récupérer le prix
     * @return Le prix du plugin
     */
    public int getPrice() {
        return price;
    }

    /**
     * Permet de récupérer la valeur de la couleur rouge
     * @return La valeur du rouge de l'embed
     */
    public int getRed() {
        return red;
    }

    /**
     * Permet de récupérer la valeur de la couleur verte
     * @return La valeur de vert de l'embed
     */
    public int getGreen() {
        return green;
    }

    /**
     * Permet de récupérer la valeur de la couleur bleue
     * @return La valeur de bleu de l'embed
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Permet de récupérer la liste des ID pouvant effectuer la commande
     * @return Liste des ID
     */
    public List<String> getAcceptedId() {
        return acceptedId;
    }
}
