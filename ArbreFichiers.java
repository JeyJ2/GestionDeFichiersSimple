public class ArbreFichiers{
    private ArbreFichiers pere;
    private ArbreFichiers premierFils;
    private ArbreFichiers frereDroit;
    private ArbreFichiers frereGauche;
    private String nom; //pas d'espaces
    private boolean estFichier; //ou dossier
    private String contenu; //null si c'est un dossier
    private int taille; //nbr de caract du contenu si fichier sinon la somme

    //un constructeur avec tous les paramètres j'aurais pu ne pas prendre pere, premierFils, frereDroit, frereGauche en arguments
    //on peut les initilaliser à null car quand on crée un nouveau fichier ou dossier ces éléments sont null normalement
    public ArbreFichiers(
        ArbreFichiers pere,
        ArbreFichiers premierFils,
        ArbreFichiers frereDroit,
        ArbreFichiers frereGauche,
        String nom,
        boolean estFichier,
        String contenu
    ) {
        this.pere = pere;
        this.premierFils = premierFils;
        this.frereDroit = frereDroit;
        this.frereGauche = frereGauche;
        this.nom = nom;
        this.estFichier = estFichier;
        this.contenu = contenu;
        //si c'est un dossier le contenu est null donc taille=0
        if(contenu!=null) this.taille = contenu.length(); else this.taille = 0;
    }

    public ArbreFichiers getPere(){
        return pere;
    }
    public boolean estUnFichier(){
        return estFichier;
    }

    public String getContenu(){
        return contenu;
    }

    public int getTaille(){
        return taille;
    }

    public String getNom(){
        return nom;
    }

    public ArbreFichiers getPremierFils(){
        return premierFils;
    }

    //méthode 1 pour ajouter un nouveau fichier ou un nouveau dossier au dossier courant
    //cette fonction sera appelée uniquement s'il s'git d'un dossier
    public void addFils(ArbreFichiers nouveauFils){ 
        if(nouveauFils== null){
            throw new IllegalArgumentException();
        }
        nouveauFils.pere = this; //définition du père
        if(this.premierFils==null){ //si le dossier est vide le nouveauFils sera le premierFils
            this.premierFils = nouveauFils; 
        }
        else{
            ArbreFichiers element = this.premierFils;
            boolean dernier = false;
            //on insère le nouveauFils dans un bon ordre alphabetique
            while(!dernier && element.nom.compareToIgnoreCase(nouveauFils.nom)<=0){
                if(element.frereDroit==null) dernier = true;
                else element = element.frereDroit; 
            }
            if(dernier){
                element.frereDroit=nouveauFils;
                nouveauFils.frereGauche =element;
                nouveauFils.frereDroit = null; //pas nécessaire
            }else{
                nouveauFils.frereDroit = element;
                nouveauFils.frereGauche = element.frereGauche;
                if(element.frereGauche!=null) element.frereGauche.frereDroit = nouveauFils;
                element.frereGauche = nouveauFils;
                if(this.premierFils==element){
                    this.premierFils = nouveauFils;
                }
            }
        }

        //on met à jour la taille jusqu'au niveau de la racine
        ArbreFichiers element = this; 
        while(element!=null){
            element.taille += nouveauFils.taille;
            element = element.pere;
        }



    }

    //méthode pour supprimer un fichier ou un dossier 
    public void supprimer(){ //méthode 2
        ArbreFichiers p = this.pere;
        ArbreFichiers g = this.frereGauche;
        ArbreFichiers d = this.frereDroit;
        //on ne supprime pas la racine
        //on fixe les frere droit et gauche selon les cas "d'existence"

        if(g==null){
            if(d==null) p.premierFils = null;
            else p.premierFils = d;
        }else{
            if(d==null) g.frereDroit = null;
            else{
                g.frereDroit = d;
                d.frereGauche = g;
            }
        }

        //on met à jour la taille
        ArbreFichiers element = p;
        while(element!=null){
            element.taille -= this.taille;
            element = element.pere;
        }
        //on met à null tous les réferences extérieures de l'élement supprimé
        this.pere=null;
        this.frereDroit=null;
        this.frereGauche=null;
    }

    //méthode pour tester le contenu des dossiers
    public String lsContenu(){ //méthode 3
        String Newligne=System.getProperty("line.separator");
        String res=null;
        if(this.pere==null){
            res="Voici le contenu de : la Racine "+this.taille+ " octets "+Newligne;
        }else{
            res = "Voici la liste du contenu de : "+this.nom+" "+this.taille+ " octets "+Newligne;
        }
        //on commence par le premier fils on fixe la lettre d ou f selon le cas
        ArbreFichiers element= this.premierFils;
        while(element!=null){
            if(element.estFichier){
                res+="f ";
            }else{
                res+="d ";
            }
            res+=element.nom;
            res+=" "+element.taille+" octets "+Newligne;
            element = element.frereDroit;
        }
        return res;
    }

    //méthode qui retourne le chemin complet d'un dossier ou d'un fichier
    public String pwdChemin(){ //méthode 4
        String res = this.nom;
        ArbreFichiers element = this.pere;
        while(element!=null){
            res = element.nom+"/"+res;
            element = element.pere;
        }
        return res;
    }

    //méthode qui retourne la référence de l'élément contenu dans le dossier
    public ArbreFichiers cheminRelatif(String nom){ //méthode 5
        if(nom.equals("..")) return this.pere;
        ArbreFichiers element=this.premierFils;
        while(element!=null && !element.nom.equals(nom)){
            element = element.frereDroit;
        }
        return element;
        //dans le cas où auccun des fils porte le nom cherché, element=null est renvoyé
    }

    //on modifie le nom d'un dossier ou fichier
    //on supprimer et on réinsere car l'ordre alphébétique n'est pas garantie sinon
    public void setNom(String nom){
        ArbreFichiers pere = this.pere;
        this.supprimer();
        this.nom = nom;
        pere.addFils(this);
    }

    //méthode qui liste les fichiers ou dossiers dont le nom contient le paramètre
    public String nomLocate(String nom){
        String res = "";
        if(this.nom.contains(nom)){
            res += this.pwdChemin()+"\n";
        }
        ArbreFichiers element = this.premierFils;
        while(element!=null){
            res += element.nomLocate(nom);
            element = element.frereDroit;
        }
        return res;
    }

    //méthode qui liste des fichiers dont le contenu possède le text passé en paramètre
    public String chercheGrep(String text){
        String res = "";
        if(this.estFichier && this.contenu.contains(text)){
            res += this.pwdChemin()+"\n";
        }else{
            ArbreFichiers element = this.premierFils;
            while(element!=null){
                res += element.chercheGrep(text);
                element = element.frereDroit;
            }
        }
        return res;
    }
}