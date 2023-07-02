import java.util.Scanner;

public class Test {



    public static void commande(ArbreFichiers initiale){
        Scanner sc = new Scanner(System.in);
        boolean continuer = true;
        ArbreFichiers dossierCourant = initiale;
        while(continuer){
            String [] tab=null; 
            boolean conforme = false;
            //une boucle do while pour éviter des saisies incorrectes
            do{ 
                if(dossierCourant==initiale){
                    System.out.println("------dossier courant : Racine. Entrez une nouvelle commande>>>>");
                }else{
                    System.out.println("------dossier courant : "+dossierCourant.pwdChemin()+". Entrez une nouvelle commande>>>>");
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
            switch(cmd){
                case "ls":
                    if(tab.length>1){
                        System.out.println("commande invalide : dans notre programme la commande 'ls' ne prend pas d'arguments");
                    }else{
                        System.out.println(dossierCourant.lsContenu());
                    }
                    break;
                case "cd":
                    if(tab.length!=2){
                        System.out.println("commande invalide : dans notre programme la commande 'cd' prend 1 argument");
                    }else{
                        if(tab[1].equals("..") && dossierCourant==initiale){
                            System.out.println("vous êtes déjà dans le dossier racine, impossible de remonter");
                        }else if(dossierCourant.cheminRelatif(tab[1])==null){
                            System.out.println("le dossier "+tab[1]+" n'existe pas dans le dossier courant");
                        }else if(dossierCourant.cheminRelatif(tab[1]).estUnFichier()){
                            System.out.println("Attention : Vous avez entré le nom d'un fichier au lieu de dossier");
                        }else{
                            dossierCourant = dossierCourant.cheminRelatif(tab[1]);
                        }
                    }
                    break;
                case "mkdir":
                    if(tab.length!=2){
                        System.out.println("commande invalide : dans notre programme la commande 'mkdir' prend 1 argument : nom de dossier à créer sans espaces");
                    }else{
                        ArbreFichiers nouveauFils = new ArbreFichiers(null, null, null, null, tab[1], false, null);
                        dossierCourant.addFils(nouveauFils);
                    }
                    break;
                case "mkfile":
                    if(tab.length!=2){
                        System.out.println("commande invalide : dans notre programme la commande 'mkfile' prend 1 argument : nom de fichier a créer sans espaces");
                    }else{
                        System.out.println("Contenu de votre fichier ?");
                        String contenu = sc.nextLine();
                        ArbreFichiers nouveauFils = new ArbreFichiers(null, null, null, null, tab[1], true, contenu);
                        dossierCourant.addFils(nouveauFils);
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
                default:
                    System.out.println("Commande inconnue");
                    break;
            }

        }
        sc.close();
    }

    public static void main(String[] args) {
        ArbreFichiers initiale = ArborescenceInitiale.lectureFichierTxt(args[0]);
        commande(initiale); 
    }
  
}
