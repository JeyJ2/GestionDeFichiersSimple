public class ArbreFichiers{
    private ArbreFichiers pere;
    private ArbreFichiers premierFils;
    private ArbreFichiers frereDroit;
    private ArbreFichiers frereGauche;
    private String nom; //pas d'espaces
    private boolean estFichier; //ou dossier
    private String contenu; //null si c'est un dossier
    private int taille; //nbr de caract du contenu si fichier sinon la somme


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
        while(element!=null){
            element.taille -= this.taille;
            element = element.pere;
        }

        this.pere=null;
        this.frereDroit=null;
        this.frereGauche=null;
    }

    public String lsContenu(){ //méthode 3
        String Newligne=System.getProperty("line.separator");
        String res=null;
        if(this.pere==null){
            res="Voici le contenu de : la Racine "+this.taille+ " octets "+Newligne;
        }else{
            res = "Voici la liste du contenu de : "+this.nom+" "+this.taille+ " octets "+Newligne;
        }
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

    public String pwdChemin(){ //méthode 4
        String res = this.nom;
        ArbreFichiers element = this.pere;
        while(element!=null){
            res = element.nom+"/"+res;
            element = element.pere;
        }
        return res;
    }
    public ArbreFichiers cheminRelatif(String nom){ //méthode 5
        if(nom.equals("..")) return this.pere;
        ArbreFichiers element=this.premierFils;
        while(element!=null && !element.nom.equals(nom)){
            element = element.frereDroit;
        }
        return element;
        //dans le cas où auccun des fils porte le nom cherché, element=null est renvoyé
    }
    public void setNom(String nom){
        ArbreFichiers pere = this.pere;
        this.supprimer();
        this.nom = nom;
        pere.addFils(this);
    }

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

    public String chercheGrep(String text){
        String res = "";
        if(this.estFichier && this.contenu.contains(text)){
            res += this.pwdChemin()+"\n";
        }else{
            ArbreFichiers element = this.premierFils;
            while(element!=null){
                res += element.nomLocate(text);
                element = element.frereDroit;
            }
        }
        return res;
    }











}