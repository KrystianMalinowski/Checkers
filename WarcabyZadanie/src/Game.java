import java.util.Scanner;

class Ascii{
	static int BIALE_POLE = 11035;
	static int CZARNE_POLE = 11036;
	static int BIALY_PION = 9817;
	static int CZARNY_PION = 9823;
	static int BIALA_DAMKA = 9813;
	static int CZARNA_DAMKA = 9819;
}
class OpcjeGry{
	static boolean koniecGry= false;
	static boolean turaGracza1 = true;
	static boolean nastepnyRuch = false;
	static boolean ruchPoprawny = false;
	static boolean remis = false;
	static int remis1=0;
	static int remis0=0;
}

public class Game {

	public static void main(String[] args) {
		
		long[] players = new long[4];
		
		players[0]= 0b101111111101111101101111100101111001101110110101110011L;
		players[1]= 0b101110010101110000101101111101101101101101100101101001L;
		players[2]= 0b100010110100010011100010010100010000100001111100001101L;
		players[3]= 0b100001100100001001100000110100000011100000010100000000L;

		
		Scanner in = new Scanner(System.in);
		String komenda ="";
		
		while(OpcjeGry.koniecGry!=true) {
//			System.out.println(OpcjeGry.turaGracza1);
			wyswietlPlansze(players);
			if(OpcjeGry.turaGracza1==true) {
				System.out.println("Ruch wykonuje gracz 1 (xy_xy):");
			}
			else
				System.out.println("Ruch wykonuje gracz 2 (xy_xy):");
			komenda=in.nextLine();
			ruch(players, komenda);
			while(OpcjeGry.nastepnyRuch==true) {
				String skadZaczac=komenda.substring(3);
				System.out.println("Ruch wykonany poprawnie!");
				System.out.println("Mozliwy kolejny ruch!");
				if(OpcjeGry.turaGracza1==true)
					System.out.println("Ruch wykonuje gracz 1 (xy_xy):");
				else
					System.out.println("Ruch wykonuje gracz 2 (xy_xy):");
				System.out.println("Musisz zaczac od "+skadZaczac);
				wyswietlPlansze(players);
				komenda=in.nextLine();
				while(komenda.substring(0,2)==skadZaczac) {
					System.out.println(komenda.substring(0,2));
					System.out.println("Sprobuj ponownie");
					komenda=in.nextLine();
				}
				ruch(players, komenda);
			}
			if(OpcjeGry.remis0==15&&OpcjeGry.remis1==15) {
				System.out.println("Gra konczy sie remisem");
				OpcjeGry.koniecGry=true;
			}
			else if(OpcjeGry.turaGracza1==true) {
				OpcjeGry.koniecGry=czySaPionkiGraczaA(players);
				if(OpcjeGry.koniecGry==true)
					System.out.println("Wygrywa gracz 2!");
			}
			else if (OpcjeGry.turaGracza1==false)
				OpcjeGry.koniecGry=czySaPionkiGraczaB(players);
			if(OpcjeGry.koniecGry==true)
				System.out.println("Wygrywa gracz 1!");
			
		}
	}
	
	
	public static void wyswietlPlansze(long[] players) {
		System.out.print(" ");
		for(int i =0;i<8;i++) {
			System.out.print(i);
		}
		System.out.println(" ");
		
		for(int i =0;i<8;i++) {
			System.out.print(i);
			int j=0;
			for(;j<8;j++) {
					if(j%2==0&&i%2==0||j%2==1&&i%2==1) {
						if(czyJestBialyPion(players, j, i)!=0)
							System.out.print(String.format("%c", Ascii.BIALY_PION));
						else if(czyJestCzarnyPion(players, j, i)!=0)
							System.out.print(String.format("%c", Ascii.CZARNY_PION));
						else if(czyJestBialaDamka(players, j, i)!=0)
							System.out.print(String.format("%c", Ascii.BIALA_DAMKA));
						else if(czyJestCzarnaDamka(players, j, i)!=0)
							System.out.print(String.format("%c", Ascii.CZARNA_DAMKA));
						else
							System.out.print(String.format("%c", Ascii.CZARNE_POLE));
					}
				else
					System.out.print(String.format("%c", Ascii.BIALE_POLE));
			}
			System.out.print(i);
			System.out.println(" ");
		}
		System.out.print(" ");
		for(int i =0;i<8;i++) {
			System.out.print(i);
		}
		System.out.println(" ");
	}
	
	
	
