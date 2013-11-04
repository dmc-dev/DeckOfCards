public class Card {

	public static final int CLUB=0;
	public static final int HEART=1;
	public static final int SPADE=2;
	public static final int DIMOND=3;
	
	public static final int TWO=0;
	public static final int THREE=1;
	public static final int FOUR=2;
	public static final int FIVE=3;	
	public static final int SIX=4;
	public static final int SEVEN=5;
	public static final int EIGHT=6;
	public static final int NINE=7;
	public static final int TEN=8;
	public static final int JACK=9;
	public static final int QUENE=10;
	public static final int KING=11;
	public static final int ACE=12;
	
	
	public static final String[] suits_long = {"club", "hart", "spade", "Dimond"}; 
	public static final String[] values_long = {"two","three","four","five","six","seven","eight","nine","ten","jack","quene","king","ace"};
	
	public static final String[] suits = {"C", "H", "S", "D"}; 
	public static final String[] values = {"2","3","4","5","6","7","8","9","T","J","Q","K","A"};
	
	private int suit=0;
	private int value=0;
	private int ID=0;	
	private boolean faceUp=true;
	
	public Card(int suit, int value){
		this.suit=suit;
		this.value=value;
	}
	public Card(int suit, int value, int id){
		this.suit=suit;
		this.value=value;
		this.ID=id;
	}
	public Card(int absCardNum){
		int id=0;
		for(int v=0; v<13; v++){
			for(int s=0; s<4; s++, id++){
				if(id==absCardNum){
					value=v; suit=s; ID=id;
				}
			}
		}
		
		//suit = absCardNum % s;
		//value = absCardNum % 14;
		//suit = value % suits.length;
		//ID = absCardNum;
	}
	public Card(byte suit, byte value){
		this.suit=(int)suit;
		this.value=(int)value;
	}
	public Card(byte[] b){
		value = (int)b[0];
		suit = (int)b[1];
	}
	public Card(Card c){
		suit=c.suit;
		value=c.value;
		ID=c.ID;
	}
	
	public int getSuit(){return suit;}
	public int getValue(){return value;}
	
	public byte getSuitByte(){return (byte)suit;}
	public byte getValueByte(){return (byte)value;}
	
	public boolean isSameSuit(Card c){return suit==c.suit;}
	public boolean isSameSuit(int value){return this.suit==value;}
	
	public boolean isSameValue(Card c){return value==c.value;}
	public boolean isSameValue(int value){return this.value==value;}
	
	public boolean isSameCard(Card c){return suit==c.suit && value==c.value;}
	public boolean isSameCard(int value, int suit){return this.value==value && this.suit==suit;}
	
	
	public boolean isHigher(Card c){return value > c.value;}
	public boolean isless(Card c){return value < c.value;}

	public boolean isValue(int v){return value==v;}
	public boolean isSuit(int s){return suit==s;}
	public boolean isCard(int v, int s){return value==v && suit==s;}
	
	public boolean isFaceUp(){return faceUp;}
	public void setFaceUp(boolean faceUp){this.faceUp=faceUp;}
	
	public String toString(){
		if(faceUp)
			return values[value]+"-"+suits[suit];
		return "<"+values[value]+"-"+suits[suit]+">";
	}

	public String toIdxString(){
		return Integer.toString(value)+Integer.toString(suit);
	}
	
	public int absCardNumber(){
		return ID;
	}
	
	public char[] toIdxChars(){
		char[] c= new char[2] ;
		c[0]= (char)value;
		c[1]= (char)value;
		return c;
	}
	
	public byte[] toBytes(){
		return new byte[]{(byte)value,(byte)suit};		
	}
}	