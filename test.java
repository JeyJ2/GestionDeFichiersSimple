public class test {

    public static void main(String[] args) {
        ArbreFichiers initiale = ArborescenceInitiale.lectureFichierTxt("exemple.txt");
        System.out.println(initiale.getTaille());
        System.out.println(initiale.lsContenu() );
        System.out.println(initiale.getPremierFils().lsContenu());
    }
  
}