	public static int czyJestBialyPion(long[] players, int x, int y) {
		int ascii = 0;
		int[] ko =zmianaKoordynatow(x, y);
		
		for(int i=0;i<players.length/2;i++) {
			int j=0;
			for(;j<6;j++) {
				long playerstmp = players[i];
				playerstmp &=~((long)1<<(8+j*9));
				playerstmp|=((long)1<<(7+j*9));
				long sprawdzenie =0b0L;
				sprawdzenie |=((long)1<<(8+j*9) | (long)1<<(7+j*9));
				if((players[i] ^ playerstmp) == sprawdzenie) {
					long playerstmp1 = players[i];
					int pozycja = 5;
					for(int k=0;k<ko.length;k++) {
						if(ko[k]>0)
							playerstmp1 &=~((long)1<<pozycja+j*9);
						else
							playerstmp1 |=((long)1<<pozycja+j*9);
						
						pozycja--;
					}
					long sprawdzenie2 = 0b0L;
					for(int k=0;k<ko.length;k++) {
						sprawdzenie2 |=((long)1<<k+j*9);
					}
					if((players[i]^playerstmp1)==sprawdzenie2) {
						ascii=Ascii.BIALY_PION;
					}
					
				}
			}
		}
		
		return ascii;
	}
	public static int czyJestBialaDamka(long[] players, int x, int y) {
		int ascii = 0;
		int[] ko =zmianaKoordynatow(x, y);
		
		for(int i=0;i<players.length/2;i++) {
			int j=0;
			for(;j<6;j++) {
				long playerstmp = players[i];
				playerstmp &=~((long)1<<(8+j*9) | (long)1<<(7+j*9));
				long sprawdzenie =0b0L;
				sprawdzenie |=((long)1<<(8+j*9) | (long)1<<(7+j*9));
				if((players[i] ^ playerstmp) == sprawdzenie) {
					long playerstmp1 = players[i];
					int pozycja = 5;
					for(int k=0;k<ko.length;k++) {
						if(ko[k]>0)
							playerstmp1 &=~((long)1<<pozycja+j*9);
						else
							playerstmp1 |=((long)1<<pozycja+j*9);
						
						pozycja--;
					}
					long sprawdzenie2 = 0b0L;
					for(int k=0;k<ko.length;k++) {
						sprawdzenie2 |=((long)1<<k+j*9);
					}
					if((players[i]^playerstmp1)==sprawdzenie2) {
						ascii=Ascii.BIALA_DAMKA;
					}
					
				}
			}
		}
		
		return ascii;
	}
	
