import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ArborescenceInitiale {


    //cette méthode lit le fichier texte et retourne un ArbreFichiers initiale
    public static ArbreFichiers lectureFichierTxt(String nomFichier){
        ArbreFichiers noeudCourant = null ;
        try{
            BufferedReader lecteur = new BufferedReader(new FileReader(nomFichier));
            String ligne = lecteur.readLine();
            
            //on crée le dossier racine sinon on lève une exception
            if(ligne.equalsIgnoreCase("racine")){
                noeudCourant = new ArbreFichiers(null,null,null,null,"",false,null);
            }else{
                lecteur.close();
                throw new IllegalArgumentException("Debut fichier non valide : doit commencer par racine");
            }
            //tant qu'on trouve le mot 'fin' isoler on  continue à implementer notre arborescence de fichiers et dossiers
            while(!ligne.equalsIgnoreCase("fin")){
                ligne=lecteur.readLine();
                String [] tab = ligne.split(" ");
                if(tab.length>2 && tab[2].equals("d")){
                    ArbreFichiers nouveauFils = new ArbreFichiers(null, null, null, null, tab[1], false, null);
                    noeudCourant.addFils(nouveauFils);
                    noeudCourant = nouveauFils;
                }else if(tab.length>2 && tab[2].equals("f")){
                    String nom = tab[1];
                    ligne = lecteur.readLine();
                    ArbreFichiers nouveauFils = new ArbreFichiers(null, null, null, null, nom , true, ligne);
                    noeudCourant.addFils(nouveauFils);
                    //les cas de dossier et fichiers ont été déjà traités donc ce mot 'fin'  ne correspond pas à un nom de dossier ou fichier
                }else if(tab.length>1 && tab[1].equals("fin")){
                    noeudCourant = noeudCourant.getPere();
                }
            }
            lecteur.close(); 
        }catch(IOException e){
            System.out.println("un problème avec le fichier texte lors de crération de l'arborescence initiale");
        }

        //ici le noeudCourant retourné sera la racine ("d'après le format de notre fichier texte ")
        return noeudCourant;
    }
}
