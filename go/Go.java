package go;
public class Go {
   public static myWindow mainWindow = new myWindow();

   public static Goban getGoban() {
      return mainWindow.goban;
   }

   public static void main(String[] args) {
      mainWindow.displayMenu();
   }
}