	public static int czyJestCzarnyPion(long[] players, int x, int y) {
		int ascii = 0;
		int[] ko =zmianaKoordynatow(x, y);
		
		for(int i=2;i<players.length;i++) {
			int j=0;
			for(;j<6;j++) {
				long playerstmp = players[i];
				playerstmp &=~((long)1<<(8+j*9));
				playerstmp|=((long)1<<(7+j*9));
				long sprawdzenie =0b0L;
				sprawdzenie |=((long)1<<(8+j*9) | (long)1<<(7+j*9));
				if((players[i] ^ playerstmp) == sprawdzenie) {
					long playerstmp1 = players[i];
					int pozycja = 5;
					for(int k=0;k<ko.length;k++) {
						if(ko[k]>0)
							playerstmp1 &=~((long)1<<pozycja+j*9);
						else
							playerstmp1 |=((long)1<<pozycja+j*9);
						
						pozycja--;
					}
					long sprawdzenie2 = 0b0L;
					for(int k=0;k<ko.length;k++) {
						sprawdzenie2 |=((long)1<<k+j*9);
					}
					if((players[i]^playerstmp1)==sprawdzenie2) {
						ascii=Ascii.CZARNY_PION;
					}
					
				}
			}
		}
		
		return ascii;
	}
	public static int czyJestCzarnaDamka(long[] players, int x, int y) {
		int ascii = 0;
		int[] ko =zmianaKoordynatow(x, y);
		
		for(int i=2;i<players.length;i++) {
			int j=0;
			for(;j<6;j++) {
				long playerstmp = players[i];
				playerstmp &=~((long)1<<(8+j*9) | (long)1<<(7+j*9));
				long sprawdzenie =0b0L;
				sprawdzenie |=((long)1<<(8+j*9) | (long)1<<(7+j*9));
				if((players[i] ^ playerstmp) == sprawdzenie) {
					long playerstmp1 = players[i];
					int pozycja = 5;
					for(int k=0;k<ko.length;k++) {
						if(ko[k]>0)
							playerstmp1 &=~((long)1<<pozycja+j*9);
						else
							playerstmp1 |=((long)1<<pozycja+j*9);
						
						pozycja--;
					}					
					long sprawdzenie2 = 0b0L;
					for(int k=0;k<ko.length;k++) {
						sprawdzenie2 |=((long)1<<k+j*9);
					}
					if((players[i]^playerstmp1)==sprawdzenie2) {
						ascii=Ascii.CZARNA_DAMKA;
					}
					
				}
			}
		}
		
		return ascii;
	}
	
	
	public static int[] zmianaKoordynatow(int x, int y) {
		String koordynaty = "";
		int[] koordynatyInt = new int[6];
		
		if(y==0)
			koordynaty+="000";
		else if(y==1)
			koordynaty+="001";
		else if(y==2)
			koordynaty+="010";
		else if(y==3)
			koordynaty+="100";
		else if(y==4)
			koordynaty+="011";
		else if(y==5)
			koordynaty+="101";
		else if(y==6)
			koordynaty+="110";
		else if(y==7)
			koordynaty+="111";
		
		if(x==0)
			koordynaty+="000";
		else if(x==1)
			koordynaty+="001";
		else if(x==2)
			koordynaty+="010";
		else if(x==3)
			koordynaty+="100";
		else if(x==4)
			koordynaty+="011";
		else if(x==5)
			koordynaty+="101";
		else if(x==6)
			koordynaty+="110";
		else if(x==7)
			koordynaty+="111";
		
		for(int i=0;i<koordynatyInt.length;i++) {
			koordynatyInt[i]=Character.getNumericValue(koordynaty.charAt(i));
		}
		
		return koordynatyInt;
	}
	
	public static long[] ruch(long[] players, String ruch) {
//		System.out.println(OpcjeGry.turaGracza1);
		int x1=Character.getNumericValue(ruch.charAt(0));
		int y1=Character.getNumericValue(ruch.charAt(1));
		
		int x2=Character.getNumericValue(ruch.charAt(3));
		int y2=Character.getNumericValue(ruch.charAt(4));
		
		int[] koordynaty1=zmianaKoordynatow(x1, y1);
		int[] koordynaty2=zmianaKoordynatow(x2, y2);
		int gracz;
		if(OpcjeGry.turaGracza1==true) {
			gracz = 1;
		}
		else {
			gracz = 0;
		}
//		System.out.println("ruch");
		//Czy jest bierka
		int bierka =czyJestBierka(players, koordynaty1, gracz);
		if(bierka<2) {
			//Czy jest puste pole
			if(czyPoleJestPuste(players, koordynaty2)==true){
				//Czy ruch jest dozwolony
//				System.out.println("Krotki ruch - "+krotkiRuch(y1, y2, gracz, bierka));
				if(krotkiRuch(y1, y2, gracz, bierka)) {
					przestawienieBierek(players, koordynaty1, koordynaty2,y2,gracz,bierka);
				}
				else
					bicieBierek(players, koordynaty1, koordynaty2, gracz, y1, y2, x1, x2, bierka);
			}
		}
		return players;
	}

	public static int czyJestBierka(long[] players, int[] koordynaty, int gracz) {
		int bierka= 2;
//		System.out.println("Czy jest bierka");
		
		for(int i=0;i<players.length;i++) {
			int j=0;
			for(;j<6;j++) {
				long playerstmp = players[i];
				playerstmp &=~((long)1<<(8+j*9));
				if(gracz>0)
					playerstmp &=~((long)1<<6+j*9);
				else
					playerstmp |=((long)1<<6+j*9);
				int pozycja=5;
				for(int k=0;k<koordynaty.length;k++) {
					if(koordynaty[k]>0)
						playerstmp &=~((long)1<<pozycja+j*9);
					else
						playerstmp |=((long)1<<pozycja+j*9);
					
					pozycja--;
				}
				long sprawdzenie = 0b0L;
				for(int k=0;k<koordynaty.length;k++) {
					sprawdzenie |=((long)1<<k+j*9);
				}
				sprawdzenie |=(((long)1<<6+j*9) | ((long)1<<8+j*9));
				if((players[i]^playerstmp) == sprawdzenie) {
					long players2tmp = players[i];
					players2tmp |=((long)1<<(7+j*9));
					if((players2tmp^players[i])>0) {
						bierka=0;
					}
					else
						bierka=1;
				}
			}
		}
	
	return bierka;
	}
	
