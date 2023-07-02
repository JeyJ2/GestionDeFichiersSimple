import java.util.Scanner;

public class Main {

    //méthode auxiliaire qui modifie le nom d'un dossier ou fichier
    public static void methodeRename(ArbreFichiers dossier, String nom, String nouveauNom){
        ArbreFichiers element = dossier.cheminRelatif(nom);
        if(element!=null){
            element.setNom(nouveauNom);
        }else{
            System.out.println("Aucun dossier ou fichier ne porte ce nom ! "+"\n");
        }
    }

    //méthode auxiliaire qui affiche le contenu d'un fichier
    public static void methodeLess(ArbreFichiers dossier, String nom){
        ArbreFichiers element = dossier.cheminRelatif(nom);
        if(element!=null && element.estUnFichier()){
            String [] lignes = element.getContenu().split("___");
            for(int i=0;i<lignes.length;i++){
                System.out.println(lignes[i]);
            }
        }else{
            System.out.println("Attention il n'y a auccun fichier portant ce nom dans ce dossier courant"+"\n");
        }
    }



    public static void commande(ArbreFichiers initiale){
        String Newligne=System.getProperty("line.separator");  //pour faire des retours à la ligne (selon l'OS)
        Scanner sc = new Scanner(System.in);
        boolean continuer = true;
        ArbreFichiers dossierCourant = initiale;
        while(continuer){
            String [] tab=null; 
            boolean conforme = false;
            //une boucle do while pour éviter des saisies incorrectes
            do{ 
                if(dossierCourant==initiale){
                    System.out.println("------dossier courant : Racine. Entrez une nouvelle commande>>>>"+Newligne);
                }else{
                    System.out.println("------dossier courant : "+dossierCourant.pwdChemin()+". Entrez une nouvelle commande>>>>"+Newligne);
                }
                String ligne = sc.nextLine();
                tab = ligne.split(" ");
               if(tab.length==0 || tab[0].equals("")){
                    //le cas où on n'a rien saisi ou que des espaces ou on commence par des espaces
                    System.out.println("Saisie invalide");
                }else if(tab.length>3){
                    System.out.println("Format commande non respecté : dans notre programme auccune commande avec plus de 2 arguments");
                }else{
                    conforme = true;
                }
            }while(!conforme);

            //on récupere la commande avec l'indice 0 du tableau
            String cmd = tab[0];
            //on fait un switch pour pouvoir exécuter les commandes
            switch(cmd){
                case "ls":
                    if(tab.length>1){
                        System.out.println("commande invalide : dans notre programme la commande 'ls' ne prend pas d'arguments"+Newligne);
                    }else{
                        System.out.println(dossierCourant.lsContenu());
                    }
                    break;
                case "cd":
                    if(tab.length!=2){
                        System.out.println("commande invalide : dans notre programme la commande 'cd' prend 1 argument"+Newligne);
                    }else{
                        if(tab[1].equals("..") && dossierCourant==initiale){
                            System.out.println("vous êtes déjà dans le dossier racine, impossible de remonter"+Newligne);
                        }else if(dossierCourant.cheminRelatif(tab[1])==null){
                            System.out.println("le dossier "+tab[1]+" n'existe pas dans le dossier courant"+Newligne);
                        }else if(dossierCourant.cheminRelatif(tab[1]).estUnFichier()){
                            System.out.println("Attention : Vous avez entré le nom d'un fichier au lieu de dossier"+Newligne);
                        }else{
                            dossierCourant = dossierCourant.cheminRelatif(tab[1]);
                        }
                    }
                    break;
                case "mkdir":
                    if(tab.length!=2){
                        System.out.println("commande invalide : dans notre programme la commande 'mkdir' prend 1 argument : nom de dossier à créer sans espaces"+Newligne);
                    }else{
                        ArbreFichiers nouveauFils = new ArbreFichiers(null, null, null, null, tab[1], false, null);
                        dossierCourant.addFils(nouveauFils);
                    }
                    break;
                case "mkfile":
                    if(tab.length!=2){
                        System.out.println("commande invalide : dans notre programme la commande 'mkfile' prend 1 argument : nom de fichier a créer sans espaces"+Newligne);
                    }else{
                        System.out.println("Contenu de votre fichier ?");
                        String contenu = sc.nextLine();
                        ArbreFichiers nouveauFils = new ArbreFichiers(null, null, null, null, tab[1], true, contenu);
                        dossierCourant.addFils(nouveauFils);
                    }
                    break;
                case "less":
                    if(tab.length!= 2){
                        System.out.println("commande invalide : dans notre programme la commande 'less' prend 1 argument : nom du fichier à afficher"+Newligne);
                    }else{
                        //une méthode auxiliaire pour traiter ce cas
                        methodeLess(dossierCourant, tab[1]);
                        System.out.println();
                    }
                    break;
                case "pwd":
                    if(tab.length>1){
                        System.out.println("commande invalide : dans notre programme la commande 'pwd' ne prend pas d'arguments"+Newligne);
                    }else{
                        System.out.println(dossierCourant.pwdChemin()+Newligne);
                    }
                    break;
                case "locate":
                    if(tab.length!=2){
                        System.out.println("commande invalide : dans notre programme la commande 'locate' prend 1 argument : nom du fichier à rechercher"+Newligne);
                    }else{
                        System.out.println(initiale.nomLocate(tab[1]));
                    }
                    break;
                case "grep":
                    if(tab.length!=2){
                        System.out.println("commande invalide : dans notre programme la commande 'grep' prend 1 argument : le mot à rechercher"+Newligne);
                    }else{
                        System.out.println(initiale.chercheGrep(tab[1]));
                    }
                    break;
                case "rename":
                    if(tab.length!=3){
                        System.out.println("commande invalide : dans notre programme la commande 'rename' prend 2 arguments : nom du fichier à renommer et le nouveau nom"+Newligne);
                    }else{
                        methodeRename(dossierCourant, tab[1], tab[2]);
                    }
                    break;
                case "rm":
                    if(tab.length!=2){
                        System.out.println("commande invalide : dans notre programme la commande 'rm' prend 1 argument : nom du fichier à supprimer"+Newligne);
                    }else{
                        //element renvoyé par la méthode cheminRelatif est null s'il n'y a pas d'élement portant ce nom
                        ArbreFichiers element = dossierCourant.cheminRelatif(tab[1]);
                        if(element!=null){
                            element.supprimer();
                        }else{ 
                            System.out.println("Aucun dossier ou fichier ne porte ce nom ! "+Newligne);
                        }
                    }
                    break;
                case "quit":
                    if(tab.length>1){
                        System.out.println("commande invalide : dans notre programme la commande 'quit' ne prend pas d'arguments");
                    }else{
                        System.out.println("Fin du programme");
                        continuer = false;
                    }
                    break;
                case "exit":
                     if(tab.length>1){
                        System.out.println("commande invalide : dans notre programme la commande 'quit' ne prend pas d'arguments");
                    }else{
                        System.out.println("Fin du programme");
                        continuer = false;
                    }
                    break;
                //toutes les autres commandes sont inconnues pour ce programme
                default:
                    System.out.println("Commande inconnue");
                    break;
            }

        }
        sc.close();
    }

    public static void main(String[] args) {
        //on initialise l'arbre de départ à null
        ArbreFichiers initiale = null;
        if(args.length>=2){
            System.out.println("Attention! Ce programme prend 0 argument ou 1 seul argument qui est le nom du fichier texte");
        }else if(args.length==0){
            //si on entre rien comme référence de fichier texte alors un dossier racine vide est crée
            initiale = new ArbreFichiers(null, null, null, null, "", false, null);
        }else{
            //sinon c'est las classe ArborenseInitiale qui construit l'arbre de départ avec BufferReader
            initiale = ArborescenceInitiale.lectureFichierTxt(args[0]);
        }
        //s'il n'y a pas de soucis avec l'arbre de départ on appelle le gestionnaire de commandes 
        if(initiale!=null){
        commande(initiale);
        }
    }
  
}
