public class Tables {
   public Tables() {
      loadE();
      loadL();
      loadInv();
   }

   public byte[] E = new byte[256]; // "exp" table (base 0x03)
   public byte[] L = new byte[256]; // "Log" table (base 0x03)
   public byte[] S = new byte[256]; // SubBytes table
   public byte[] invS = new byte[256]; // inverse of SubBytes table
   public byte[] inv = new byte[256]; // multiplicative inverse table
   public byte[] powX = new byte[15]; // powers of x = 0x02
   private String[] dig = {"0","1","2","3","4","5","6","7",
                           "8","9","a","b","c","d","e","f"};

   // FFMulFast: fast multiply using table lookup
   public byte FFMulFast(byte a, byte b){
      int t = 0;;
      if (a == 0 || b == 0) return 0;
      t = (L[(a & 0xff)] & 0xff) + (L[(b & 0xff)] & 0xff);
      if (t > 255) t = t - 255;
      return E[(t & 0xff)];
   }

   // hex: print a byte as two hex digits
   public String hex(byte a) {
      return dig[(a & 0xff) >> 4] + dig[a & 0x0f];
   }

   // hex: print a single digit (for tables)
   public String hex(int a) {
      return dig[a];
   }

   // loadE: create and load the E table
   public void loadE() {
      byte x = (byte)0x01;
      int index = 0;
      E[index++] = (byte)0x01;
      for (int i = 0; i < 255; i++) {
         byte y = FFMul(x, (byte)0x03);
         E[index++] = y;
         x = y;
      }
   }

   // loadL: load the L table using the E table
   public void loadL() { // careful: had 254 below several places
      int index;
      for (int i = 0; i < 255; i++) {
          L[E[i] & 0xff] = (byte)i;
      }
   }

   // loadInv: load in the table inv
   public void loadInv() {
      int index;
      for (int i = 0; i < 256; i++)
          inv[i] = (byte)(FFInv((byte)(i & 0xff)) & 0xff);
   }

   // FFInv: the multiplicative inverse of a byte value
   public byte FFInv(byte b) {
      byte e = L[b & 0xff];
      return E[0xff - (e & 0xff)];
   }

   // printTable: print a 256-byte table
   public void printTable(byte[] S, String name) {
      for (int i = 0; i < 256; i++) {
         System.out.println(Integer.toHexString(i).toUpperCase() + "," + hex(S[i]).toUpperCase());
      }
   }
   // printInv: print the inv table
   public void printInv() {
      printTable(inv, "inv");
   }

   public static void main(String[] args) {
      Tables sB = new Tables();
      sB.printInv();
   }
}