	public static boolean czyPoleJestPuste(long[] players, int[] koordynaty) {
//		System.out.println("Czy pole jest puste");
		boolean tak = true;
		
		for(int i=0;i<players.length;i++) {
			int j=0;
			for(;j<6;j++) {
				long playerstmp1=players[i];
				playerstmp1 &=~((long)1<<(8+j*9));
				int pozycja=5;
				for(int k=0;k<koordynaty.length;k++) {
					if(koordynaty[k]>0)
						playerstmp1 &=~((long)1<<pozycja+j*9);
					else
						playerstmp1 |=((long)1<<pozycja+j*9);
					
					pozycja--;
				}					
				long sprawdzenie2 = 0b0L;
				for(int k=0;k<koordynaty.length;k++) {
					sprawdzenie2 |=((long)1<<k+j*9);
				}
				sprawdzenie2 |=((long)1<<8+j*9);
				if((players[i]^playerstmp1)==sprawdzenie2) {
					tak=false;
				}
			}
		}
		return tak;
	}
	public static boolean krotkiRuch(int y1, int y2, int gracz, int bierka) {
//		System.out.println("Krotki ruch");
		boolean tak=false;
			
		if(bierka==0&&gracz==1) {
			if(y1-1==y2) {
				tak=true;
			}
		}
		else if(bierka==0&&gracz==0) {
			if(y1+1==y2) {
				tak=true;
			}
		}
		else if(bierka==1) {
			if(y1-1==y2||y1+1==y2) {
				tak=true;
			}
		}
		
		
		return tak;
	}
	public static long[] przestawienieBierek(long[] players, int[] koordynaty1, int[] koordynaty2, int y2, int gracz, int bierka) {
//		System.out.println("Przestawienie bierek");
		
		for(int i=0;i<players.length;i++) {
			int j=0;
			for(;j<6;j++) {
				long playerstmp=players[i];
				playerstmp &=~((long)1<<(8+j*9));
				int pozycja=5;
				for(int k=0;k<koordynaty1.length;k++) {
					if(koordynaty1[k]>0)
						playerstmp &=~((long)1<<pozycja+j*9);
					else
						playerstmp |=((long)1<<pozycja+j*9);
					
					pozycja--;
				}					
				long sprawdzenie2 = 0b0L;
				for(int k=0;k<koordynaty1.length;k++) {
					sprawdzenie2 |=((long)1<<k+j*9);
				}
				sprawdzenie2 |=((long)1<<8+j*9);
				if((players[i]^playerstmp)==sprawdzenie2) {
					int pozycja1=5;
					for(int k=0;k<koordynaty2.length;k++) {
						if(koordynaty2[k]>0)
							players[i] |=((long)1<<pozycja1+j*9);
						else
							players[i] &=~((long)1<<pozycja1+j*9);
						
						pozycja1--;
					}
					if(gracz==0&&bierka==1) {
						OpcjeGry.remis0++;
					}
					if(gracz==1&&bierka==1) {
						OpcjeGry.remis1++;
					}
					if(gracz==0&&y2==7) {
						players[i] |=((long)1<<7+j*9);
					}
					if(gracz==1&&y2==0) {
						players[i] |=((long)1<<7+j*9);
					}
					OpcjeGry.nastepnyRuch=false;
					if(OpcjeGry.turaGracza1==false) {
						OpcjeGry.turaGracza1=true;
					}
					else {
						OpcjeGry.turaGracza1=false;
					}
				}
			}
		}
		
		return players;
	}
	public static long[] bicieBierek(long[] players, int[] koordynaty1, int[] koordynaty2, int gracz, int y1, int y2, int x1, int x2,int bierka) {
//		System.out.println("Bicie bierki");
		//Sprawdzanie odleglosci x
		boolean tak = false;
//		System.out.println("szukanie odleglosci");
		if(x1>x2) {
			if(x1-x2==2) {
				tak=true;
			}
		}
		else if(x2>x1) {
			if(x2-x1==2)
				tak=true;
		}
		//Szukanie koordynatow bierki
		if(tak==true) {
//			System.out.println("Szukanie koordynatow");
			int x3=(x1+x2)/2;
			int y3=(y1+y2)/2;
			int[] koordynaty3=zmianaKoordynatow(x3, y3);
			
			int gracz2;
			//Sprawdzanie znalezionej bierki
			if(gracz==1)
				gracz2=0;
			else
				gracz2=1;
			
			int bierkaPrzeciwnika = czyJestBierka(players, koordynaty3, gracz2);
//			System.out.println(bierkaPrzeciwnika + "Bierka przeciwnika");
			if(bierkaPrzeciwnika<2) {
//				System.out.println("Zmiana stanu");
				//zmiana stanu i koordynatow
				for(int i=0;i<players.length;i++) {
					int j=0;
					for(;j<6;j++) {
						//bierka przeciwnika
						long playerstmp3=players[i];
						int pozycja3=5;
						for(int k=0;k<koordynaty3.length;k++) {
							if(koordynaty3[k]>0)
								playerstmp3 &=~((long)1<<pozycja3+j*9);
							else
								playerstmp3 |=((long)1<<pozycja3+j*9);
							
							pozycja3--;
						}					
						long sprawdzenie3 = 0b0L;
						for(int k=0;k<koordynaty3.length;k++) {
							sprawdzenie3 |=((long)1<<k+j*9);
						}
						//bierka gracza
						long playerstmp1=players[i];
						int pozycja1=5;
						for(int k=0;k<koordynaty1.length;k++) {
							if(koordynaty1[k]>0)
								playerstmp1 &=~((long)1<<pozycja1+j*9);
							else
								playerstmp1 |=((long)1<<pozycja1+j*9);
							
							pozycja1--;
						}					
						long sprawdzenie1 = 0b0L;
						for(int k=0;k<koordynaty1.length;k++) {
							sprawdzenie1 |=((long)1<<k+j*9);
						}
						if((players[i]^playerstmp3)==sprawdzenie3) {
//							System.out.println("kasacja");
							players[i] &=~((long)1<<8+j*9);
						}
						else if((players[i]^playerstmp1)==sprawdzenie1) {
//							System.out.println("teleport"); 
							if(gracz==0&&y2==7) {
								players[i] |=((long)1<<7+j*9);
							}
							if(gracz==1&&y2==0) {
								players[i] |=((long)1<<7+j*9);
							}
							int pozycja2=5;
							for(int k=0;k<koordynaty2.length;k++) {
								if(koordynaty2[k]>0)
									players[i] |=((long)1<<pozycja2+j*9);
								else
									players[i] &=~((long)1<<pozycja2+j*9);
								
								pozycja2--;
							}
							OpcjeGry.nastepnyRuch=false;
							if(gracz==0) {
								OpcjeGry.turaGracza1=true;
//								System.out.println(OpcjeGry.turaGracza1);
							}
							else {
								OpcjeGry.turaGracza1=false;
//								System.out.println(OpcjeGry.turaGracza1);
							}
						}
					}
				}
//				System.out.println(x2 +" - x2");
//				System.out.println(y2 +" - y2");
//				System.out.println("Bierka -"+ bierka);
				//Szukanie innego bicia
				if(bierka==1||bierka==0&&gracz==1) {
					if(x2-2>=0&&y2-2>=0) {
						int[] koordynaty4 = zmianaKoordynatow(x2-2, y2-2);
						int tak1 = czyJestBierka(players, koordynaty4, gracz2);
						int tak2 = czyJestBierka(players, koordynaty4, gracz);
						if(tak1==2&&tak2==2) {
							int[] koordynaty5 = zmianaKoordynatow(x2-1, y2-1);
							int tak3 = czyJestBierka(players, koordynaty5, gracz2);
							if(tak3<2) {
								OpcjeGry.nastepnyRuch=true;
//								System.out.println("1Z");
								if(gracz==1) {
									OpcjeGry.turaGracza1=true;
//									System.out.println(OpcjeGry.turaGracza1);
								}
								else {
									OpcjeGry.turaGracza1=false;
//									System.out.println(OpcjeGry.turaGracza1);
								}
							}
						}
					}
				}
				if(bierka==1||bierka==0&&gracz==1) {
					if(x2+2<8&&y2-2>=0) {
						int[] koordynaty4 = zmianaKoordynatow(x2+2, y2-2);
						int tak1 = czyJestBierka(players, koordynaty4, gracz2);
						int tak2 = czyJestBierka(players, koordynaty4, gracz);
						if(tak1==2&&tak2==2) {
							int[] koordynaty5 = zmianaKoordynatow(x2+1, y2-1);
							int tak3 = czyJestBierka(players, koordynaty5, gracz2);
							if(tak3<2) {
								OpcjeGry.nastepnyRuch=true;
//								System.out.println("2Z");
								if(gracz==1) {
									OpcjeGry.turaGracza1=true;
//									System.out.println(OpcjeGry.turaGracza1);
								}
								else {
									OpcjeGry.turaGracza1=false;
//									System.out.println(OpcjeGry.turaGracza1);
								}
							}
						}
					}
				}
				if(bierka==1||bierka==0&&gracz==0) {
					if(x2-2>=0&&y2+2<8) {
						int[] koordynaty4 = zmianaKoordynatow(x2-2, y2+2);
						int tak1 = czyJestBierka(players, koordynaty4, gracz2);
						int tak2 = czyJestBierka(players, koordynaty4, gracz);
						if(tak1==2&&tak2==2) {
							int[] koordynaty5 = zmianaKoordynatow(x2-1, y2+1);
							int tak3 = czyJestBierka(players, koordynaty5, gracz2);
							if(tak3<2) {
								OpcjeGry.nastepnyRuch=true;
//								System.out.println("3Z");
								if(gracz==1) {
									OpcjeGry.turaGracza1=true;
//									System.out.println(OpcjeGry.turaGracza1);
								}
								else {
									OpcjeGry.turaGracza1=false;
//									System.out.println(OpcjeGry.turaGracza1);
								}
							}
						}
					}
				}
				if(bierka==1||bierka==0&&gracz==0) {
					if(x2+2<8&&y2+2<8) {
						int[] koordynaty4 = zmianaKoordynatow(x2+2, y2+2);
						int tak1 = czyJestBierka(players, koordynaty4, gracz2);
						int tak2 = czyJestBierka(players, koordynaty4, gracz);
						if(tak1==2&&tak2==2) {
							int[] koordynaty5 = zmianaKoordynatow(x2+1, y2+1);
							int tak3 = czyJestBierka(players, koordynaty5, gracz2);
							if(tak3<2) {
								OpcjeGry.nastepnyRuch=true;
//								System.out.println("4Z");
								if(gracz==1) {
									OpcjeGry.turaGracza1=true;
//									System.out.println(OpcjeGry.turaGracza1);
								}
								else {
									OpcjeGry.turaGracza1=false;
//									System.out.println(OpcjeGry.turaGracza1);
								}
							}
						}
					}
				}
			}
			
		}
				
		
		
		return players;
	}
	
	public static boolean czySaPionkiGraczaA(long[] players) {
		boolean tak=false;
		int liczydlo = 0;
		for(int i =0;i<players.length/2;i++) {
			int j=0;
			for(;j<6;j++) {
				long playerstmp = players[i];
				playerstmp &=~((long)1<<(8+j*9));
				if((players[i]^playerstmp)>0) {
					liczydlo++;
				}
//				System.out.println(liczydlo);
			}
		}
		if(liczydlo==0)
			tak=true;
		
		return tak;
	}
	public static boolean czySaPionkiGraczaB(long[] players) {
		boolean tak=false;
		int liczydlo = 0;
		for(int i =2;i<players.length;i++) {
			int j=0;
			for(;j<6;j++) {
				long playerstmp = players[i];
				playerstmp &=~((long)1<<(8+j*9));
				if((players[i]^playerstmp)>0) {
					liczydlo++;
				}
//				System.out.println(liczydlo);
			}
		}
		if(liczydlo==0)
			tak=true;
		
		return tak;
	}

}
