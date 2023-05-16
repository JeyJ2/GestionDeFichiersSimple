public class ArbreFichiers{
    private ArbreFichiers pere;
    private ArbreFichiers premierFils;
    private ArbreFichiers frereDroit;
    private ArbreFichiers frereGauche;
    private String nom; //pas d'espaces
    private boolean estFichier; //ou dossier
    private String contenu; //null si c'est un dossier
    private int taille; //nbr de caract du contenu si fichier sinon la somme

    public ArbreFichiers getPere(){
        return pere;
    }
    public boolean estUnFichier(){
        return estFichier;
    }

    public String getContenu(){
        return contenu;
    }

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
        this.taille = contenu.length();
    }

    public void addFils(ArbreFichiers nouveauFils){ //méthode 1
        if(nouveauFils== null){
            throw new IllegalArgumentException();
        }
        nouveauFils.pere = this;
        if(this.premierFils==null){
            this.premierFils = nouveauFils;
        }
        else{
            ArbreFichiers element = this.premierFils;
            boolean dernier = false;
            while(!dernier && element.nom.compareToIgnoreCase(nouveauFils.nom)<=0){
                if(element.frereDroit==null) dernier = true;
                else element = element.frereDroit;
            }
            if(dernier){
                element.frereDroit=nouveauFils;
                nouveauFils.frereGauche =element;
                nouveauFils.frereDroit = null;
            }else{
                nouveauFils.frereDroit = element;
                nouveauFils.frereGauche = element.frereGauche;
                element.frereGauche.frereDroit = nouveauFils;
                element.frereGauche = nouveauFils;
                if(this.premierFils==element){
                    this.premierFils = nouveauFils;
                }
            }
        }

        ArbreFichiers element = this; 
        while(element!=null){
            element.taille += nouveauFils.taille;
            element = element.pere;
        }



    }

    public void supprimer(){ //méthode 2
        ArbreFichiers p = this.pere;
        ArbreFichiers g = this.frereGauche;
        ArbreFichiers d = this.frereDroit;
    //le cas où le dossier à supprimer est la racine ?? appeler .premierFils lorsque p =null

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
        ArbreFichiers element = p;
        while(p!=null){
            element.taille -= this.taille;
            element = element.pere;
        }

        this.pere=null;
        this.frereDroit=null;
        this.frereGauche=null;
    }

    public String lsContenu(){ //méthode 3
        String res="";
        ArbreFichiers element= this.premierFils;
        while(element!=null){
            if(element.estFichier){
                res="f ";
            }else{
                res="d ";
            }
            res+=element.nom;
            res+=" "+element.taille+" octets \n";
        }
        return res;
    }

    public String pwdChemin(){ //méthode 4
        String res = this.nom;
        ArbreFichiers element = this.pere;
        while(element!=null){
            res = element.nom+"/"+res;
            element = element.pere;
        }
        return res;
    }
    










}